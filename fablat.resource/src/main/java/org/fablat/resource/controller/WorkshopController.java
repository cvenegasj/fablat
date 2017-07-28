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

import org.fablat.resource.dto.WorkshopDTO;
import org.fablat.resource.dto.WorkshopTutorDTO;
import org.fablat.resource.entities.Location;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.entities.WorkshopTutor;
import org.fablat.resource.model.dao.LocationDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.fablat.resource.model.dao.WorkshopTutorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/workshops")
public class WorkshopController {
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy h:mm a");
	private SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM");
	private SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MMMMM d(EEE) h:mm a");
	
	@Autowired
	private SubGroupDAO subGroupDAO;
	@Autowired
	private SubGroupMemberDAO subGroupMemberDAO;
	@Autowired
	private WorkshopDAO workshopDAO;
	@Autowired
	private WorkshopTutorDAO workshopTutorDAO;
	@Autowired
	private LocationDAO locationDAO;
	
	@RequestMapping(value = "/{idWorkshop}", method = RequestMethod.GET)
    public WorkshopDTO findOne(@PathVariable("idWorkshop") Integer idWorkshop, Principal principal) {
		Workshop workshop = workshopDAO.findById(idWorkshop);
		WorkshopDTO wDTO = convertToDTO(workshop);
		
		// additional properties
		WorkshopTutor userAsWorkshopTutor = workshopTutorDAO.findByWorkshopAndFabber(idWorkshop, principal.getName());
		if (userAsWorkshopTutor != null) {
			wDTO.setAmITutor(true);
		}
		
		// workshop's tutors
		List<WorkshopTutorDTO> tutors = new ArrayList<WorkshopTutorDTO>();
		for (WorkshopTutor wt : workshop.getWorkshopTutors()) {
			WorkshopTutorDTO wtDTO = convertToDTO(wt);
			tutors.add(wtDTO);
		}
		wDTO.setTutors(tutors);
		
		return wDTO;
	}
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public WorkshopDTO create(@RequestBody WorkshopDTO workshopDTO, Principal principal) throws ParseException {
		Workshop workshop = convertToEntity(workshopDTO);
		workshop.setEnabled(true);
		// set creation datetime 
        Instant now = Instant.now();
        ZonedDateTime zdtLima = now.atZone(ZoneId.of("GMT-05:00"));
        workshop.setCreationDateTime(Date.from(zdtLima.toInstant()));
        
        // Parent SubGroup
        workshop.setSubGroup(subGroupDAO.findById(workshopDTO.getSubGroupId()));
        // Creator
        WorkshopTutor wt = new WorkshopTutor();
        wt.setSubGroupMember(subGroupMemberDAO.findBySubGroupAndFabber(workshopDTO.getSubGroupId(), principal.getName()));
        wt.setWorkshop(workshop);
        
        workshop.getWorkshopTutors().add(wt);
        
        // Location
        if (workshopDTO.getLocationId() == null) {
        	// create new Location
        	Location location = new Location();
        	location.setAddress1(workshopDTO.getLocationAddress());
        	location.setCity(workshopDTO.getLocationCity());
        	location.setCountry(workshopDTO.getLocationCountry());
        	location.setLatitude(workshopDTO.getLocationLatitude());
        	location.setLongitude(workshopDTO.getLocationLongitude());
        	workshop.setLocation(location);
        } else {
        	workshop.setLocation(locationDAO.findById(workshopDTO.getLocationId()));
        }
        
        // replication number: inserting and updating
        // first getting the workshops count before 'now'
        Integer beforeCount = workshopDAO.countAllBySubGroupBeforeDate(workshopDTO.getSubGroupId(), Date.from(zdtLima.toInstant()));
        workshop.setReplicationNumber(beforeCount + 1);
        
        Workshop workshopCreated = workshopDAO.makePersistent(workshop);
        
        // then updating the replication number of the workshops 'after'
        updateReplicationNumbers(workshopDTO.getSubGroupId(), Date.from(zdtLima.toInstant()));
		
		return convertToDTO(workshopCreated);
	}
	
	private void updateReplicationNumbers(Integer idSubGroup, Date date) {
		for (Workshop w : workshopDAO.findAllBySubGroupAfterDate(idSubGroup, date)) {
        	w.setReplicationNumber(w.getReplicationNumber() + 1);
        	workshopDAO.makePersistent(w);
        }
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
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(workshop.getStartDateTime());
		wDTO.setStartDateDay(cal.get(Calendar.DAY_OF_MONTH));
		wDTO.setStartDateMonth(monthFormatter.format(workshop.getStartDateTime()));
		wDTO.setStartDateFormatted(dateFormatter2.format(workshop.getStartDateTime()));
		wDTO.setEndDateFormatted(dateFormatter2.format(workshop.getEndDateTime()));
		
		wDTO.setIsPaid(workshop.getIsPaid());
		wDTO.setPrice(workshop.getPrice());
		wDTO.setCurrency(workshop.getCurrency());
		wDTO.setFacebookUrl(workshop.getFacebookUrl());
		wDTO.setTicketsUrl(workshop.getTicketsUrl());
		wDTO.setCreationDateTime(dateTimeFormatter.format(workshop.getCreationDateTime()));
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
	
	private Workshop convertToEntity(WorkshopDTO wDTO) throws ParseException {
		Workshop w = new Workshop();
		w.setName(wDTO.getName());
		w.setDescription(wDTO.getDescription());
		// format the date and time strings
		w.setStartDateTime(dateTimeFormatter.parse(wDTO.getStartDate() + " " + wDTO.getStartTime()));
		w.setEndDateTime(dateTimeFormatter.parse(wDTO.getEndDate() + " " + wDTO.getEndTime()));
		w.setIsPaid(wDTO.getIsPaid());
		w.setPrice(wDTO.getPrice());
		w.setCurrency(wDTO.getCurrency());
		w.setFacebookUrl(wDTO.getFacebookUrl());
		w.setTicketsUrl(wDTO.getTicketsUrl());
			
		return w;
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
