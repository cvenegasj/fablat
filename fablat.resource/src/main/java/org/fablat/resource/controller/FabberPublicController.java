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
import org.modelmapper.ModelMapper;
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
	private ModelMapper modelMapper;
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

		if (!fabberDTO.getIsFabAcademyGrad()) {
			fabber.setFabAcademyGradYear(null);
		}

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
		modelMapper.typeMap(Fabber.class, FabberDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getLab().getIdLab(), FabberDTO::setLabId);
			mapper.map(src -> src.getLab().getName(), FabberDTO::setLabName);
		});

		FabberDTO fabberDTO = modelMapper.map(fabber, FabberDTO.class);
		return fabberDTO;
	}

	private Fabber convertToEntity(FabberDTO fabberDTO) {
		Fabber fabber = modelMapper.map(fabberDTO, Fabber.class);
		
		return fabber;
	}
}
