package org.fablat.resource.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Project;
import org.fablat.resource.entities.ProjectMember;
import org.fablat.resource.entities.Group;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.ProjectDAO;
import org.fablat.resource.model.dao.ProjectMemberDAO;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.fablat.resource.util.ProjectDetailResource;
import org.fablat.resource.util.ProjectMemberResource;
import org.fablat.resource.util.ProjectRequestWrapper;
import org.fablat.resource.util.ProjectResource;
import org.fablat.resource.util.Resources;
import org.fablat.resource.util.View;
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
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private ProjectMemberDAO projectMemberDAO;
	@Autowired
	private WorkshopDAO workshopDAO;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<ProjectResource> save(@RequestBody ProjectRequestWrapper projectRequestWrapper,
			Principal principal) throws ParseException {

		Fabber fabber = fabberDAO.findByUsername(principal.getName());
		Group group = groupDAO.findById(projectRequestWrapper.getIdGroup());

		// Project creation
		Project project = new Project();
		project.setGroup(group);
		project.setName(projectRequestWrapper.getName());
		project.setDescription(projectRequestWrapper.getDescription());
		// Reunion Day (optional)
		project.setReunionDay(projectRequestWrapper.getReunionDay() != null ? projectRequestWrapper.getReunionDay()
				: null);
		// Reunion Time (optional)
		if (projectRequestWrapper.getReunionTime() != null) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			Date d1 = format.parse(projectRequestWrapper.getReunionTime());
			project.setReunionTime(d1);
		} else {
			project.setReunionTime(null);
		}
		// creation DateTime
		DateTime dt = new DateTime();
		DateTime dtLocal = dt.withZone(DateTimeZone.forID("America/Lima"));
		project.setCreationDateTime(dtLocal.toDate());

		project.setEnabled(true);

		// Project member creation
		ProjectMember pm = new ProjectMember();
		pm.setIsCoordinator(true);
		pm.setNotificationsEnabled(true);
		// creation DateTime
		pm.setCreationDateTime(dtLocal.toDate());
		pm.setFabber(fabber);

		pm.setProject(project);
		project.getProjectMembers().add(pm);

		Project p = projectDAO.makePersistent(project);

		ProjectResource gr = new ProjectResource(p.getIdProject(), p.getName(), p.getGroup().getName(), "JOINED",
				pm.getNotificationsEnabled(), p.getProjectMembers().size());

		return new ResponseEntity<ProjectResource>(gr, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestBody Project group, Principal principal) {

		Project p = projectDAO.findById(group.getIdProject());
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
			projectDAO.makeTransient(p);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/brief", method = RequestMethod.GET)
	public ResponseEntity<ProjectResource> brief(@RequestParam(value = "idProject") Integer idProject) {

		Project project = projectDAO.findById(idProject);

		ProjectResource pr = new ProjectResource();
		pr.setIdProject(project.getIdProject());
		pr.setName(project.getName());
		pr.setWorkshopsNumber(project.getWorkshops().size());

		return new ResponseEntity<ProjectResource>(pr, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<ProjectDetailResource> detail(@RequestParam(value = "idProject") Integer idProject,
			Principal principal) {

		Fabber fabber = fabberDAO.findByUsername(principal.getName());
		Project project = projectDAO.findById(idProject);

		ProjectDetailResource pdr = new ProjectDetailResource();
		pdr.setIdProject(project.getIdProject());
		pdr.setName(project.getName());
		pdr.setDescription(project.getDescription());
		pdr.setReunionDay(project.getReunionDay() != null ? project.getReunionDay() : null);

		DateFormat df = new SimpleDateFormat("HH:mm");
		pdr.setReunionTime(project.getReunionTime() != null ? df.format(project.getReunionTime()) : null);
		pdr.setLineName(project.getGroup().getName());
		pdr.setCurrentUserStatus("NOT_JOINED");

		for (ProjectMember pm : fabber.getProjectMembers()) {
			if (pm.getProject().getIdProject().equals(idProject)) {
				pdr.setCurrentUserStatus("JOINED");
				pdr.setNotificationsEnabled(pm.getNotificationsEnabled());
				break;
			}
		}

		pdr.setCoordinators(projectDAO.findCoordinators(idProject));
		pdr.setCollaborators(projectDAO.findCollaborators(idProject));

		return new ResponseEntity<ProjectDetailResource>(pdr, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-exclusive-user", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectResource>> listExclusiveUser(Principal principal) {

		List<ProjectResource> prs = new ArrayList<ProjectResource>();
		List<Project> projectsUser = projectDAO.findFabberProjects(principal.getName());

		// create the response list adding the user status related to each group
		for (Project px : projectsUser) {
			ProjectResource wr = new ProjectResource(px.getIdProject(), px.getName(), px.getGroup().getName(),
					"JOINED", null, px.getProjectMembers().size());

			prs.add(wr);
		}

		return new ResponseEntity<List<ProjectResource>>(prs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-all-user", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectResource>> listAllUser(Principal principal) {

		List<ProjectResource> grs = new ArrayList<ProjectResource>();

		List<Project> projectsAll = projectDAO.findAllOrderedAsc();
		List<Project> projectsUser = projectDAO.findFabberProjects(principal.getName());

		// create the response list adding the user status related to each group
		for (Project px : projectsAll) {
			ProjectResource pr = new ProjectResource(px.getIdProject(), px.getName(), px.getGroup().getName(),
					"NOT_JOINED", null, px.getProjectMembers().size());

			if (projectsUser.contains(px)) {
				pr.setCurrentUserStatus("JOINED");
			}

			grs.add(pr);
		}

		return new ResponseEntity<List<ProjectResource>>(grs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-managed-user", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectResource>> listManagedUser(Principal principal) {

		List<ProjectResource> prs = new ArrayList<ProjectResource>();
		Fabber fabber = fabberDAO.findByUsername(principal.getName());

		for (ProjectMember pm : fabber.getProjectMembers()) {
			if (pm.getIsCoordinator()) {
				ProjectResource pr = new ProjectResource(pm.getProject().getIdProject(), pm.getProject().getName(), pm
						.getProject().getGroup().getName(), "JOINED", pm.getNotificationsEnabled(), pm.getProject()
						.getProjectMembers().size());
				prs.add(pr);
			}
		}

		return new ResponseEntity<List<ProjectResource>>(prs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-not-managed-user", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectResource>> listNotManagedUser(Principal principal) {

		List<ProjectResource> prs = new ArrayList<ProjectResource>();
		Fabber fabber = fabberDAO.findByUsername(principal.getName());

		for (ProjectMember pm : fabber.getProjectMembers()) {
			if (!pm.getIsCoordinator()) {
				ProjectResource pr = new ProjectResource(pm.getProject().getIdProject(), pm.getProject().getName(), pm
						.getProject().getGroup().getName(), "JOINED", pm.getNotificationsEnabled(), pm.getProject()
						.getProjectMembers().size());
				prs.add(pr);
			}
		}

		return new ResponseEntity<List<ProjectResource>>(prs, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-all-admin", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectResource>> listAllAdmin() {

		List<ProjectResource> prs = new ArrayList<ProjectResource>();
		List<Project> projectsAll = projectDAO.findAllOrderedAsc();

		// create the response list adding the user status related to each group
		for (Project px : projectsAll) {
			ProjectResource pr = new ProjectResource(px.getIdProject(), px.getName(), px.getGroup().getName(),
					"NOT_JOINED", null, px.getProjectMembers().size());

			prs.add(pr);
		}

		return new ResponseEntity<List<ProjectResource>>(prs, HttpStatus.OK);
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public ResponseEntity<Void> join(@RequestBody Project project, Principal principal) {

		Fabber fabber = fabberDAO.findByUsername(principal.getName());
		Project p = projectDAO.findById(project.getIdProject());

		// Group member creation
		ProjectMember pm = new ProjectMember();
		pm.setIsCoordinator(false);
		pm.setNotificationsEnabled(true);
		// creation DateTime
		DateTime dt = new DateTime();
		DateTime dtLocal = dt.withZone(DateTimeZone.forID("America/Lima"));
		pm.setCreationDateTime(dtLocal.toDate());

		pm.setFabber(fabber);
		pm.setProject(p);
		projectMemberDAO.makePersistent(pm);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/list-coordinators", method = RequestMethod.GET)
	public ResponseEntity<List<Fabber>> listCoordinators(@RequestParam(value = "idProject") Integer idProject) {

		List<Fabber> fabbers = projectDAO.findCoordinators(idProject);

		return new ResponseEntity<List<Fabber>>(fabbers, HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/list-collaborators", method = RequestMethod.GET)
	public ResponseEntity<List<Fabber>> listCollaborators(@RequestParam(value = "idProject") Integer idProject) {

		List<Fabber> fabbers = projectDAO.findCollaborators(idProject);

		return new ResponseEntity<List<Fabber>>(fabbers, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-members-not-me", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectMemberResource>> listMembersNotMe(
			@RequestParam(value = "idProject") Integer idProject, Principal principal) {

		List<ProjectMemberResource> members = new ArrayList<ProjectMemberResource>();
		Project project = projectDAO.findById(idProject);

		for (ProjectMember pm : project.getProjectMembers()) {
			// not include current logged in user. Current user is assigned by
			// default as coordinator of
			// the replication, therefore its name should not be selectable for
			// avoiding redundancy
			if (pm.getFabber().getUsername().equals(principal.getName())) {
				continue;
			}

			ProjectMemberResource pmr = new ProjectMemberResource();
			pmr.setIdProjectMember(pm.getIdProjectMember());
			pmr.setFullName(pm.getFabber().getFirstName() + " " + pm.getFabber().getLastName());
			pmr.setEmail(pm.getFabber().getEmail());
			pmr.setImageUrl("img/avatar.png");

			members.add(pmr);
		}

		return new ResponseEntity<List<ProjectMemberResource>>(members, HttpStatus.OK);
	}

	@RequestMapping(value = "/list-upcoming-workshops", method = RequestMethod.GET)
	public ResponseEntity<List<WorkshopResource>> listUpcomingWorkshops(
			@RequestParam(value = "idProject") Integer idProject) {

		List<WorkshopResource> workshops = new ArrayList<WorkshopResource>();
		List<Workshop> upcomingWorkshops = workshopDAO.findUpcomingByProject(idProject);

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
	@RequestMapping(value = "/delete-user", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> params, Principal principal) {

		Project project = projectDAO.findById(Integer.parseInt(params.get("idProject")));

		for (ProjectMember pm : project.getProjectMembers()) {
			if (pm.getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				projectMemberDAO.makeTransient(pm);
				break;
			}
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/name-coordinator", method = RequestMethod.POST)
	public ResponseEntity<Fabber> nameCoordinator(@RequestBody Map<String, String> params, Principal principal) {

		Project project = projectDAO.findById(Integer.parseInt(params.get("idProject")));
		Fabber fabber = null;

		for (ProjectMember pm : project.getProjectMembers()) {
			if (pm.getFabber().getIdFabber().equals(Integer.parseInt(params.get("idFabber")))) {
				fabber = pm.getFabber();
				pm.setIsCoordinator(true);
				projectMemberDAO.makePersistent(pm);
				break;
			}
		}

		return new ResponseEntity<Fabber>(fabber, HttpStatus.OK);
	}

	@RequestMapping(value = "/get-current-user-status", method = RequestMethod.GET)
	public Map<String, Object> getCurrentUserStatus(@RequestParam(value = "idProject") Integer idProject,
			Principal principal) {

		Project project = projectDAO.findById(idProject);
		Fabber fabber = fabberDAO.findByUsername(principal.getName());

		Map<String, Object> model = new HashMap<String, Object>();
		Boolean isProjectMember = false;
		Boolean isCoordinator = false;

		for (ProjectMember pm : fabber.getProjectMembers()) {
			if (pm.getProject().getIdProject().equals(project.getIdProject())) {
				isProjectMember = true;
				isCoordinator = pm.getIsCoordinator();
				break;
			}
		}

		model.put("isProjectMember", isProjectMember);
		model.put("isCoordinator", isCoordinator);
		return model;
	}
}
