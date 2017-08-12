package org.fablat.resource.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.fablat.resource.dto.GroupActivityDTO;
import org.fablat.resource.dto.SubGroupActivityDTO;
import org.fablat.resource.entities.GroupActivity;
import org.fablat.resource.entities.SubGroupActivity;
import org.fablat.resource.model.dao.GroupActivityDAO;
import org.fablat.resource.model.dao.SubGroupActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth/activities")
public class ActivityController {
	
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Autowired
	private GroupActivityDAO groupActivityDAO;
	@Autowired
	private SubGroupActivityDAO subGroupActivityDAO;

	@RequestMapping(value = "/find-all-external", method = RequestMethod.GET)
	public List<Object> findAllExternal(Principal principal) {
		// Single list with all types of activities
		List<Object> objectList = new ArrayList<Object>();
		
		for (GroupActivity ga : groupActivityDAO.findAllExternal()) {
			GroupActivityDTO gaDTO = new GroupActivityDTO();
			gaDTO.setIdGroupActivity(ga.getIdGroupActivity());
			gaDTO.setType(ga.getType());
			gaDTO.setVisibility(ga.getVisibility());
			gaDTO.setCreationDateTime(dateTimeFormatter.format(ga.getCreationDateTime()));
			gaDTO.setCreationDateTimeRaw(ga.getCreationDateTime());
			gaDTO.setGroupId(ga.getGroup().getIdGroup());
			gaDTO.setGroupName(ga.getGroup().getName());
			gaDTO.setFabberId(ga.getGroupMember().getFabber().getIdFabber());
			gaDTO.setFabberFirstName(ga.getGroupMember().getFabber().getFirstName());
			gaDTO.setFabberLastName(ga.getGroupMember().getFabber().getLastName());
			
			objectList.add(gaDTO);
		}
		
		for (SubGroupActivity sa : subGroupActivityDAO.findAllExternal()) {
			SubGroupActivityDTO saDTO = new SubGroupActivityDTO();
			saDTO.setIdSubGroupActivity(sa.getIdSubGroupActivity());
			saDTO.setType(sa.getType());
			saDTO.setVisibility(sa.getVisibility());
			saDTO.setCreationDateTime(dateTimeFormatter.format(sa.getCreationDateTime()));
			saDTO.setCreationDateTimeRaw(sa.getCreationDateTime());
			saDTO.setSubGroupId(sa.getSubGroup().getIdSubGroup());
			saDTO.setSubGroupName(sa.getSubGroup().getName());
			saDTO.setGroupId(sa.getSubGroup().getGroup().getIdGroup());
			saDTO.setGroupName(sa.getSubGroup().getGroup().getName());
			saDTO.setFabberId(sa.getSubGroupMember().getGroupMember().getFabber().getIdFabber());
			saDTO.setFabberFirstName(sa.getSubGroupMember().getGroupMember().getFabber().getFirstName());
			saDTO.setFabberLastName(sa.getSubGroupMember().getGroupMember().getFabber().getLastName());
			
			objectList.add(saDTO);
		}

		// Order by date desc
		Collections.sort(objectList, new Comparator<Object>() {
		  public int compare(Object o1, Object o2) {
			  Date dateTime1 = new Date();
			  Date dateTime2 = new Date();
			  if (o1 instanceof GroupActivityDTO && o2 instanceof GroupActivityDTO) {
				  dateTime1 = ((GroupActivityDTO) o1).getCreationDateTimeRaw();
				  dateTime2 = ((GroupActivityDTO) o2).getCreationDateTimeRaw();
			  } else if (o1 instanceof GroupActivityDTO && o2 instanceof SubGroupActivityDTO) {
				  dateTime1 = ((GroupActivityDTO) o1).getCreationDateTimeRaw();
				  dateTime2 = ((SubGroupActivityDTO) o2).getCreationDateTimeRaw(); 
			  } else if (o1 instanceof SubGroupActivityDTO && o2 instanceof GroupActivityDTO) {
				  dateTime1 = ((SubGroupActivityDTO) o1).getCreationDateTimeRaw();
				  dateTime2 = ((GroupActivityDTO) o2).getCreationDateTimeRaw(); 
			  } else {
				  dateTime1 = ((SubGroupActivityDTO) o1).getCreationDateTimeRaw();
				  dateTime2 = ((SubGroupActivityDTO) o2).getCreationDateTimeRaw(); 
			  }
			  
			  return dateTime2.compareTo(dateTime1);
		  }
		});
		
		return objectList;
	}
	
}
