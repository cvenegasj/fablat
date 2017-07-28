package org.fablat.resource.controller;

import org.fablat.resource.dto.FabberDTO;
import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.RoleFabber;
import org.fablat.resource.exception.DuplicateEmailException;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.RoleFabberDAO;
import org.fablat.resource.util.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public")
public class FabberPublicController {

	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private LabDAO labDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private RoleFabberDAO roleFabberDAO;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public FabberDTO create(@RequestBody FabberDTO fabberDTO) {
		if (fabberDAO.findByEmail(fabberDTO.getEmail()) != null) {
			throw new DuplicateEmailException();
		}

		Fabber fabber = convertToEntity(fabberDTO);
		fabber.setEnabled(true);
		// TODO: encode password
		

		// set fabber's Lab
		if (fabberDTO.getLabId() != null) {
			fabber.setLab(labDAO.findById(fabberDTO.getLabId()));
			fabber.setIsNomade(false);
		} else {
			fabber.setLab(null);
			fabber.setIsNomade(true);
		}

		// assign ROLE_USER
		RoleFabber rf = new RoleFabber();
		rf.setRole(roleDAO.findByName(Resources.ROLE_USER));
		rf.setFabber(fabber);
		
		fabber.getRoleFabbers().add(rf);
		Fabber fabberCreated = fabberDAO.makePersistent(fabber);
		
		return convertToDTO(fabberCreated);
	}
	

	// ========== DTO conversion ==========
	
	private FabberDTO convertToDTO(Fabber fabber) {
		FabberDTO fabberDTO = new FabberDTO();
		fabberDTO.setIdFabber(fabber.getIdFabber());
		fabberDTO.setEmail(fabber.getEmail());
		fabberDTO.setFirstName(fabber.getFirstName());
		fabberDTO.setLastName(fabber.getLastName());
		fabberDTO.setIsFabAcademyGrad(fabber.getIsFabAcademyGrad());
		fabberDTO.setFabAcademyGradYear(fabber.getFabAcademyGradYear());
		fabberDTO.setCellPhoneNumber(fabber.getCellPhoneNumber());
		fabberDTO.setIsNomade(fabber.getIsNomade());
		fabberDTO.setMainQuote(fabber.getMainQuote());
		fabberDTO.setCity(fabber.getCity());
		fabberDTO.setCountry(fabber.getCountry());
		fabberDTO.setWeekGoal(fabber.getWeekGoal());
		fabberDTO.setAvatarUrl(fabber.getAvatarUrl());
		fabberDTO.setLabId(fabber.getLab() != null ? fabber.getLab().getIdLab() : null);
		fabberDTO.setLabName(fabber.getLab() != null ? fabber.getLab().getName() : null);
				
	    return fabberDTO;
	}

	private Fabber convertToEntity(FabberDTO fabberDTO) {
		Fabber fabber = new Fabber();
	    fabber.setFirstName(fabberDTO.getFirstName());
	    fabber.setLastName(fabberDTO.getLastName());
	    fabber.setIsFabAcademyGrad(fabberDTO.getIsFabAcademyGrad());
	    fabber.setFabAcademyGradYear(fabberDTO.getIsFabAcademyGrad() ? fabberDTO.getFabAcademyGradYear() : null);
		
		return fabber;
	}
	
}
