package org.fablat.resource.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Lab;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.serializable.FabberResource;
import org.fablat.resource.serializable.FabberStatsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fabber")
public class FabberController {

	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private LabDAO labDAO;
	@Autowired
	private SubGroupDAO subGroupDAO;

	// For getting the principal values needed in the UI
	@RequestMapping(value = "/user-diggested", method = RequestMethod.GET)
	public Map<String, Object> userDiggested(Principal user) {

		Fabber fabber = fabberDAO.findByUsername(user.getName());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", fabber.getIdFabber());
		model.put("username", fabber.getUsername());
		model.put("email", fabber.getEmail());
		model.put("firstName", fabber.getFirstName());
		model.put("lastName", fabber.getLastName());
		model.put("idLab", fabber.getLab() != null ? fabber.getLab().getIdLab() : null);

		Set<String> roles = new HashSet<String>();

		for (RoleFabber rf : fabber.getRoleFabbers()) {
			roles.add(rf.getRole().getName());
		}

		model.put("roles", roles);

		return model;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody Map<String, String> params, Principal principal) {

		// Fabber creation
		Fabber fabber = new Fabber();
		fabber.setIdFabber(Long.parseLong(params.get("idFabber")));
		fabber.setUsername(principal.getName());

		fabber.setEmail(params.get("email"));
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

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<Void> update(@RequestBody Map<String, String> params, Principal principal) {

		// Fabber update
		Fabber fabber = fabberDAO.findByUsername(principal.getName());

		// fabber.setEmail(params.get("email"));
		fabber.setFirstName(params.get("firstName"));
		fabber.setLastName(params.get("lastName"));

		fabber.setIsFabAcademyGrad(Boolean.parseBoolean(params.get("isFabAcademyGrad")));

		if (fabber.getIsFabAcademyGrad()) {
			fabber.setFabAcademyGradYear(Integer.parseInt(params.get("fabAcademyGradYear")));
		} else {
			fabber.setFabAcademyGradYear(null);
		}

		// Lab creation
		// if (params.get("idLab") == null) {
		// fabber.setLab(null);
		// fabber.setIsNomade(true);
		// } else {
		// Lab lab = labDAO.findById(Integer.parseInt(params.get("idLab")));
		// fabber.setLab(lab);
		// fabber.setIsNomade(false);
		// }

		fabberDAO.makePersistent(fabber);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public ResponseEntity<FabberResource> current(Principal principal) {

		Fabber fabber = fabberDAO.findByUsername(principal.getName());

		FabberResource fResource = new FabberResource();
		fResource.setIdFabber(fabber.getIdFabber());
		fResource.setUsername(fabber.getUsername());
		fResource.setEmail(fabber.getEmail());
		fResource.setFirstName(fabber.getFirstName());
		fResource.setLastName(fabber.getLastName());
		fResource.setIsFabAcademyGrad(fabber.getIsFabAcademyGrad());
		fResource.setFabAcademyGradYear(fabber.getFabAcademyGradYear());
		fResource.setLabId(fabber.getLab() != null ? fabber.getLab().getIdLab() : null);
		fResource.setLabName(fabber.getLab() != null ? fabber.getLab().getName() : null);
		fResource.setCellPhoneNumber(fabber.getCellPhoneNumber());
		fResource.setIsNomade(fabber.getIsNomade());
		fResource.setAvatar(fabber.getAvatarUrl());

		return new ResponseEntity<FabberResource>(fResource, HttpStatus.OK);
	}

	@RequestMapping(value = "/current-stats", method = RequestMethod.GET)
	public ResponseEntity<FabberStatsResource> currentStats(Principal principal) {
		
		Integer nSubGroupsCoordinator = subGroupDAO.findManagedSubGroups(principal.getName()).size();
		Integer nSubGroupsCollaborator = subGroupDAO.findNotManagedSubGroups(principal.getName()).size();

		FabberStatsResource fsr = new FabberStatsResource();
		fsr.setnSubGroupsCoordinator(nSubGroupsCoordinator);
		fsr.setnSubGroupsCollaborator(nSubGroupsCollaborator);

		return new ResponseEntity<FabberStatsResource>(fsr, HttpStatus.OK);
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<FabberResource> detail(@RequestParam(value = "idFabber") Long idFabber) {

		Fabber fabber = fabberDAO.findById(idFabber);

		FabberResource fr = new FabberResource();
		fr.setIdFabber(fabber.getIdFabber());
		fr.setUsername(fabber.getUsername());
		fr.setEmail(fabber.getEmail());
		fr.setFirstName(fabber.getFirstName());
		fr.setLastName(fabber.getLastName());
		fr.setIsFabAcademyGrad(fabber.getIsFabAcademyGrad());
		fr.setFabAcademyGradYear(fabber.getFabAcademyGradYear());
		fr.setLabId(fabber.getLab() != null ? fabber.getLab().getIdLab() : null);
		fr.setLabName(fabber.getLab() != null ? fabber.getLab().getName() : null);
		fr.setCellPhoneNumber(fabber.getCellPhoneNumber());
		fr.setIsNomade(fabber.getIsNomade());
		fr.setAvatar(fabber.getAvatarUrl());

		return new ResponseEntity<FabberResource>(fr, HttpStatus.OK);
	}

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public ResponseEntity<FabberStatsResource> stats(@RequestParam(value = "idFabber") Long idFabber) {
		
		Integer nSubGroupsCoordinator = subGroupDAO.findManagedSubGroups(idFabber).size();
		Integer nSubGroupsCollaborator = subGroupDAO.findNotManagedSubGroups(idFabber).size();

		FabberStatsResource fsr = new FabberStatsResource();
		fsr.setnSubGroupsCoordinator(nSubGroupsCoordinator);
		fsr.setnSubGroupsCollaborator(nSubGroupsCollaborator);

		return new ResponseEntity<FabberStatsResource>(fsr, HttpStatus.OK);
	}
}
