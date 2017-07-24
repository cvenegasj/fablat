package org.fablat.resource.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.entities.WorkshopEvent;
import org.fablat.resource.entities.WorkshopEventTutor;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.model.dao.WorkshopEventDAO;
import org.fablat.resource.model.dao.WorkshopEventTutorDAO;
import org.fablat.resource.serializable.SubGroupMemberResource;
import org.fablat.resource.serializable.WorkshopDetailResource;
import org.fablat.resource.serializable.WorkshopRequestWrapper;
import org.fablat.resource.serializable.WorkshopResource;
import org.fablat.resource.util.Resources;
import org.fablat.resource.util.View;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/workshop")
public class WorkshopEventController {

	@Autowired
	private WorkshopEventDAO workshopEventDAO;
	@Autowired
	private WorkshopEventTutorDAO workshopEventMemberDAO;
	@Autowired
	private SubGroupMemberDAO subGroupMemberDAO;
	@Autowired
	private SubGroupDAO subGroupDAO;
	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private LabDAO labDAO;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<WorkshopResource> save(@RequestBody WorkshopRequestWrapper workshopRequestWrapper,
			Principal principal) throws ParseException {

		SubGroup subGroup = subGroupDAO.findById(workshopRequestWrapper.getIdSubGroup());

		WorkshopEvent workshop = new WorkshopEvent();
		workshop.setSubGroup(subGroup);
		workshop.setReplicationNumber(subGroup.getWorkshopEvents().size() + 1);

		// Parsing dates
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date d1 = format.parse(workshopRequestWrapper.getTime());
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		Date d2 = format2.parse(workshopRequestWrapper.getDate());

		// Single DateTime creation
		Calendar dCal = Calendar.getInstance();
		dCal.setTime(d2);
		Calendar tCal = Calendar.getInstance();
		tCal.setTime(d1);
		dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
		dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));

		workshop.setDateTime(dCal.getTime());

		workshop.setIsPaid(workshopRequestWrapper.getIsPaid());
		if (workshop.getIsPaid()) {
			workshop.setPrice(workshopRequestWrapper.getPrice());
			workshop.setCurrency(workshopRequestWrapper.getCurrency());
		}

		// creation DateTime
		DateTime dt = new DateTime();
		DateTime dtLocal = dt.withZone(DateTimeZone.forID("America/Lima"));
		workshop.setCreationDateTime(dtLocal.toDate());
		workshop.setEnabled(true);

		// add current user as coordinator
		WorkshopEventTutor wmb = new WorkshopEventTutor();
		wmb.setSubGroupMember(subGroupMemberDAO.findBySubGroupAndFabber(workshopRequestWrapper.getIdSubGroup(),
				principal.getName()));
		wmb.setWorkshopEvent(workshop);
		workshop.getWorkshopEventTutors().add(wmb);

		// rest of members
		for (SubGroupMemberResource smr : workshopRequestWrapper.getCoordinators()) {
			SubGroupMember sm = subGroupMemberDAO.findById(smr.getIdSubGroupMember());
			WorkshopEventTutor wm = new WorkshopEventTutor();
			wm.setSubGroupMember(sm);
			wm.setWorkshopEvent(workshop);
			workshop.getWorkshopEventTutors().add(wm);
		}

		for (SubGroupMemberResource smr : workshopRequestWrapper.getCollaborators()) {
			SubGroupMember sm = subGroupMemberDAO.findById(smr.getIdSubGroupMember());
			WorkshopEventTutor wm = new WorkshopEventTutor();
			wm.setSubGroupMember(sm);
			wm.setWorkshopEvent(workshop);
			workshop.getWorkshopEventTutors().add(wm);
		}

		workshop = workshopEventDAO.makePersistent(workshop);

		/*for (LocationResource lr : workshopRequestWrapper.getLocations()) {

			WorkshopLocation wl = new WorkshopLocation();
			wl.setWorkshopEvent(workshop);

			// If the location belongs to a fab lab, associate it to the
			// workshop, else create a new one
			if (lr.getIdLab() != null) {
				Lab lab = labDAO.findById(lr.getIdLab());
				wl.setLocation(lab.getLocation());
			} else {
				Location location = new Location();
				location.setAddress1(lr.getAddress1());
				location.setAddress2(lr.getAddress2());
				location.setCity(lr.getCity());
				location.setCountry(lr.getCountry());
				wl.setLocation(location);
			}

			wl = workshopLocationDAO.makePersistent(wl);
		} */

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

		return new ResponseEntity<WorkshopResource>(wr, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestBody WorkshopEvent workshop, Principal principal) {

		WorkshopEvent w = workshopEventDAO.findById(workshop.getIdWorkshopEvent());
		Fabber f = fabberDAO.findByUsername(principal.getName());

		boolean isLatAdmin = false;

		for (RoleFabber rf : f.getRoleFabbers()) {
			if (rf.getRole().getName().equals(Resources.ROLE_ADMIN_GENERAL)) {
				isLatAdmin = true;
				break;
			}
		}

		// action only allowed for ROLE_ADMIN_LAT
		if (isLatAdmin) {
			workshopEventDAO.makeTransient(w);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcoming() {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<WorkshopEvent> upcomingWorkshops = workshopEventDAO.findUpcoming();

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
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<WorkshopDetailResource> detail(@RequestParam(value = "idWorkshop") Integer idWorkshop,
			Principal principal) {

		WorkshopEvent workshop = workshopEventDAO.findById(idWorkshop);

		WorkshopDetailResource wdr = new WorkshopDetailResource();
		wdr.setIdWorkshop(workshop.getIdWorkshopEvent());
		wdr.setReplicationNumber(workshop.getReplicationNumber());

		SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
		wdr.setDateTime(format3.format(workshop.getDateTime()));

		wdr.setIsPaid(workshop.getIsPaid());
		wdr.setPrice(workshop.getPrice());
		wdr.setCurrency(workshop.getCurrency());
		wdr.setSubGroupName(workshop.getSubGroup().getName());

		// check if current user is member of workshop
		Fabber fabber = fabberDAO.findByUsername(principal.getName());
		List<Fabber> coordinators = workshopEventDAO.findCoordinators(idWorkshop);
		List<Fabber> collaborators = workshopEventDAO.findCollaborators(idWorkshop);

		wdr.setCurrentUserStatus("NOT_JOINED");

		if (coordinators.contains(fabber) || collaborators.contains(fabber)) {
			wdr.setCurrentUserStatus("JOINED");
		}

		wdr.setCoordinators(coordinators);
		wdr.setCollaborators(collaborators);

		/*for (WorkshopLocation wl : workshop.getWorkshopLocations()) {
			LocationResource lr = new LocationResource();
			lr.setIdLab(wl.getLocation().getLab().getIdLab());
			lr.setName(wl.getLocation().getLab().getName());
			lr.setAddress1(wl.getLocation().getAddress1());
			lr.setAddress2(wl.getLocation().getAddress2());
			lr.setCity(wl.getLocation().getCity());
			lr.setCountry(wl.getLocation().getCountry());
			lr.setLatitude(wl.getLocation().getLatitude());
			lr.setLongitude(wl.getLocation().getLongitude());

			wdr.getLocations().add(lr);
		} */

		return new ResponseEntity<WorkshopDetailResource>(wdr, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming-user", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcomingUser(Principal principal) {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<WorkshopEvent> upcomingWorkshops = workshopEventDAO.findUpcomingByUser(principal.getName());

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
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-past-user", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listPastUser(Principal principal) {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<WorkshopEvent> pastWorkshops = workshopEventDAO.findPastByUser(principal.getName());

		for (WorkshopEvent workshop : pastWorkshops) {
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
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming-admin", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcomingAdmin() {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<WorkshopEvent> upcomingWorkshops = workshopEventDAO.findUpcoming();

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
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public ResponseEntity<Void> join(@RequestBody WorkshopEvent workshop, Principal principal) {

		WorkshopEvent w = workshopEventDAO.findById(workshop.getIdWorkshopEvent());

		// Find SubGroup member
		SubGroupMember pm = subGroupMemberDAO.findBySubGroupAndFabber(w.getSubGroup().getIdSubGroup(),
				principal.getName());

		// Workshop member creation
		WorkshopEventTutor wm = new WorkshopEventTutor();

		wm.setSubGroupMember(pm);
		wm.setWorkshopEvent(w);
		workshopEventMemberDAO.makePersistent(wm);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/leave", method = RequestMethod.POST)
	public ResponseEntity<Void> leave(@RequestBody WorkshopEvent workshop, Principal principal) {

		WorkshopEvent w = workshopEventDAO.findById(workshop.getIdWorkshopEvent());

		// Find Workshop member
		WorkshopEventTutor wm = workshopEventMemberDAO.findByWorkshopAndFabber(w.getIdWorkshopEvent(), principal.getName());
		workshopEventMemberDAO.makeTransient(wm);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/delete-user", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> params, Principal principal) {

		WorkshopEvent workshop = workshopEventDAO.findById(Integer.parseInt(params.get("idWorkshop")));

		/*for (WorkshopEventTutor wm : workshop.getWorkshopEventTutors()) {
			if (wm.getSubGroupMember().getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				workshopEventMemberDAO.makeTransient(wm);
				break;
			}
		} */

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/name-coordinator", method = RequestMethod.POST)
	public ResponseEntity<Fabber> nameCoordinator(@RequestBody Map<String, String> params, Principal principal) {

		WorkshopEvent workshop = workshopEventDAO.findById(Integer.parseInt(params.get("idWorkshop")));
		Fabber fabber = null;

		/*for (WorkshopEventTutor wm : workshop.getWorkshopEventMembers()) {
			if (wm.getSubGroupMember().getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				fabber = wm.getSubGroupMember().getFabber();
				wm.setIsCoordinator(true);
				workshopEventMemberDAO.makePersistent(wm);
				break;
			}
		} */

		return new ResponseEntity<Fabber>(fabber, HttpStatus.OK);
	}

	@RequestMapping(value = "/get-current-user-status", method = RequestMethod.GET)
	public Map<String, Object> getCurrentUserStatus(@RequestParam(value = "idWorkshop") Integer idWorkshop,
			Principal principal) {

		WorkshopEvent ws = workshopEventDAO.findById(idWorkshop);

		Map<String, Object> model = new HashMap<String, Object>();
		Boolean isSubGroupMember = false;
		Boolean isWorkshopMember = false;
		Boolean isCoordinator = false;
		
		SubGroupMember sm = subGroupMemberDAO.findBySubGroupAndFabber(ws.getSubGroup().getIdSubGroup(), principal.getName());
		
		if (sm != null) {
			isSubGroupMember = true;
			
			/*for (WorkshopEventTutor wm : sm.getWorkshopEventMembers()) {
				if (wm.getWorkshopEvent().getIdWorkshopEvent().equals(ws.getIdWorkshopEvent())) {
					isWorkshopMember = true;
					isCoordinator = wm.getIsCoordinator();
					break;
				}
			} */
		}

		model.put("isSubGroupMember", isSubGroupMember);
		model.put("isWorkshopMember", isWorkshopMember);
		model.put("isCoordinator", isCoordinator);
		return model;
	}
}
