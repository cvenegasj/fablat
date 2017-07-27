package org.fablat.resource.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilController {

	@Autowired
	private FabberDAO fabberDAO;

	@RequestMapping(value = "/first", method = RequestMethod.GET)
	public Map<String, Object> first(@RequestParam(value = "idFabber") Integer idFabber, Principal principal) {

		Map<String, Object> model = new HashMap<String, Object>();

		// this must be the first hit to the database

		// check if the user already exists in the DB
		Fabber fabAux = fabberDAO.findById(idFabber);

		if (fabAux != null) {
			// update username in case it has changed
			// fabAux.setUsername(principal.getName());
			fabberDAO.makePersistent(fabAux);
			model.put("firstTime", "FALSE");
		} else {
			model.put("firstTime", "TRUE");
		}

		return model;
	}
}
