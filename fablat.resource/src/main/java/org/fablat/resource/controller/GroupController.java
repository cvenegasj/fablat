package org.fablat.resource.controller;

import java.security.Principal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fablat.resource.dto.FabberDTO;
import org.fablat.resource.dto.GroupDTO;
import org.fablat.resource.dto.GroupMemberDTO;
import org.fablat.resource.dto.SubGroupDTO;
import org.fablat.resource.entities.ActivityLog;
import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Group;
import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.model.dao.ActivityLogDAO;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.util.EmailServiceImpl;
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
@RequestMapping(value = "/auth/groups")
public class GroupController {

	private final DateTimeFormatter timeFormatterIn = DateTimeFormatter.ofPattern("h:m a");
	private final DateTimeFormatter timeFormatterOut = DateTimeFormatter.ofPattern("h:mm a");

	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private SubGroupDAO subGroupDAO;
	@Autowired
	private GroupMemberDAO groupMemberDAO;
	@Autowired
	private SubGroupMemberDAO subGroupMemberDAO;
	@Autowired
	private ActivityLogDAO activityLogDAO;
	@Autowired
    public EmailServiceImpl emailService;

	@RequestMapping(method = RequestMethod.GET)
	public List<GroupDTO> findAll(Principal principal) {
		List<GroupDTO> returnList = new ArrayList<GroupDTO>();
		
		for (Group group : groupDAO.findAllOrderDate()) {
			GroupDTO gDTO = convertToDTO(group);
			gDTO.setMembersCount(groupDAO.getMembersCount(group.getIdGroup()));
			gDTO.setSubGroupsCount(groupDAO.getSubGroupsCount(group.getIdGroup()));			
			
			gDTO.setAmIMember(groupDAO.checkIfMember(group.getIdGroup(), principal.getName()));
			
      	  	returnList.add(gDTO);
		}
	
		return returnList;
	}
	
	@RequestMapping(value = "/find-all-mine", method = RequestMethod.GET)
	public List<GroupDTO> findAllMine(Principal principal) {
		List<GroupDTO> returnList = new ArrayList<GroupDTO>();
		
		// Mapping user's group and subgroups
		for (GroupMember gm : groupMemberDAO.findAllByFabber(principal.getName())) {
			GroupDTO gDTO = convertToDTO(gm.getGroup());
			gDTO.setMembersCount(groupDAO.getMembersCount(gm.getGroup().getIdGroup()));
			gDTO.setSubGroupsCount(groupDAO.getSubGroupsCount(gm.getGroup().getIdGroup()));
			
			// additional properties
			gDTO.setAmIMember(true);
      	  	gDTO.setAmICoordinator(gm.getIsCoordinator());
			
      	  	// subgroups
      	  	List<SubGroupDTO> subGroups = new ArrayList<SubGroupDTO>();
      	  	for (SubGroupMember sgm : subGroupMemberDAO.findAllByGroupAndFabber(gm.getGroup().getIdGroup(), principal.getName())) {
      	  		SubGroupDTO sDTO = convertToDTO(sgm.getSubGroup());
      	  		sDTO.setMembersCount(subGroupDAO.getMembersCount(sgm.getSubGroup().getIdSubGroup()));
				sDTO.setAmIMember(true);
				sDTO.setAmICoordinator(sgm.getIsCoordinator());
      	  		
      	  		subGroups.add(sDTO);
      	  	}
      	  	gDTO.setSubGroups(subGroups);
      	  	returnList.add(gDTO);
		}	
		
		return returnList;
	}
	
	@RequestMapping(value = "/find-all-fabber/{idFabber}", method = RequestMethod.GET)
	public List<GroupDTO> findAllFabber(@PathVariable("idFabber") Integer idFabber) {
		List<GroupDTO> returnList = new ArrayList<GroupDTO>();
		
		// Mapping user's group and subgroups
		for (GroupMember gm : groupMemberDAO.findAllByFabber(idFabber)) {
			GroupDTO gDTO = convertToDTO(gm.getGroup());
			gDTO.setMembersCount(groupDAO.getMembersCount(gm.getGroup().getIdGroup()));
			gDTO.setSubGroupsCount(groupDAO.getSubGroupsCount(gm.getGroup().getIdGroup()));
			
			// additional properties
			gDTO.setAmIMember(true);
      	  	gDTO.setAmICoordinator(gm.getIsCoordinator());
			
      	  	// subgroups
      	  	List<SubGroupDTO> subGroups = new ArrayList<SubGroupDTO>();
      	  	for (SubGroupMember sgm : subGroupMemberDAO.findAllByGroupAndFabber(gm.getGroup().getIdGroup(), idFabber)) {
      	  		SubGroupDTO sDTO = convertToDTO(sgm.getSubGroup());
				sDTO.setAmIMember(true);
				sDTO.setAmICoordinator(sgm.getIsCoordinator());
      	  		
      	  		subGroups.add(sDTO);
      	  	}
      	  	gDTO.setSubGroups(subGroups);
      	  	returnList.add(gDTO);
		}	
		
		return returnList;
	}
	
	@RequestMapping(value = "/search/{searchText}", method = RequestMethod.GET)
	public List<GroupDTO> findByTerm(@PathVariable("searchText") String searchText) { 
		List<GroupDTO> returnList = new ArrayList<GroupDTO>();	
		for (Group g : groupDAO.findByTerm(searchText)) {
			GroupDTO gDTO = new GroupDTO();
			gDTO.setIdGroup(g.getIdGroup());
			gDTO.setName(g.getName());
			returnList.add(gDTO);
		}
		
		return returnList;
	}
	
	@RequestMapping(value = "/{idGroup}", method = RequestMethod.GET)
    public GroupDTO findOne(@PathVariable("idGroup") Integer idGroup, Principal principal) {
		Group group = groupDAO.findById(idGroup);
		GroupDTO gDTO = convertToDTO(group);
		
		// additional properties
		GroupMember userAsGroupMember = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		if (userAsGroupMember != null) {
			gDTO.setAmIMember(true);
			gDTO.setAmICoordinator(userAsGroupMember.getIsCoordinator());
		} else {
			gDTO.setAmIMember(false);
		}
		
		// group's subgroups
		List<SubGroupDTO> subGroups = new ArrayList<SubGroupDTO>();
		for (SubGroup sg : subGroupDAO.findAllByGroup(group.getIdGroup())) {
			SubGroupDTO sDTO = convertToDTO(sg);
			sDTO.setMembersCount(subGroupDAO.getMembersCount(sg.getIdSubGroup()));
			
			SubGroupMember userAsSubGroupMember = subGroupMemberDAO.findBySubGroupAndFabber(sg.getIdSubGroup(), principal.getName());
			if (userAsSubGroupMember != null) {
				sDTO.setAmIMember(true);
				sDTO.setAmICoordinator(userAsSubGroupMember.getIsCoordinator());
			} else {
				sDTO.setAmIMember(false);
			}
			
			subGroups.add(sDTO);
		}
		gDTO.setSubGroups(subGroups);
		
		// group's members
		List<GroupMemberDTO> members = new ArrayList<GroupMemberDTO>();
		for (GroupMember gm : groupMemberDAO.findAllByGroup(group.getIdGroup())) {
			GroupMemberDTO gmDTO = convertToDTO(gm);
			members.add(gmDTO);
		}
		gDTO.setMembers(members);
		
        return gDTO;
    }
	
	@RequestMapping(value = "/{idGroup}/verify-me", method = RequestMethod.GET)
    public GroupDTO verifyMe(@PathVariable("idGroup") Integer idGroup, Principal principal) {
		Group group = groupDAO.findById(idGroup);
		GroupDTO gDTO = convertToDTO(group);
		
		// additional properties
		GroupMember userAsGroupMember = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		if (userAsGroupMember != null) {
			gDTO.setAmIMember(true);
			gDTO.setAmICoordinator(userAsGroupMember.getIsCoordinator());
		} else {
			gDTO.setAmIMember(false);
		}
		
        return gDTO;
    }
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDTO create(@RequestBody GroupDTO groupDTO, Principal principal) {
		Group group = convertToEntity(groupDTO);
        group.setEnabled(true);
        // set creation datetime 
        Instant now = Instant.now();
        group.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        
        // creator
        GroupMember gm = new GroupMember();
        gm.setIsCoordinator(true);
        gm.setNotificationsEnabled(true);
        gm.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        gm.setFabber(fabberDAO.findByEmail(principal.getName()));
        gm.setGroup(group);
        group.getGroupMembers().add(gm);
        
        // create activity on group creation
        ActivityLog activity = new  ActivityLog();
        activity.setLevel(Resources.ACTIVITY_LEVEL_GROUP);
        activity.setType(Resources.ACTIVITY_TYPE_ORIGIN); // it's the origin of the group
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_EXTERNAL); // app-wide visibility
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setGroup(group);
        activity.setFabber(gm.getFabber());
        
        Group groupCreated = groupDAO.makePersistent(group);
        activityLogDAO.makePersistent(activity);
        
        return convertToDTO(groupCreated);
    }
	
	@RequestMapping(value = "/{idGroup}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("idGroup") Integer idGroup, @RequestBody GroupDTO groupDTO) throws ParseException {
		Group group = groupDAO.findById(idGroup);
		group.setName(groupDTO.getName());
		group.setDescription(groupDTO.getDescription());
		group.setReunionDay(groupDTO.getReunionDay());
		group.setReunionTime(groupDTO.getReunionTime() != null ? LocalTime.parse(groupDTO.getReunionTime(), timeFormatterIn) : null);
		group.setMainUrl(groupDTO.getMainUrl());
		group.setSecondaryUrl(groupDTO.getSecondaryUrl());
		group.setPhotoUrl(groupDTO.getPhotoUrl());
		
		groupDAO.makePersistent(group);
    }
	
	@RequestMapping(value = "/{idGroup}/update-avatar", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateAvatar(@PathVariable("idGroup") Integer idGroup, @RequestBody GroupDTO groupDTO) {
		Group group = groupDAO.findById(idGroup);
		group.setPhotoUrl(groupDTO.getPhotoUrl());
		System.out.println("PHOTO URL: " + groupDTO.getPhotoUrl());
		
		groupDAO.makePersistent(group);
    }
	
	@RequestMapping(value = "/{idGroup}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("idGroup") Integer idGroup) {
		groupDAO.makeTransient(groupDAO.findById(idGroup));
	}
	
	@RequestMapping(value = "/{idGroup}/join", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void join(@PathVariable("idGroup") Integer idGroup, Principal principal) {
		// check if is already member
		if (groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName()) != null) {
			return;
		}
		
		Group group = groupDAO.findById(idGroup);
		
		GroupMember member = new GroupMember();
		member.setIsCoordinator(groupDAO.getMembersCount(group.getIdGroup()) == 0 ? true : false);
		member.setNotificationsEnabled(true);
		// set creation datetime 
        Instant now = Instant.now();
        member.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));  
        
        member.setFabber(fabberDAO.findByEmail(principal.getName()));
        member.setGroup(group);
		groupMemberDAO.makePersistent(member);
		
		// generate activity
		ActivityLog activity = new  ActivityLog();
		activity.setLevel(Resources.ACTIVITY_LEVEL_GROUP);
        activity.setType(Resources.ACTIVITY_TYPE_USER_JOINED);
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_INTERNAL); // internal visibility
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setGroup(group);
        activity.setFabber(member.getFabber());
        activityLogDAO.makePersistent(activity);
	}
	
	@RequestMapping(value = "/{idGroup}/leave", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void leave(@PathVariable("idGroup") Integer idGroup, Principal principal) {
		GroupMember member = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		groupMemberDAO.makeTransient(member);
		Group group = groupDAO.findById(idGroup);
		
		// if the user was the last member, the group disappears
		if (groupDAO.getMembersCount(group.getIdGroup()) == 0) {
			groupDAO.makeTransient(group);
			return;
		}
		
		// if the user was the only coordinator, assign the oldest member as coordinator
		if (!groupMemberDAO.findAllByGroup(idGroup).stream().anyMatch(item -> item.getIsCoordinator())) {
			GroupMember oldestMember = groupMemberDAO.findAllByGroup(idGroup).stream().findFirst().get();
			oldestMember.setIsCoordinator(true);		
			groupMemberDAO.makePersistent(oldestMember);		
		}
		
		// generate activity
        ActivityLog activity = new  ActivityLog();
        activity.setLevel(Resources.ACTIVITY_LEVEL_GROUP);
        activity.setType(Resources.ACTIVITY_TYPE_USER_LEFT);
        activity.setVisibility(Resources.ACTIVITY_VISIBILITY_INTERNAL); // internal visibility
        Instant now = Instant.now();
        activity.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        activity.setGroup(group);
        activity.setFabber(member.getFabber());
        activityLogDAO.makePersistent(activity);
	}
	
	@RequestMapping(value = "/{idGroup}/members", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addMember(@PathVariable("idGroup") Integer idGroup, @RequestBody GroupMemberDTO groupMemberDTO, Principal principal) {
		//GroupMember me = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		// action only allowed to coordinators
		//if (!me.getIsCoordinator()) {
		//	return;
		//}
		
		GroupMember gm = groupMemberDAO.findByGroupAndFabber(idGroup, groupMemberDTO.getFabberId());
		// check if already member
		if (gm != null) {
			return;
		}
		
		gm = new GroupMember();
		gm.setIsCoordinator(false);
		gm.setNotificationsEnabled(true);
		Instant now = Instant.now();
		gm.setCreationDateTime(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
		gm.setFabber(fabberDAO.findById(groupMemberDTO.getFabberId()));
		gm.setGroup(groupDAO.findById(idGroup));
		
		groupMemberDAO.makePersistent(gm);
	}
	
	@RequestMapping(value = "/{idGroup}/members/{idGroupMember}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteMember(@PathVariable("idGroup") Integer idGroup, @PathVariable("idGroupMember") Integer idGroupMember, Principal principal) {
		GroupMember me = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		// action only allowed to coordinators
		if (!me.getIsCoordinator()) {
			return;
		}
		
		GroupMember member = groupMemberDAO.findById(idGroupMember);
		groupMemberDAO.makeTransient(member);
	}
	
	@RequestMapping(value = "/{idGroup}/members/send-invitation-email", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void sendInvitationEmail(@PathVariable("idGroup") Integer idGroup, @RequestBody FabberDTO fabberDTO) {
		
		Group g = groupDAO.findById(idGroup);
		
		emailService.sendHTMLMessage(fabberDTO.getEmail(),
                "Invitation to join a Fab Lat group", 
                "You have received an invitation to join the group " + g.getName() + ". <br />"
                		+ "Join the Fab Lat platform on <a href=\"fablat.net\">fablat.net</a>.");
	}
	
	@RequestMapping(value = "/{idGroup}/members/autocomplete/{searchText}", method = RequestMethod.GET)
	public List<HashMap<String, Object>> searchAutocomplete(@PathVariable("idGroup") Integer idGroup, @PathVariable("searchText") String searchText) {
		List<HashMap<String, Object>> models = new ArrayList<HashMap<String, Object>>();
		
		for (Fabber f : fabberDAO.findByTerm(searchText)) {
			// do not include already members
			if (groupMemberDAO.findByGroupAndFabber(idGroup, f.getIdFabber()) != null) {
				continue;
			}
			HashMap<String, Object> model = new HashMap<String, Object>();
			model.put("idFabber", f.getIdFabber());
			model.put("email", f.getEmail());
			model.put("firstName", f.getFirstName());
			model.put("lastName", f.getLastName());
			model.put("fullName", f.getFirstName() + " " + f.getLastName());
			
			models.add(model);
		}
		
		return models;
	}
	
	@RequestMapping(value = "/{idGroup}/name-coordinator", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void nameCoordinator(@PathVariable("idGroup") Integer idGroup, @RequestBody GroupMemberDTO groupMemberDTO, Principal principal) {
		GroupMember me = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		// action only allowed to coordinators
		if (!me.getIsCoordinator()) {
			return;
		}
		
		GroupMember member = groupMemberDAO.findById(groupMemberDTO.getIdGroupMember());
		member.setIsCoordinator(true);
		groupMemberDAO.makePersistent(member);
	}
	
	
	// ========== DTO conversion ==========
	
	private GroupDTO convertToDTO(Group group) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setIdGroup(group.getIdGroup());
		groupDTO.setName(group.getName());
		groupDTO.setDescription(group.getDescription());
		groupDTO.setReunionDay(group.getReunionDay());
		groupDTO.setReunionTime(group.getReunionTime() != null ? timeFormatterOut.format(group.getReunionTime()) : null);
		groupDTO.setMainUrl(group.getMainUrl());
		groupDTO.setSecondaryUrl(group.getSecondaryUrl());
		groupDTO.setPhotoUrl(group.getPhotoUrl());
		groupDTO.setCreationDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(group.getCreationDateTime()));
		
		return groupDTO;
	}
	
	private Group convertToEntity(GroupDTO gDTO) {
		Group g = new Group();
		g.setName(gDTO.getName());
		g.setDescription(gDTO.getDescription());
		
		return g;
	}
	
	private SubGroupDTO convertToDTO(SubGroup subGroup) {
		SubGroupDTO subGroupDTO = new SubGroupDTO();
		subGroupDTO.setIdSubGroup(subGroup.getIdSubGroup());
		subGroupDTO.setName(subGroup.getName());
		subGroupDTO.setDescription(subGroup.getDescription());
		
		return subGroupDTO;
	}
	
	private GroupMemberDTO convertToDTO(GroupMember gm) {
		GroupMemberDTO gmDTO = new GroupMemberDTO();
		gmDTO.setIdGroupMember(gm.getIdGroupMember());
		gmDTO.setFirstName(gm.getFabber().getFirstName());
		gmDTO.setLastName(gm.getFabber().getLastName());
		gmDTO.setEmail(gm.getFabber().getEmail());
		gmDTO.setIsCoordinator(gm.getIsCoordinator());
		gmDTO.setCreationDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(gm.getCreationDateTime()));
		gmDTO.setFabberId(gm.getFabber().getIdFabber());
		
		return gmDTO;
	}
	
}
