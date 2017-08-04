package org.fablat.resource.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fablat.resource.dto.SubGroupDTO;
import org.fablat.resource.dto.SubGroupMemberDTO;
import org.fablat.resource.dto.WorkshopDTO;
import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/subgroups")
public class SubGroupController {

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy h:mm a");
	private SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM");
	private SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MMMMM d(EEE) h:mm a");
	
	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private SubGroupDAO subGroupDAO;
	@Autowired
	private SubGroupMemberDAO subGroupMemberDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private GroupMemberDAO groupMemberDAO;

	@RequestMapping(value = "/{idSubGroup}", method = RequestMethod.GET)
	public SubGroupDTO findOne(@PathVariable("idSubGroup") Integer idSubGroup, Principal principal) {
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		SubGroupDTO sDTO = convertToDTO(subGroup);

		// additional properties
		SubGroupMember userAsSubGroupMember = subGroupMemberDAO
				.findBySubGroupAndFabber(idSubGroup, principal.getName());
		if (userAsSubGroupMember != null) {
			sDTO.setAmIMember(true);
			sDTO.setAmICoordinator(userAsSubGroupMember.getIsCoordinator());
		} else {
			sDTO.setAmIMember(false);
		}

		// subgroup's workshops
		List<WorkshopDTO> workshops = new ArrayList<WorkshopDTO>();
		for (Workshop w : subGroup.getWorkshops()) {
			WorkshopDTO wDTO = convertToDTO(w);
			workshops.add(wDTO);
		}
		sDTO.setWorkshops(workshops);

		// subgroup's members
		List<SubGroupMemberDTO> members = new ArrayList<SubGroupMemberDTO>();
		for (SubGroupMember sgm : subGroup.getSubGroupMembers()) {
			SubGroupMemberDTO sgmDTO = convertToDTO(sgm);
			members.add(sgmDTO);
		}
		sDTO.setMembers(members);

		return sDTO;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public SubGroupDTO create(@RequestBody SubGroupDTO subGroupDTO, Principal principal) {
		SubGroup subGroup = convertToEntity(subGroupDTO);
		subGroup.setEnabled(true);
		// set creation datetime
		Instant now = Instant.now();
		ZonedDateTime zdtLima = now.atZone(ZoneId.of("GMT-05:00"));
		subGroup.setCreationDateTime(Date.from(zdtLima.toInstant()));

		// parent group
		subGroup.setGroup(groupDAO.findById(subGroupDTO.getGroupId()));
		// creator
		SubGroupMember sgm = new SubGroupMember();
		sgm.setIsCoordinator(true);
		sgm.setNotificationsEnabled(true);
		sgm.setCreationDateTime(Date.from(zdtLima.toInstant()));
		GroupMember gm = groupMemberDAO.findByGroupAndFabber(subGroupDTO.getGroupId(), principal.getName());
		sgm.setGroupMember(gm);
		sgm.setSubGroup(subGroup);

		subGroup.getSubGroupMembers().add(sgm);
		SubGroup subGroupCreated = subGroupDAO.makePersistent(subGroup);

		return convertToDTO(subGroupCreated);
	}
	
	@RequestMapping(value = "/{idSubGroup}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("idSubGroup") Integer idSubGroup, @RequestBody SubGroupDTO subGroupDTO) throws ParseException {
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		subGroup.setName(subGroupDTO.getName());
		subGroup.setDescription(subGroupDTO.getDescription());
		subGroup.setReunionDay(subGroupDTO.getReunionDay());
		subGroup.setReunionTime(subGroupDTO.getReunionTime() != null ? timeFormatter.parse(subGroupDTO.getReunionTime()) : null);
		subGroup.setMainUrl(subGroupDTO.getMainUrl());
		subGroup.setSecondaryUrl(subGroupDTO.getSecondaryUrl());
		subGroup.setPhotoUrl(subGroupDTO.getPhotoUrl());
		
		subGroupDAO.makePersistent(subGroup);
    }
	
	@RequestMapping(value = "/{idSubGroup}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("idSubGroup") Integer idSubGroup) {
		subGroupDAO.makeTransient(subGroupDAO.findById(idSubGroup)); 
	}
	
	@RequestMapping(value = "/{idSubGroup}/join", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void join(@PathVariable("idSubGroup") Integer idSubGroup, Principal principal) {
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		
		SubGroupMember member = new SubGroupMember();
		member.setIsCoordinator(subGroup.getSubGroupMembers().size() == 0 ? true : false);
		member.setNotificationsEnabled(true);
		// set creation datetime 
        Instant now = Instant.now();
        ZonedDateTime zdtLima = now.atZone(ZoneId.of("GMT-05:00"));
        member.setCreationDateTime(Date.from(zdtLima.toInstant()));  
        
        GroupMember gm = groupMemberDAO.findByGroupAndFabber(subGroup.getGroup().getIdGroup(), principal.getName());
        // if user is not member of parent group, we add it
        if (gm == null) {
        	gm = new GroupMember();
        	gm.setIsCoordinator(false);
        	gm.setNotificationsEnabled(true);
        	gm.setCreationDateTime(Date.from(zdtLima.toInstant()));
        	gm.setFabber(fabberDAO.findByEmail(principal.getName()));
        	gm.setGroup(subGroup.getGroup());
        }
      
        member.setGroupMember(gm);
        member.setSubGroup(subGroup);
		subGroupMemberDAO.makePersistent(member);
	}

	
	// ========== DTO conversion ==========
	
	private SubGroupDTO convertToDTO(SubGroup subGroup) {
		SubGroupDTO sDTO = new SubGroupDTO();
		sDTO.setIdSubGroup(subGroup.getIdSubGroup());
		sDTO.setName(subGroup.getName());
		sDTO.setDescription(subGroup.getDescription());
		sDTO.setReunionDay(subGroup.getReunionDay());
		sDTO.setReunionTime(subGroup.getReunionTime() != null ? timeFormatter.format(subGroup.getReunionTime()) : null);
		sDTO.setMainUrl(subGroup.getMainUrl());
		sDTO.setSecondaryUrl(subGroup.getSecondaryUrl());
		sDTO.setPhotoUrl(subGroup.getPhotoUrl());
		sDTO.setCreationDateTime(dateTimeFormatter.format(subGroup.getCreationDateTime()));
		sDTO.setGroupId(subGroup.getGroup().getIdGroup());
		sDTO.setGroupName(subGroup.getGroup().getName());

		sDTO.setMembersCount(subGroup.getSubGroupMembers().size());

		return sDTO;
	}
	
	private SubGroup convertToEntity(SubGroupDTO sgDTO) {
		SubGroup sg = new SubGroup();
		sg.setName(sgDTO.getName());
		sg.setDescription(sgDTO.getDescription());
		
		return sg;
	}

	private WorkshopDTO convertToDTO(Workshop workshop) {
		WorkshopDTO wDTO = new WorkshopDTO();
		wDTO.setIdWorkshop(workshop.getIdWorkshop());
		wDTO.setReplicationNumber(workshop.getReplicationNumber());
		wDTO.setName(workshop.getName());
		// workshopDTO.setDescription(workshop.getDescription());
		wDTO.setStartDate(dateFormatter.format(workshop.getStartDateTime()));
		wDTO.setStartTime(timeFormatter.format(workshop.getStartDateTime()));
		wDTO.setEndDate(dateFormatter.format(workshop.getEndDateTime()));
		wDTO.setEndTime(timeFormatter.format(workshop.getEndDateTime()));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(workshop.getStartDateTime());
		wDTO.setStartDateDay(cal.get(Calendar.DAY_OF_MONTH));
		wDTO.setStartDateMonth(monthFormatter.format(workshop.getStartDateTime()));
		wDTO.setStartDateFormatted(dateFormatter2.format(workshop.getStartDateTime()));
		wDTO.setEndDateFormatted(dateFormatter2.format(workshop.getEndDateTime()));
		
		wDTO.setIsPaid(workshop.getIsPaid());
		wDTO.setPrice(workshop.getPrice());
		wDTO.setCurrency(workshop.getCurrency());
		wDTO.setLabName(workshop.getLocation().getLab() != null ? workshop.getLocation().getLab().getName() : null);

		return wDTO;
	}

	private SubGroupMemberDTO convertToDTO(SubGroupMember sgm) {
		SubGroupMemberDTO sgmDTO = new SubGroupMemberDTO();
		sgmDTO.setIdSubGroupMember(sgm.getIdSubGroupMember());
		sgmDTO.setFirstName(sgm.getGroupMember().getFabber().getFirstName());
		sgmDTO.setLastName(sgm.getGroupMember().getFabber().getLastName());
		sgmDTO.setEmail(sgm.getGroupMember().getFabber().getEmail());
		sgmDTO.setIsCoordinator(sgm.getIsCoordinator());
		sgmDTO.setCreationDateTime(dateTimeFormatter.format(sgm.getCreationDateTime()));
		sgmDTO.setFabberId(sgm.getGroupMember().getFabber().getIdFabber());

		return sgmDTO;
	}

}
