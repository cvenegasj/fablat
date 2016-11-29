package org.fablat.resource.controller;

import java.util.List;

import org.fablat.resource.entities.Group;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private GroupDAO groupDAO;

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<Group>> listManagedUser() {

		List<Group> groups = groupDAO.findAll();

		return new ResponseEntity<List<Group>>(groups, HttpStatus.OK);
	}
}
