package org.fablat.resource.controller;

import java.security.Principal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.fablat.resource.dto.SubGroupDTO;
import org.fablat.resource.dto.SubGroupMemberDTO;
import org.fablat.resource.dto.WorkshopDTO;
import org.fablat.resource.entities.ActivityLog;
import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.ActivityLogDAO;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.util.Resources;
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

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private final DateTimeFormatter timeFormatterIn = DateTimeFormatter.ofPattern("h:m a");
	private final DateTimeFormatter timeFormatterOut = DateTimeFormatter.ofPattern("h:mm a");
	private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
	private final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("MMM d(EEE) h:mm a");
	
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
	@Autowired
	private ActivityLogDAO activityLogDAO;

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
		for (SubGroupMember sgm : subGroupMemberDAO.findAllBySubGroup(subGroup.getIdSubGroup())) {
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
		subGroup.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));

		// parent group
		subGroup.setGroup(groupDAO.findById(subGroupDTO.getGroupId()));
		// creator
		SubGroupMember sgm = new SubGroupMember();
		sgm.setIsCoordinator(true);
		sgm.setNotificationsEnabled(true);
		sgm.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
		GroupMember gm = groupMemberDAO.findByGroupAndFabber(subGroupDTO.getGroupId(), principal.getName());
		sgm.setGroupMember(gm);
		sgm.setSubGroup(subGroup);
		subGroup.getSubGroupMembers().add(sgm);
		
		// create activities on subgroup creation
        ActivityLog activity = new  ActivityLog();
        activity.setLevel(Resources.ACTIVITY_LEVEL_SUBGROUP);
        activity.setType(Resources.ACTIVITY_TYPE_ORIGIN); // it's the origin of the subgroup
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_EXTERNAL); // app-wide visibility
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setSubGroup(subGroup);
        activity.setGroup(subGroup.getGroup());
        activity.setFabber(sgm.getGroupMember().getFabber());
        // activity for the parent group
        ActivityLog activity2 = new ActivityLog();
        activity2.setLevel(Resources.ACTIVITY_LEVEL_GROUP);
        activity2.setType(Resources.ACTIVITY_TYPE_SUBGROUP_CREATED);
        activity2.setVisibility(Resources.ACTIVITY_VISIBILITY_INTERNAL);
        activity2.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity2.setGroup(groupDAO.findById(subGroupDTO.getGroupId()));
        activity2.setFabber(gm.getFabber());
		
		SubGroup subGroupCreated = subGroupDAO.makePersistent(subGroup);
		activityLogDAO.makePersistent(activity);
		activityLogDAO.makePersistent(activity2);

		return convertToDTO(subGroupCreated);
	}
	
	@RequestMapping(value = "/{idSubGroup}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("idSubGroup") Integer idSubGroup, @RequestBody SubGroupDTO subGroupDTO) throws ParseException {
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		subGroup.setName(subGroupDTO.getName());
		subGroup.setDescription(subGroupDTO.getDescription());
		subGroup.setReunionDay(subGroupDTO.getReunionDay());
		subGroup.setReunionTime(subGroupDTO.getReunionTime() != null ? LocalTime.parse(subGroupDTO.getReunionTime(), timeFormatterIn) : null);
		subGroup.setMainUrl(subGroupDTO.getMainUrl());
		subGroup.setSecondaryUrl(subGroupDTO.getSecondaryUrl());
		subGroup.setPhotoUrl(subGroupDTO.getPhotoUrl());
		
		subGroupDAO.makePersistent(subGroup);
    }
	
	@RequestMapping(value = "/{idSubGroup}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("idSubGroup") Integer idSubGroup, Principal principal) {
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		
		// activity for the parent group
        ActivityLog activity = new ActivityLog();
        activity.setLevel(Resources.ACTIVITY_LEVEL_GROUP);
        activity.setType(Resources.ACTIVITY_TYPE_SUBGROUP_DELETED);
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_INTERNAL);
        Instant now = Instant.now();
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setGroup(subGroup.getGroup());
        activity.setFabber(groupMemberDAO.findByGroupAndFabber(subGroup.getGroup().getIdGroup(), principal.getName()).getFabber());
        
        activityLogDAO.makePersistent(activity);
        subGroupDAO.makeTransient(subGroup); 
	}
	
	@RequestMapping(value = "/{idSubGroup}/join", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void join(@PathVariable("idSubGroup") Integer idSubGroup, Principal principal) {
		// check if is already member
		if (subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName()) != null) {
			return;
		}
		
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		
		SubGroupMember member = new SubGroupMember();
		member.setIsCoordinator(subGroup.getSubGroupMembers().size() == 0 ? true : false);
		member.setNotificationsEnabled(true);
		// set creation datetime 
        Instant now = Instant.now();
        member.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));  
        
        GroupMember gm = groupMemberDAO.findByGroupAndFabber(subGroup.getGroup().getIdGroup(), principal.getName());
        // if user is not member of parent group, we add it
        if (gm == null) {
        	gm = new GroupMember();
        	gm.setIsCoordinator(false);
        	gm.setNotificationsEnabled(true);
        	gm.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        	gm.setFabber(fabberDAO.findByEmail(principal.getName()));
        	gm.setGroup(subGroup.getGroup());
        	groupMemberDAO.makePersistent(gm);
        }
      
        member.setGroupMember(gm);
        member.setSubGroup(subGroup);
		subGroupMemberDAO.makePersistent(member);
		
		// generate activity
        ActivityLog activity = new  ActivityLog();
        activity.setLevel(Resources.ACTIVITY_LEVEL_SUBGROUP);
        activity.setType(Resources.ACTIVITY_TYPE_USER_JOINED);
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_INTERNAL); // internal visibility
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setSubGroup(subGroup);
        activity.setFabber(member.getGroupMember().getFabber());
        activityLogDAO.makePersistent(activity);
	}
	
	@RequestMapping(value = "/{idSubGroup}/leave", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void leave(@PathVariable("idSubGroup") Integer idSubGroup, Principal principal) {
		SubGroupMember member = subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName());
		subGroupMemberDAO.makeTransient(member);
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		
		// if the user was the last member, the subgroup disappears
		if (subGroup.getSubGroupMembers().size() == 0) {
			subGroupDAO.makeTransient(subGroup);
			return;
		}
		
		// if the user was the only coordinator, assign the oldest member as coordinator
		if (!subGroup.getSubGroupMembers().stream().anyMatch(item -> item.getIsCoordinator())) {
			SubGroupMember oldestMember = subGroup.getSubGroupMembers().stream().findFirst().get();
			oldestMember.setIsCoordinator(true);		
			subGroupMemberDAO.makePersistent(oldestMember);		
		}
		
		// generate activity
        ActivityLog activity = new  ActivityLog();
        activity.setLevel(Resources.ACTIVITY_LEVEL_SUBGROUP);
        activity.setType(Resources.ACTIVITY_TYPE_USER_LEFT);
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_INTERNAL); // internal visibility
        Instant now = Instant.now();
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setSubGroup(subGroup);
        activity.setFabber(member.getGroupMember().getFabber());
        activityLogDAO.makePersistent(activity);
	}
	
	@RequestMapping(value = "/{idSubGroup}/add-member", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addMember(@PathVariable("idSubGroup") Integer idSubGroup, @RequestBody SubGroupMemberDTO subGroupMemberDTO, Principal principal) {
		SubGroupMember me = subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName());
		// action only allowed to coordinators
		if (!me.getIsCoordinator()) {
			return;
		}
		
		SubGroup subGroup = subGroupDAO.findById(idSubGroup);
		SubGroupMember sgm = new SubGroupMember();
		sgm.setIsCoordinator(subGroupMemberDTO.getIsCoordinator());
		sgm.setNotificationsEnabled(true);
		Instant now = Instant.now();
        sgm.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        // sgm.setGroupMember(groupMemberDAO.findByGroupAndFabber(subGroup.getGroup().getIdGroup(), email));
        sgm.setSubGroup(subGroup);
		
		subGroupMemberDAO.makePersistent(sgm);
	}
	
	@RequestMapping(value = "/{idSubGroup}/delete-member", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteMember(@PathVariable("idSubGroup") Integer idSubGroup, @RequestBody SubGroupMemberDTO subGroupMemberDTO, Principal principal) {
		SubGroupMember me = subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName());
		// action only allowed to coordinators
		if (!me.getIsCoordinator()) {
			return;
		}
		
		SubGroupMember member = subGroupMemberDAO.findById(subGroupMemberDTO.getIdSubGroupMember());
		subGroupMemberDAO.makeTransient(member);
	}
	
	@RequestMapping(value = "/{idSubGroup}/name-coordinator", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void nameCoordinator(@PathVariable("idSubGroup") Integer idSubGroup, @RequestBody SubGroupMemberDTO subGroupMemberDTO, Principal principal) {
		SubGroupMember me = subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName());
		// action only allowed to coordinators
		if (!me.getIsCoordinator()) {
			return;
		}
		
		SubGroupMember member = subGroupMemberDAO.findById(subGroupMemberDTO.getIdSubGroupMember());
		member.setIsCoordinator(true);
		subGroupMemberDAO.makePersistent(member);
	}

	
	// ========== DTO conversion ==========
	
	private SubGroupDTO convertToDTO(SubGroup subGroup) {
		SubGroupDTO sDTO = new SubGroupDTO();
		sDTO.setIdSubGroup(subGroup.getIdSubGroup());
		sDTO.setName(subGroup.getName());
		sDTO.setDescription(subGroup.getDescription());
		sDTO.setReunionDay(subGroup.getReunionDay());
		sDTO.setReunionTime(subGroup.getReunionTime() != null ? timeFormatterOut.format(subGroup.getReunionTime()) : null);
		sDTO.setMainUrl(subGroup.getMainUrl());
		sDTO.setSecondaryUrl(subGroup.getSecondaryUrl());
		sDTO.setPhotoUrl(subGroup.getPhotoUrl());
		sDTO.setCreationDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(subGroup.getCreationDateTime()));
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
		wDTO.setStartTime(timeFormatterOut.format(workshop.getStartDateTime()));
		wDTO.setEndDate(dateFormatter.format(workshop.getEndDateTime()));
		wDTO.setEndTime(timeFormatterOut.format(workshop.getEndDateTime()));
		
		wDTO.setStartDateDay(workshop.getStartDateTime().getDayOfMonth());
		wDTO.setStartDateMonth(monthFormatter.format(workshop.getStartDateTime()));
		wDTO.setStartDateFormatted(dateFormatter2.format(workshop.getStartDateTime()));
		wDTO.setEndDateFormatted(dateFormatter2.format(workshop.getEndDateTime()));
		
		wDTO.setStartDateTimeISO(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(workshop.getStartDateTime()));
		wDTO.setEndDateTimeISO(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(workshop.getEndDateTime()));
		
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
		sgmDTO.setCreationDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(sgm.getCreationDateTime()));
		sgmDTO.setFabberId(sgm.getGroupMember().getFabber().getIdFabber());

		return sgmDTO;
	}

}
