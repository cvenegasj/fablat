package org.fablat.resource.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.fablat.resource.dto.WorkshopDTO;
import org.fablat.resource.dto.WorkshopTutorDTO;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.entities.WorkshopTutor;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public/workshops")
public class WorkshopPublicController {
	
	private final DateTimeFormatter dateTimeFormatterCalendar = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"); // for google calendar
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
	private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
	private final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("MMM d(EEE) h:mm a");
	
	@Autowired
	private WorkshopDAO workshopDAO;

	@RequestMapping(value = "/upcoming", method = RequestMethod.GET)
    public List<WorkshopDTO> findUpcoming() {
		List<WorkshopDTO> returnList = new ArrayList<WorkshopDTO>();
		
        // find all workshops after today
		for (Workshop w : workshopDAO.findAllAfterDate(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))) {
			WorkshopDTO wDTO = convertToDTO(w);
			// workshop's tutors
			List<WorkshopTutorDTO> tutors = new ArrayList<WorkshopTutorDTO>();
			for (WorkshopTutor wt : w.getWorkshopTutors()) {
				WorkshopTutorDTO wtDTO = convertToDTO(wt);
				tutors.add(wtDTO);
			}
			wDTO.setTutors(tutors);
			returnList.add(wDTO);
		}
		
		return returnList;
	}
	
	// ========== DTO conversion ==========
	
	private WorkshopDTO convertToDTO(Workshop workshop) {
		WorkshopDTO wDTO = new WorkshopDTO();
		wDTO.setIdWorkshop(workshop.getIdWorkshop());
		wDTO.setReplicationNumber(workshop.getReplicationNumber());
		wDTO.setName(workshop.getName());
		wDTO.setDescription(workshop.getDescription());
		wDTO.setStartDate(dateFormatter.format(workshop.getStartDateTime()));
		wDTO.setStartTime(timeFormatter.format(workshop.getStartDateTime()));
		wDTO.setEndDate(dateFormatter.format(workshop.getEndDateTime()));
		wDTO.setEndTime(timeFormatter.format(workshop.getEndDateTime()));
		
		wDTO.setStartDateDay(workshop.getStartDateTime().getDayOfMonth());
		wDTO.setStartDateMonth(monthFormatter.format(workshop.getStartDateTime()));
		wDTO.setStartDateFormatted(dateFormatter2.format(workshop.getStartDateTime()));
		wDTO.setEndDateFormatted(dateFormatter2.format(workshop.getEndDateTime()));
		
		wDTO.setStartDateTimeISO(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(workshop.getStartDateTime()));
		wDTO.setEndDateTimeISO(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(workshop.getEndDateTime()));
		
		wDTO.setStartDateTimeCalendar(dateTimeFormatterCalendar.format(workshop.getStartDateTime()));
		wDTO.setEndDateTimeCalendar(dateTimeFormatterCalendar.format(workshop.getEndDateTime()));
		
		wDTO.setIsPaid(workshop.getIsPaid());
		wDTO.setPrice(workshop.getPrice());
		wDTO.setCurrency(workshop.getCurrency());
		wDTO.setFacebookUrl(workshop.getFacebookUrl());
		wDTO.setTicketsUrl(workshop.getTicketsUrl());
		wDTO.setCreationDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(workshop.getCreationDateTime()));
		wDTO.setLocationId(workshop.getLocation().getIdLocation());
		wDTO.setLocationAddress(workshop.getLocation().getAddress1());
		wDTO.setLocationCity(workshop.getLocation().getCity());
		wDTO.setLocationCountry(workshop.getLocation().getCountry());
		wDTO.setLocationLatitude(workshop.getLocation().getLatitude());
		wDTO.setLocationLongitude(workshop.getLocation().getLongitude());
		wDTO.setLabName(workshop.getLocation().getLab() != null ? workshop.getLocation().getLab().getName() : null);
		wDTO.setSubGroupId(workshop.getSubGroup().getIdSubGroup());
		wDTO.setSubGroupName(workshop.getSubGroup().getName());
		wDTO.setGroupId(workshop.getSubGroup().getGroup().getIdGroup());
		wDTO.setGroupName(workshop.getSubGroup().getGroup().getName());
		
		return wDTO;
	}
	
	private WorkshopTutorDTO convertToDTO(WorkshopTutor wt) {
		WorkshopTutorDTO wtDTO = new WorkshopTutorDTO();
		wtDTO.setIdWorkshopTutor(wt.getIdWorkshopTutor());
		wtDTO.setFirstName(wt.getSubGroupMember().getGroupMember().getFabber().getFirstName());
		wtDTO.setLastName(wt.getSubGroupMember().getGroupMember().getFabber().getLastName());
		wtDTO.setEmail(wt.getSubGroupMember().getGroupMember().getFabber().getEmail());
		wtDTO.setFabberId(wt.getSubGroupMember().getGroupMember().getFabber().getIdFabber());
		
		return wtDTO;
	}
	
}
