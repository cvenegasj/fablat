package org.fablat.resource.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Lab;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.RoleFabberDAO;
import org.fablat.resource.util.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/public")
public class PublicController {

	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private LabDAO labDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private RoleFabberDAO roleFabberDAO;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Map<String, Object> home() {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");

		return model;
	}

	//@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Void> signup(@RequestBody Map<String, String> params) {

		// Fabber creation
		Fabber fabber = new Fabber();
		fabber.setEmail(params.get("email"));
		fabber.setPassword(params.get("password"));

		fabber.setFirstName(params.get("firstName"));
		fabber.setLastName(params.get("lastName"));
		fabber.setEnabled(true);

		fabber.setIsFabAcademyGrad(Boolean.parseBoolean(params.get("isFabAcademyGrad")));
		if (fabber.getIsFabAcademyGrad()) {
			fabber.setFabAcademyGradYear(Integer.parseInt(params.get("fabAcademyGradYear")));
		} else {
			fabber.setFabAcademyGradYear(null);
		}

		// Lab creation
		if (params.get("idLab") == null) {
			fabber.setLab(null);
			fabber.setIsNomade(true);
		} else {
			Lab lab = labDAO.findById(Integer.parseInt(params.get("idLab")));
			fabber.setLab(lab);
			fabber.setIsNomade(false);
		}

		fabberDAO.makePersistent(fabber);

		// Role for user
		RoleFabber rf = new RoleFabber();
		rf.setRole(roleDAO.findByName(Resources.ROLE_USER));
		rf.setFabber(fabber);
		roleFabberDAO.makePersistent(rf);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/signup-nomade", method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody Map<String, String> params, Principal principal) {

		// Fabber creation
		Fabber fabber = new Fabber();
		fabber.setEmail(params.get("email"));
		fabber.setPassword(params.get("password"));

		fabber.setFirstName(params.get("firstName"));
		fabber.setLastName(params.get("lastName"));
		fabber.setEnabled(true);

		fabber.setIsFabAcademyGrad(Boolean.parseBoolean(params.get("isFabAcademyGrad")));
		if (fabber.getIsFabAcademyGrad()) {
			fabber.setFabAcademyGradYear(Integer.parseInt(params.get("fabAcademyGradYear")));
		} else {
			fabber.setFabAcademyGradYear(null);
		}

		// Lab nomade
		fabber.setLab(null);
		fabber.setIsNomade(true);

		fabberDAO.makePersistent(fabber);

		// Role for user
		RoleFabber rf = new RoleFabber();
		rf.setRole(roleDAO.findByName(Resources.ROLE_USER));
		rf.setFabber(fabber);
		roleFabberDAO.makePersistent(rf);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
