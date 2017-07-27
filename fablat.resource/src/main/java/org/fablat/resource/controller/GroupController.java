package org.fablat.resource.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fablat.resource.dto.GroupDTO;
import org.fablat.resource.dto.SubGroupDTO;
import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Group;
import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.modelmapper.ModelMapper;
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

	private SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy h:mm a");
	@Autowired
    private ModelMapper modelMapper;
	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private GroupMemberDAO groupMemberDAO;
	@Autowired
	private SubGroupMemberDAO subGroupMemberDAO;

	@RequestMapping(method = RequestMethod.GET)
	public List<GroupDTO> findAll(Principal principal) {
		List<GroupDTO> returnList = new ArrayList<GroupDTO>();
		
		for (Group group : groupDAO.findAll()) {
			GroupDTO gDTO = convertToDTO(group);
			
			GroupMember userAsGroupMember = groupMemberDAO.findByGroupAndFabber(group.getIdGroup(), principal.getName());
			if (userAsGroupMember != null) {
				gDTO.setAmIMember(true);
				gDTO.setAmICoordinator(userAsGroupMember.getIsCoordinator());
			} else {
				gDTO.setAmIMember(false);
			}
      	  	
			// subgroups
			List<SubGroupDTO> subGroups = new ArrayList<SubGroupDTO>();
			for (SubGroup sg : group.getSubGroups()) {
				SubGroupDTO sDTO = convertToDTO(sg);
				
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
      	  	returnList.add(gDTO);
		}
	
		return returnList;
	}
	
	@RequestMapping(value = "/findAllMine", method = RequestMethod.GET)
	public List<GroupDTO> findAllMine(Principal principal) {
		Fabber me = fabberDAO.findByEmail(principal.getName());
		List<GroupDTO> returnList = new ArrayList<GroupDTO>();
		
		// Mapping user's group and subgroups
		for (GroupMember gm : me.getGroupMembers()) {
			GroupDTO gDTO = convertToDTO(gm.getGroup());
			// additional properties
			gDTO.setAmIMember(true);
      	  	gDTO.setAmICoordinator(gm.getIsCoordinator());
			
      	  	// subgroups
      	  	List<SubGroupDTO> subGroups = new ArrayList<SubGroupDTO>();
      	  	for (SubGroupMember sgm : gm.getSubGroupMembers()) {
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
	
	@RequestMapping(value = "/{idGroup}", method = RequestMethod.GET)
    public GroupDTO findOne(@PathVariable("idGroup") Integer idGroup, Principal principal) {
		Group group = groupDAO.findById(idGroup);
		GroupDTO groupDTO = convertToDTO(group);
		
		GroupMember userAsGroupMember = groupMemberDAO.findByGroupAndFabber(idGroup, principal.getName());
		if (userAsGroupMember != null) {
			groupDTO.setAmIMember(true);
			groupDTO.setAmICoordinator(userAsGroupMember.getIsCoordinator());
		} else {
			groupDTO.setAmIMember(false);
		}
		
		// group's subgroups
		List<SubGroupDTO> subGroups = new ArrayList<SubGroupDTO>();
		for (SubGroup sg : group.getSubGroups()) {
			SubGroupDTO sDTO = convertToDTO(sg);
			
			SubGroupMember userAsSubGroupMember = subGroupMemberDAO.findBySubGroupAndFabber(sg.getIdSubGroup(), principal.getName());
			if (userAsSubGroupMember != null) {
				sDTO.setAmIMember(true);
				sDTO.setAmICoordinator(userAsSubGroupMember.getIsCoordinator());
			} else {
				sDTO.setAmIMember(false);
			}
			
			subGroups.add(sDTO);
		}
		groupDTO.setSubGroups(subGroups);
		
		// group's members
		
		
		
        return groupDTO;
    }
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDTO create(@RequestBody GroupDTO groupDTO, Principal principal) {
        Group group = modelMapper.map(groupDTO, Group.class);
        group.setEnabled(true);
        // set creation datetime 
        Instant now = Instant.now();
        ZonedDateTime zdtLima = now.atZone(ZoneId.of("GMT-05:00"));
        group.setCreationDateTime(Date.from(zdtLima.toInstant()));     
        
        // creator
        GroupMember gm = new GroupMember();
        gm.setIsCoordinator(true);
        gm.setNotificationsEnabled(true);
        gm.setCreationDateTime(Date.from(zdtLima.toInstant()));
        gm.setFabber(fabberDAO.findByEmail(principal.getName()));
        gm.setGroup(group);
        
        group.getGroupMembers().add(gm);
        Group groupCreated = groupDAO.makePersistent(group);
        
        return convertToDTO(groupCreated);
    }
	
	@RequestMapping(value = "/{idGroup}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("idGroup") Integer idGroup, @RequestBody GroupDTO groupDTO) throws ParseException {
		Group group = groupDAO.findById(idGroup);
		group.setName(groupDTO.getName());
		group.setDescription(groupDTO.getDescription());
		group.setReunionDay(groupDTO.getReunionDay());
	
		Date reunionTime = timeFormatter.parse(groupDTO.getReunionTime());
		group.setReunionTime(reunionTime);
		
		group.setMainUrl(groupDTO.getMainUrl());
		group.setSecondaryUrl(groupDTO.getSecondaryUrl());
		group.setPhotoUrl(groupDTO.getPhotoUrl());
		
		groupDAO.makePersistent(group);
    }
	
	//========== DTO conversion==========
	private GroupDTO convertToDTO(Group group) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setIdGroup(group.getIdGroup());
		groupDTO.setName(group.getName());
		groupDTO.setDescription(group.getDescription());
		groupDTO.setReunionDay(group.getReunionDay());
		groupDTO.setReunionTime(group.getReunionTime() != null ? timeFormatter.format(group.getReunionTime()) : null);
		groupDTO.setMainUrl(group.getMainUrl());
		groupDTO.setSecondaryUrl(group.getSecondaryUrl());
		groupDTO.setPhotoUrl(group.getPhotoUrl());
		groupDTO.setCreationDateTime(dateTimeFormatter.format(group.getCreationDateTime()));
		
		groupDTO.setMembersCount(group.getGroupMembers().size());
		
		return groupDTO;
	}
	
	private SubGroupDTO convertToDTO(SubGroup subGroup) {
		SubGroupDTO subGroupDTO = new SubGroupDTO();
		subGroupDTO.setIdSubGroup(subGroup.getIdSubGroup());
		subGroupDTO.setName(subGroup.getName());
		subGroupDTO.setDescription(subGroup.getDescription());
		subGroupDTO.setMembersCount(subGroup.getSubGroupMembers().size());
		
		return subGroupDTO;
	}
	
}
