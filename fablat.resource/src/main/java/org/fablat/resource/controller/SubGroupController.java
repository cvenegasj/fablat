package org.fablat.resource.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fablat.resource.dto.GroupDTO;
import org.fablat.resource.dto.SubGroupDTO;
import org.fablat.resource.dto.WorkshopDTO;
import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Group;
import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.serializable.SubGroupDetailResource;
import org.fablat.resource.serializable.SubGroupMemberResource;
import org.fablat.resource.serializable.SubGroupRequestWrapper;
import org.fablat.resource.serializable.SubGroupResource;
import org.fablat.resource.serializable.WorkshopResource;
import org.fablat.resource.util.Resources;
import org.fablat.resource.util.View;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/auth/subgroups")
public class SubGroupController {

	private SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy h:mm a");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
    private ModelMapper modelMapper;
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
		SubGroupDTO subGroupDTO = convertToDTO(subGroup);
		
		SubGroupMember userAsSubGroupMember = subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName());
		if (userAsSubGroupMember != null) {
			subGroupDTO.setAmIMember(true);
			subGroupDTO.setAmICoordinator(userAsSubGroupMember.getIsCoordinator());
		} else {
			subGroupDTO.setAmIMember(false);
		}
		
		// subgroup's workshops
		List<WorkshopDTO> workshops = new ArrayList<WorkshopDTO>();
		for (Workshop w : subGroup.getWorkshops()) {
			WorkshopDTO wDTO = convertToDTO(w); 
			workshops.add(wDTO);
		}
		subGroupDTO.setWorkshops(workshops);
		
		// subgroup's members
		
		
		
		return subGroupDTO;
	}
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public SubGroupDTO create(@RequestBody SubGroupDTO subGroupDTO, Principal principal) {
		SubGroup subGroup = modelMapper.map(subGroupDTO, SubGroup.class);
		subGroup.setEnabled(true);
		// set creation datetime 
        Instant now = Instant.now();
        ZonedDateTime zdtLima = now.atZone(ZoneId.of("GMT-05:00"));
        subGroup.setCreationDateTime(Date.from(zdtLima.toInstant())); 
		
        // parent group
        subGroup.setGroup(groupDAO.findById(subGroupDTO.getIdGroup()));
        GroupMember gm = groupMemberDAO.findByGroupAndFabber(subGroupDTO.getIdGroup(), principal.getName());
       	// creator
        SubGroupMember sgm = new SubGroupMember();
        sgm.setIsCoordinator(true);
        sgm.setNotificationsEnabled(true);
        sgm.setCreationDateTime(Date.from(zdtLima.toInstant()));
        sgm.setGroupMember(gm);
        sgm.setSubGroup(subGroup);
        
        subGroup.getSubGroupMembers().add(sgm);
        SubGroup subGroupCreated = subGroupDAO.makePersistent(subGroup);
       
		return convertToDTO(subGroupCreated);
	}
	
	//========== DTO conversion==========
	private SubGroupDTO convertToDTO(SubGroup subGroup) {
		SubGroupDTO subGroupDTO = new SubGroupDTO();
		subGroupDTO.setIdSubGroup(subGroup.getIdSubGroup());
		subGroupDTO.setName(subGroup.getName());
		subGroupDTO.setDescription(subGroup.getDescription());
		subGroupDTO.setReunionDay(subGroup.getReunionDay());
		subGroupDTO.setReunionTime(subGroup.getReunionTime() != null ? timeFormatter.format(subGroup.getReunionTime()) : null);
		subGroupDTO.setMainUrl(subGroup.getMainUrl());
		subGroupDTO.setSecondaryUrl(subGroup.getSecondaryUrl());
		subGroupDTO.setPhotoUrl(subGroup.getPhotoUrl());
		subGroupDTO.setCreationDateTime(dateTimeFormatter.format(subGroup.getCreationDateTime()));
		subGroupDTO.setIdGroup(subGroup.getGroup().getIdGroup());
		subGroupDTO.setGroupName(subGroup.getGroup().getName());
		
		subGroupDTO.setMembersCount(subGroup.getSubGroupMembers().size());
		
		return subGroupDTO;
	}
	
	private WorkshopDTO convertToDTO(Workshop workshop) {
		WorkshopDTO workshopDTO = new WorkshopDTO();
		workshopDTO.setIdWorkshop(workshop.getIdWorkshop());
		workshopDTO.setReplicationNumber(workshop.getReplicationNumber());
		workshopDTO.setName(workshop.getName());
		// workshopDTO.setDescription(workshop.getDescription());
		workshopDTO.setStartDate(dateFormatter.format(workshop.getStartDateTime()));
		workshopDTO.setStartTime(timeFormatter.format(workshop.getStartDateTime()));
		workshopDTO.setEndDate(dateFormatter.format(workshop.getEndDateTime()));
		workshopDTO.setEndTime(timeFormatter.format(workshop.getEndDateTime()));
		workshopDTO.setIsPaid(workshop.getIsPaid());
		workshopDTO.setPrice(workshop.getPrice());
		workshopDTO.setCurrency(workshop.getCurrency());
		workshopDTO.setLabName(workshop.getLocation().getLab() != null ? workshop.getLocation().getLab().getName() : null);
		
		return workshopDTO;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*============== old api =============*/

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<SubGroupResource> save(@RequestBody SubGroupRequestWrapper auxRequestWrapper,
			Principal principal) throws ParseException {

		Fabber fabber = fabberDAO.findByEmail(principal.getName());
		Group group = groupDAO.findById(auxRequestWrapper.getIdGroup());

		// SubGroup creation
		SubGroup subGroup = new SubGroup();
		subGroup.setGroup(group);
		subGroup.setName(auxRequestWrapper.getName());
		subGroup.setDescription(auxRequestWrapper.getDescription());
		// Reunion Day (optional)
		subGroup.setReunionDay(auxRequestWrapper.getReunionDay() != null ? auxRequestWrapper.getReunionDay() : null);
		// Reunion Time (optional)
		if (auxRequestWrapper.getReunionTime() != null) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			Date d1 = format.parse(auxRequestWrapper.getReunionTime());
			subGroup.setReunionTime(d1);
		} else {
			subGroup.setReunionTime(null);
		}
		// creation DateTime
		DateTime dt = new DateTime();
		DateTime dtLocal = dt.withZone(DateTimeZone.forID("America/Lima"));
		subGroup.setCreationDateTime(dtLocal.toDate());

		subGroup.setEnabled(true);

		// SubGroup member creation
		SubGroupMember sm = new SubGroupMember();
		sm.setIsCoordinator(true);
		sm.setNotificationsEnabled(true);
		// creation DateTime
		sm.setCreationDateTime(dtLocal.toDate());
		// sm.setFabber(fabber);

		sm.setSubGroup(subGroup);
		subGroup.getSubGroupMembers().add(sm);

		SubGroup s = subGroupDAO.makePersistent(subGroup);

		SubGroupResource gr = new SubGroupResource(s.getIdSubGroup(), s.getName(), s.getGroup().getName(), "JOINED",
				sm.getNotificationsEnabled(), s.getSubGroupMembers().size());

		return new ResponseEntity<SubGroupResource>(gr, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestBody SubGroup group, Principal principal) {

		SubGroup s = subGroupDAO.findById(group.getIdSubGroup());
		Fabber f = fabberDAO.findByEmail(principal.getName());

		// TODO: Action reserved only for ROLE_ADMIN_LAT
		boolean isLatAdmin = false;

		for (RoleFabber rf : f.getRoleFabbers()) {
			if (rf.getRole().getName().equals(Resources.ROLE_ADMIN_GENERAL)) {
				isLatAdmin = true;
				break;
			}
		}

		// action only allowed for ROLE_ADMIN_LAT
		if (isLatAdmin) {
			subGroupDAO.makeTransient(s);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/brief", method = RequestMethod.GET)
	public ResponseEntity<SubGroupResource> brief(@RequestParam(value = "idSubGroup") Integer idSubGroup) {

		SubGroup s = subGroupDAO.findById(idSubGroup);

		SubGroupResource sr = new SubGroupResource();
		sr.setIdSubGroup(s.getIdSubGroup());
		sr.setName(s.getName());
		sr.setWorkshopsNumber(s.getWorkshops().size());

		return new ResponseEntity<SubGroupResource>(sr, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<SubGroupDetailResource> detail(@RequestParam(value = "idSubGroup") Integer idSubGroup,
			Principal principal) {

		SubGroup s = subGroupDAO.findById(idSubGroup);

		SubGroupDetailResource sdr = new SubGroupDetailResource();
		sdr.setIdSubGroup(s.getIdSubGroup());
		sdr.setName(s.getName());
		sdr.setDescription(s.getDescription());
		sdr.setReunionDay(s.getReunionDay() != null ? s.getReunionDay() : null);

		DateFormat df = new SimpleDateFormat("HH:mm");
		sdr.setReunionTime(s.getReunionTime() != null ? df.format(s.getReunionTime()) : null);
		sdr.setGroupName(s.getGroup().getName());
		sdr.setCurrentUserStatus("NOT_JOINED");
		
		SubGroupMember sm = subGroupMemberDAO.findBySubGroupAndFabber(idSubGroup, principal.getName());
		
		if (sm != null) {
			sdr.setCurrentUserStatus("JOINED");
			sdr.setNotificationsEnabled(sm.getNotificationsEnabled());
		}

		sdr.setCoordinators(subGroupDAO.findCoordinators(idSubGroup));
		sdr.setCollaborators(subGroupDAO.findCollaborators(idSubGroup));

		return new ResponseEntity<SubGroupDetailResource>(sdr, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-exclusive-user", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listExclusiveUser(Principal principal) {

		List<SubGroupResource> srs = new ArrayList<SubGroupResource>();
		List<SubGroup> subGroupsUser = subGroupDAO.findFabberSubGroups(principal.getName());

		// create the response list adding the user status related to each subgroup
		for (SubGroup sx : subGroupsUser) {
			SubGroupResource sr = new SubGroupResource(sx.getIdSubGroup(), sx.getName(), sx.getGroup().getName(),
					"JOINED", null, sx.getSubGroupMembers().size());

			srs.add(sr);
		}

		return new ResponseEntity<List<SubGroupResource>>(srs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-all-user", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listAllUser(Principal principal) {

		List<SubGroupResource> srs = new ArrayList<SubGroupResource>();

		List<SubGroup> sAll = subGroupDAO.findAllOrderedAsc();
		List<SubGroup> sUser = subGroupDAO.findFabberSubGroups(principal.getName());

		// create the response list adding the user status related to each subgroup
		for (SubGroup sx : sAll) {
			SubGroupResource sr = new SubGroupResource(sx.getIdSubGroup(), sx.getName(), sx.getGroup().getName(),
					"NOT_JOINED", null, sx.getSubGroupMembers().size());

			if (sUser.contains(sx)) {
				sr.setCurrentUserStatus("JOINED");
			}

			srs.add(sr);
		}

		return new ResponseEntity<List<SubGroupResource>>(srs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-managed-user", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listManagedUser(Principal principal) {

		List<SubGroupResource> srs = new ArrayList<SubGroupResource>();
		
		for (SubGroup s : subGroupDAO.findManagedSubGroups(principal.getName())) {
			
			SubGroupMember sm = subGroupMemberDAO.findBySubGroupAndFabber(s.getIdSubGroup(), principal.getName());
			
			SubGroupResource sr = new SubGroupResource(s.getIdSubGroup(),
					s.getName(), s.getGroup().getName(), "JOINED",
					sm.getNotificationsEnabled(), s.getSubGroupMembers().size());
			srs.add(sr);
		}

		return new ResponseEntity<List<SubGroupResource>>(srs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-not-managed-user", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listNotManagedUser(Principal principal) {

		List<SubGroupResource> srs = new ArrayList<SubGroupResource>();
		
		for (SubGroup s : subGroupDAO.findNotManagedSubGroups(principal.getName())) {
			
			SubGroupMember sm = subGroupMemberDAO.findBySubGroupAndFabber(s.getIdSubGroup(), principal.getName());
			
			SubGroupResource sr = new SubGroupResource(s.getIdSubGroup(),
					s.getName(), s.getGroup().getName(), "JOINED",
					sm.getNotificationsEnabled(), s.getSubGroupMembers().size());
			srs.add(sr);
		}

		return new ResponseEntity<List<SubGroupResource>>(srs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-all-admin", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listAllAdmin() {

		List<SubGroupResource> srs = new ArrayList<SubGroupResource>();
		List<SubGroup> sAll = subGroupDAO.findAllOrderedAsc();

		// create the response list adding the user status related to each subgroup
		for (SubGroup sx : sAll) {
			SubGroupResource sr = new SubGroupResource(sx.getIdSubGroup(), sx.getName(), sx.getGroup().getName(),
					"NOT_JOINED", null, sx.getSubGroupMembers().size());

			srs.add(sr);
		}

		return new ResponseEntity<List<SubGroupResource>>(srs, HttpStatus.OK);
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public ResponseEntity<Void> join(@RequestBody SubGroup subGroup, Principal principal) {

		Fabber fabber = fabberDAO.findByEmail(principal.getName());
		SubGroup s = subGroupDAO.findById(subGroup.getIdSubGroup());

		// SubGroup member creation
		SubGroupMember sm = new SubGroupMember();
		sm.setIsCoordinator(false);
		sm.setNotificationsEnabled(true);
		// creation DateTime
		DateTime dt = new DateTime();
		DateTime dtLocal = dt.withZone(DateTimeZone.forID("America/Lima"));
		sm.setCreationDateTime(dtLocal.toDate());

		// sm.setFabber(fabber);
		sm.setSubGroup(s);
		subGroupMemberDAO.makePersistent(sm);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/list-coordinators", method = RequestMethod.GET)
	public ResponseEntity<List<Fabber>> listCoordinators(@RequestParam(value = "idSubGroup") Integer idSubGroup) {

		List<Fabber> fabbers = subGroupDAO.findCoordinators(idSubGroup);

		return new ResponseEntity<List<Fabber>>(fabbers, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/list-collaborators", method = RequestMethod.GET)
	public ResponseEntity<List<Fabber>> listCollaborators(@RequestParam(value = "idSubGroup") Integer idSubGroup) {

		List<Fabber> fabbers = subGroupDAO.findCollaborators(idSubGroup);

		return new ResponseEntity<List<Fabber>>(fabbers, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-members-not-me", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupMemberResource>> listMembersNotMe(
			@RequestParam(value = "idSubGroup") Integer idSubGroup, Principal principal) {

		List<SubGroupMemberResource> members = new ArrayList<SubGroupMemberResource>();
		SubGroup s = subGroupDAO.findById(idSubGroup);

		for (SubGroupMember sm : s.getSubGroupMembers()) {
			// not include current logged in user. Current user is assigned by
			// default as coordinator of
			// the replication, therefore its name should not be selectable for
			// avoiding redundancy
			/*if (sm.getFabber().getUsername().equals(principal.getName())) {
				continue;
			}

			SubGroupMemberResource smr = new SubGroupMemberResource();
			smr.setIdSubGroupMember(sm.getIdSubGroupMember());
			smr.setFullName(sm.getFabber().getFirstName() + " " + sm.getFabber().getLastName());
			smr.setEmail(sm.getFabber().getEmail());
			smr.setImageUrl("img/avatar.png"); 

			members.add(smr); */
		}

		return new ResponseEntity<List<SubGroupMemberResource>>(members, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming-workshops", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcomingWorkshops(
			@RequestParam(value = "idSubGroup") Integer idSubGroup) {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		/*List<WorkshopEvent> upcomingWorkshops = workshopEventDAO.findUpcomingBySubGroup(idSubGroup);

		for (WorkshopEvent workshop : upcomingWorkshops) {
			WorkshopResource wr = new WorkshopResource();
			wr.setIdWorkshop(workshop.getIdWorkshopEvent());
			wr.setReplicationNumber(workshop.getReplicationNumber());

			SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
			wr.setDateTime(format3.format(workshop.getDateTime()));
			wr.setIsPaid(workshop.getIsPaid());
			wr.setPrice(workshop.getPrice());
			wr.setCurrency(workshop.getCurrency());
			wr.setMembersCount(workshop.getWorkshopEventTutors().size());
			wr.setSubGroupName(workshop.getSubGroup().getName());

			workshops.add(wr);
		}*/

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/delete-user", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> params, Principal principal) {

		SubGroup s = subGroupDAO.findById(Integer.parseInt(params.get("idSubGroup")));

		/*for (SubGroupMember sm : s.getSubGroupMembers()) {
			if (sm.getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				subGroupMemberDAO.makeTransient(sm);
				break;
			}
		}*/

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/name-coordinator", method = RequestMethod.POST)
	public ResponseEntity<Fabber> nameCoordinator(@RequestBody Map<String, String> params, Principal principal) {

		SubGroup s = subGroupDAO.findById(Integer.parseInt(params.get("idSubGroup")));
		Fabber fabber = null;

		/*for (SubGroupMember sm : s.getSubGroupMembers()) {
			if (sm.getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				fabber = sm.getFabber();
				sm.setIsCoordinator(true);
				subGroupMemberDAO.makePersistent(sm);
				break;
			}
		} */

		return new ResponseEntity<Fabber>(fabber, HttpStatus.OK);
	}

	@RequestMapping(value = "/get-current-user-status", method = RequestMethod.GET)
	public Map<String, Object> getCurrentUserStatus(@RequestParam(value = "idSubGroup") Integer idSubGroup,
			Principal principal) {

		SubGroup s = subGroupDAO.findById(idSubGroup);

		Map<String, Object> model = new HashMap<String, Object>();
		Boolean isSubGroupMember = false;
		Boolean isCoordinator = false;
		
		SubGroupMember sm = subGroupMemberDAO.findBySubGroupAndFabber(s.getIdSubGroup(), principal.getName());
		
		if (sm != null) {
			isSubGroupMember = true;
			isCoordinator = sm.getIsCoordinator();
		}

		model.put("isSubGroupMember", isSubGroupMember);
		model.put("isCoordinator", isCoordinator);
		return model;
	}
}
