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
import org.fablat.resource.entities.Project;
import org.fablat.resource.entities.ProjectMember;
import org.fablat.resource.entities.Lab;
import org.fablat.resource.entities.Location;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.entities.WorkshopLocation;
import org.fablat.resource.entities.WorkshopMember;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.ProjectDAO;
import org.fablat.resource.model.dao.ProjectMemberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.fablat.resource.model.dao.WorkshopLocationDAO;
import org.fablat.resource.model.dao.WorkshopMemberDAO;
import org.fablat.resource.util.ProjectMemberResource;
import org.fablat.resource.util.LocationResource;
import org.fablat.resource.util.Resources;
import org.fablat.resource.util.View;
import org.fablat.resource.util.WorkshopDetailResource;
import org.fablat.resource.util.WorkshopRequestWrapper;
import org.fablat.resource.util.WorkshopResource;
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
public class WorkshopController {

	@Autowired
	private WorkshopDAO workshopDAO;
	@Autowired
	private WorkshopMemberDAO workshopMemberDAO;
	@Autowired
	private ProjectMemberDAO projectMemberDAO;
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private LabDAO labDAO;
	@Autowired
	private WorkshopLocationDAO workshopLocationDAO;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<WorkshopResource> save(@RequestBody WorkshopRequestWrapper workshopRequestWrapper,
			Principal principal) throws ParseException {

		Project project = projectDAO.findById(workshopRequestWrapper.getIdProject());

		Workshop workshop = new Workshop();
		workshop.setProject(project);
		workshop.setReplicationNumber(project.getWorkshops().size() + 1);

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
		WorkshopMember wmb = new WorkshopMember();
		wmb.setIsCoordinator(true);
		wmb.setProjectMember(projectMemberDAO.findByProjectAndFabber(workshopRequestWrapper.getIdProject(),
				principal.getName()));
		wmb.setWorkshop(workshop);
		workshop.getWorkshopMembers().add(wmb);

		// rest of members
		for (ProjectMemberResource pmr : workshopRequestWrapper.getCoordinators()) {
			ProjectMember pm = projectMemberDAO.findById(pmr.getIdProjectMember());
			WorkshopMember wm = new WorkshopMember();
			wm.setIsCoordinator(true);
			wm.setProjectMember(pm);
			wm.setWorkshop(workshop);
			workshop.getWorkshopMembers().add(wm);
		}

		for (ProjectMemberResource pmr : workshopRequestWrapper.getCollaborators()) {
			ProjectMember pm = projectMemberDAO.findById(pmr.getIdProjectMember());
			WorkshopMember wm = new WorkshopMember();
			wm.setIsCoordinator(false);
			wm.setProjectMember(pm);
			wm.setWorkshop(workshop);
			workshop.getWorkshopMembers().add(wm);
		}

		workshop = workshopDAO.makePersistent(workshop);

		for (LocationResource lr : workshopRequestWrapper.getLocations()) {

			WorkshopLocation wl = new WorkshopLocation();
			wl.setWorkshop(workshop);

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
		}

		WorkshopResource wr = new WorkshopResource();
		wr.setIdWorkshop(workshop.getIdWorkshop());
		wr.setReplicationNumber(workshop.getReplicationNumber());
		SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
		wr.setDateTime(format3.format(workshop.getDateTime()));
		wr.setIsPaid(workshop.getIsPaid());
		wr.setPrice(workshop.getPrice());
		wr.setCurrency(workshop.getCurrency());
		wr.setMembersCount(workshop.getWorkshopMembers().size());
		wr.setProjectName(workshop.getProject().getName());

		return new ResponseEntity<WorkshopResource>(wr, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestBody Workshop workshop, Principal principal) {

		Workshop w = workshopDAO.findById(workshop.getIdWorkshop());
		Fabber f = fabberDAO.findByUsername(principal.getName());

		boolean isLatAdmin = false;

		for (RoleFabber rf : f.getRoleFabbers()) {
			if (rf.getRole().getName().equals(Resources.ROLE_ADMIN_LAT)) {
				isLatAdmin = true;
				break;
			}
		}

		// action only allowed for ROLE_ADMIN_LAT
		if (isLatAdmin) {
			workshopDAO.makeTransient(w);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcoming() {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<Workshop> upcomingWorkshops = workshopDAO.findUpcoming();

		for (Workshop workshop : upcomingWorkshops) {
			WorkshopResource wr = new WorkshopResource();
			wr.setIdWorkshop(workshop.getIdWorkshop());
			wr.setReplicationNumber(workshop.getReplicationNumber());
			SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
			wr.setDateTime(format3.format(workshop.getDateTime()));
			wr.setIsPaid(workshop.getIsPaid());
			wr.setPrice(workshop.getPrice());
			wr.setCurrency(workshop.getCurrency());
			wr.setMembersCount(workshop.getWorkshopMembers().size());
			wr.setProjectName(workshop.getProject().getName());

			workshops.add(wr);
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<WorkshopDetailResource> detail(@RequestParam(value = "idWorkshop") Integer idWorkshop,
			Principal principal) {

		Workshop workshop = workshopDAO.findById(idWorkshop);

		WorkshopDetailResource wdr = new WorkshopDetailResource();
		wdr.setIdWorkshop(workshop.getIdWorkshop());
		wdr.setReplicationNumber(workshop.getReplicationNumber());

		SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
		wdr.setDateTime(format3.format(workshop.getDateTime()));

		wdr.setIsPaid(workshop.getIsPaid());
		wdr.setPrice(workshop.getPrice());
		wdr.setCurrency(workshop.getCurrency());
		wdr.setProjectName(workshop.getProject().getName());

		// check if current user is member of workshop
		Fabber fabber = fabberDAO.findByUsername(principal.getName());
		List<Fabber> coordinators = workshopDAO.findCoordinators(idWorkshop);
		List<Fabber> collaborators = workshopDAO.findCollaborators(idWorkshop);

		wdr.setCurrentUserStatus("NOT_JOINED");

		if (coordinators.contains(fabber) || collaborators.contains(fabber)) {
			wdr.setCurrentUserStatus("JOINED");
		}

		wdr.setCoordinators(coordinators);
		wdr.setCollaborators(collaborators);

		for (WorkshopLocation wl : workshop.getWorkshopLocations()) {
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
		}

		return new ResponseEntity<WorkshopDetailResource>(wdr, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming-user", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcomingUser(Principal principal) {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<Workshop> upcomingWorkshops = workshopDAO.findUpcomingByUser(principal.getName());

		for (Workshop workshop : upcomingWorkshops) {
			WorkshopResource wr = new WorkshopResource();
			wr.setIdWorkshop(workshop.getIdWorkshop());
			wr.setReplicationNumber(workshop.getReplicationNumber());
			SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
			wr.setDateTime(format3.format(workshop.getDateTime()));
			wr.setIsPaid(workshop.getIsPaid());
			wr.setPrice(workshop.getPrice());
			wr.setCurrency(workshop.getCurrency());
			wr.setMembersCount(workshop.getWorkshopMembers().size());
			wr.setProjectName(workshop.getProject().getName());

			workshops.add(wr);
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-past-user", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listPastUser(Principal principal) {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<Workshop> pastWorkshops = workshopDAO.findPastByUser(principal.getName());

		for (Workshop workshop : pastWorkshops) {
			WorkshopResource wr = new WorkshopResource();
			wr.setIdWorkshop(workshop.getIdWorkshop());
			wr.setReplicationNumber(workshop.getReplicationNumber());
			SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
			wr.setDateTime(format3.format(workshop.getDateTime()));
			wr.setIsPaid(workshop.getIsPaid());
			wr.setPrice(workshop.getPrice());
			wr.setCurrency(workshop.getCurrency());
			wr.setMembersCount(workshop.getWorkshopMembers().size());
			wr.setProjectName(workshop.getProject().getName());

			workshops.add(wr);
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming-admin", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcomingAdmin() {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<Workshop> upcomingWorkshops = workshopDAO.findUpcoming();

		for (Workshop workshop : upcomingWorkshops) {
			WorkshopResource wr = new WorkshopResource();
			wr.setIdWorkshop(workshop.getIdWorkshop());
			wr.setReplicationNumber(workshop.getReplicationNumber());
			SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
			wr.setDateTime(format3.format(workshop.getDateTime()));
			wr.setIsPaid(workshop.getIsPaid());
			wr.setPrice(workshop.getPrice());
			wr.setCurrency(workshop.getCurrency());
			wr.setMembersCount(workshop.getWorkshopMembers().size());
			wr.setProjectName(workshop.getProject().getName());

			workshops.add(wr);
		}

		return new ResponseEntity<List<WorkshopResource>>(workshops, HttpStatus.OK);
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public ResponseEntity<Void> join(@RequestBody Workshop workshop, Principal principal) {

		Workshop w = workshopDAO.findById(workshop.getIdWorkshop());

		// Find Project member
		ProjectMember pm = projectMemberDAO.findByProjectAndFabber(w.getProject().getIdProject(), principal.getName());

		// Workshop member creation
		WorkshopMember wm = new WorkshopMember();
		wm.setIsCoordinator(false);

		wm.setProjectMember(pm);
		wm.setWorkshop(w);
		workshopMemberDAO.makePersistent(wm);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/leave", method = RequestMethod.POST)
	public ResponseEntity<Void> leave(@RequestBody Workshop workshop, Principal principal) {

		Workshop w = workshopDAO.findById(workshop.getIdWorkshop());

		// Find Workshop member
		WorkshopMember wm = workshopMemberDAO.findByWorkshopAndFabber(w.getIdWorkshop(), principal.getName());
		workshopMemberDAO.makeTransient(wm);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/delete-user", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> params, Principal principal) {

		Workshop workshop = workshopDAO.findById(Integer.parseInt(params.get("idWorkshop")));

		for (WorkshopMember wm : workshop.getWorkshopMembers()) {
			if (wm.getProjectMember().getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				workshopMemberDAO.makeTransient(wm);
				break;
			}
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/name-coordinator", method = RequestMethod.POST)
	public ResponseEntity<Fabber> nameCoordinator(@RequestBody Map<String, String> params, Principal principal) {

		Workshop workshop = workshopDAO.findById(Integer.parseInt(params.get("idWorkshop")));
		Fabber fabber = null;

		for (WorkshopMember wm : workshop.getWorkshopMembers()) {
			if (wm.getProjectMember().getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				fabber = wm.getProjectMember().getFabber();
				wm.setIsCoordinator(true);
				workshopMemberDAO.makePersistent(wm);
				break;
			}
		}

		return new ResponseEntity<Fabber>(fabber, HttpStatus.OK);
	}

	@RequestMapping(value = "/get-current-user-status", method = RequestMethod.GET)
	public Map<String, Object> getCurrentUserStatus(@RequestParam(value = "idWorkshop") Integer idWorkshop,
			Principal principal) {

		Workshop ws = workshopDAO.findById(idWorkshop);
		Fabber fabber = fabberDAO.findByUsername(principal.getName());

		Map<String, Object> model = new HashMap<String, Object>();
		Boolean isProjectMember = false;
		Boolean isWorkshopMember = false;
		Boolean isCoordinator = false;

		for (ProjectMember pm : fabber.getProjectMembers()) {
			if (pm.getProject().getIdProject().equals(ws.getProject().getIdProject())) {
				isProjectMember = true;
				for (WorkshopMember wm : pm.getWorkshopMembers()) {
					if (wm.getWorkshop().getIdWorkshop().equals(ws.getIdWorkshop())) {
						isWorkshopMember = true;
						isCoordinator = wm.getIsCoordinator();
					}
				}
				break;
			}
		}

		model.put("isProjectMember", isProjectMember);
		model.put("isWorkshopMember", isWorkshopMember);
		model.put("isCoordinator", isCoordinator);
		return model;
	}
}
