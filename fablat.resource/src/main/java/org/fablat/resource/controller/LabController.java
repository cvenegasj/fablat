package org.fablat.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fablat.resource.entities.Lab;
import org.fablat.resource.entities.Location;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.LocationDAO;
import org.fablat.resource.serializable.LabRequestWrapper;
import org.fablat.resource.serializable.LocationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab")
public class LabController {

	@Autowired
	private LabDAO labDAO;
	@Autowired
	private LocationDAO locationDAO;

	// General method for fetching data of all labs
	@RequestMapping(value = "/update-all", method = RequestMethod.POST)
	public ResponseEntity<Void> updateAll(@RequestBody Map<String, List<LabRequestWrapper>> labs) {

		for (LabRequestWrapper item : labs.get("labs")) {
			Lab lab = new Lab();
			lab.setIdLab(item.getId());
			lab.setName(item.getName());
			lab.setDescription(item.getDescription());
			lab.setAvatar(item.getAvatar());
			lab.setPhone(item.getPhone());
			lab.setEmail(item.getEmail());
			lab.setUrl(item.getUrl());

			Location location = new Location();
			location.setAddress1(item.getAddress_1());
			location.setAddress2(item.getAddress_2());
			location.setCity(item.getCity());
			location.setCountry(item.getCountry_code());
			location.setLatitude(item.getLatitude());
			location.setLongitude(item.getLongitude());

			locationDAO.makePersistent(location);
			lab.setLocation(location);
			labDAO.makePersistent(lab);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<LocationResource>> list() {

		List<LocationResource> results = new ArrayList<LocationResource>();
		List<Lab> labsAll = labDAO.findAll();

		for (Lab lab : labsAll) {
			LocationResource labResource = new LocationResource();
			labResource.setIdLab(lab.getIdLab());
			labResource.setName(lab.getName());
			labResource.setAddress1(lab.getLocation().getAddress1());
			labResource.setAddress2(lab.getLocation().getAddress2());
			labResource.setCity(lab.getLocation().getCity());
			labResource.setCountry(lab.getLocation().getCountry());

			results.add(labResource);
		}

		return new ResponseEntity<List<LocationResource>>(results, HttpStatus.OK);
	}

	@RequestMapping(value = "/find-by-term", method = RequestMethod.GET)
	public ResponseEntity<List<LocationResource>> findByTerm(@RequestParam(value = "term") String term) {

		List<LocationResource> results = new ArrayList<LocationResource>();
		List<Lab> labs = labDAO.findByTerm(term);

		for (Lab lab : labs) {
			LocationResource labResource = new LocationResource();
			labResource.setIdLab(lab.getIdLab());
			labResource.setName(lab.getName());
			labResource.setAddress1(lab.getLocation().getAddress1());
			labResource.setAddress2(lab.getLocation().getAddress2());
			labResource.setCity(lab.getLocation().getCity());
			labResource.setCountry(lab.getLocation().getCountry());

			results.add(labResource);
		}

		return new ResponseEntity<List<LocationResource>>(results, HttpStatus.OK);
	}
}
