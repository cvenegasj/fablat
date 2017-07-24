package org.fablat.resource.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.fablat.resource.entities.Group;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.model.dao.GroupDAO;
import org.fablat.resource.serializable.SubGroupResource;
import org.fablat.resource.util.View;
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
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private GroupDAO groupDAO;

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<Group>> listManagedUser() {

		List<Group> list = groupDAO.findAll();

		return new ResponseEntity<List<Group>>(list, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<Void> save() {
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete() {
		

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@JsonView(View.Summary.class)
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<Void> detail(@RequestParam(value = "idGroup") Integer idGroup,
			Principal principal) {

		

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/list-exclusive-user", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listExclusiveUser(Principal principal) {

		
		return null;
	}

	@RequestMapping(value = "/list-all-user", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listAllUser(Principal principal) {
		

		return null;
	}

	@RequestMapping(value = "/list-all-admin", method = RequestMethod.GET)
	public ResponseEntity<List<SubGroupResource>> listAllAdmin() {

		
		return null;
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public ResponseEntity<Void> join(@RequestBody SubGroup subGroup, Principal principal) {

		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(value = "/delete-user", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> params, Principal principal) {


		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
