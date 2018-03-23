package org.fablat.resource.controller;

import java.util.ArrayList;
import java.util.List;

import org.fablat.resource.dto.LabDTO;
import org.fablat.resource.dto.LabRequestWrapper;
import org.fablat.resource.entities.Lab;
import org.fablat.resource.entities.Location;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.LocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth/labs")
public class LabController {

	@Autowired
	private LabDAO labDAO;
	@Autowired
	private LocationDAO locationDAO;

	// General method for fetching data of all labs
	@RequestMapping(value = "/update-all", method = RequestMethod.GET)
	public ResponseEntity<Void> updateAll() {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<List<LabRequestWrapper>> labsResponse = restTemplate.exchange("https://api.fablabs.io/v0/labs.json", HttpMethod.GET, 
				entity, new ParameterizedTypeReference<List<LabRequestWrapper>>() {});
		
		List<LabRequestWrapper> labs = labsResponse.getBody();
		
		System.out.println(labs);
		
		for (LabRequestWrapper item : labs) {
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
			location.setLatitude(item.getLatitude() != null ? item.getLatitude().toString() : null);
			location.setLongitude(item.getLongitude() != null ? item.getLongitude().toString() : null);

			locationDAO.makePersistent(location);
			lab.setLocation(location);
			labDAO.makePersistent(lab);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	
	@RequestMapping(method = RequestMethod.GET)
	public List<LabDTO> findAll() {
		List<LabDTO> returnList = new ArrayList<LabDTO>();

		for (Lab lab : labDAO.findAll()) {
			LabDTO lDTO = new LabDTO();
			lDTO.setIdLab(lab.getIdLab());
			lDTO.setName(lab.getName());
			lDTO.setAddress1(lab.getLocation().getAddress1());
			lDTO.setCity(lab.getLocation().getCity());
			lDTO.setCountry(lab.getLocation().getCountry());
			
			returnList.add(lDTO);
		}
		
		return returnList;
	} 

	@RequestMapping(value = "/search/{searchText}", method = RequestMethod.GET)
	public List<LabDTO> findByTerm(@PathVariable("searchText") String searchText) {
		List<LabDTO> returnList = new ArrayList<LabDTO>();
		for (Lab lab : labDAO.findByTerm(searchText)) {
			LabDTO lDTO = new LabDTO();
			lDTO.setIdLab(lab.getIdLab());
			lDTO.setName(lab.getName());
			lDTO.setAddress1(lab.getLocation().getAddress1());
			lDTO.setCity(lab.getLocation().getCity());
			lDTO.setCountry(lab.getLocation().getCountry());

			returnList.add(lDTO);
		}

		return returnList;
	}
	
}
