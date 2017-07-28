package org.fablat.resource.controller;

import java.security.Principal;
import java.util.Map;

import org.fablat.resource.dto.FabberDTO;
import org.fablat.resource.entities.Fabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.model.dao.WorkshopTutorDAO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth/fabbers")
public class FabberController {

	@Autowired
    private ModelMapper modelMapper;
	@Autowired
	private FabberDAO fabberDAO;
	@Autowired
	private LabDAO labDAO;
	@Autowired
	private SubGroupDAO subGroupDAO;
	@Autowired
	private GroupMemberDAO groupMemberDAO;
	@Autowired
	private SubGroupMemberDAO subGroupMemberDAO;
	@Autowired
	private WorkshopTutorDAO workshopTutorDAO;
	@Autowired
	private RoleDAO roleDAO;
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public FabberDTO me(Principal principal) {	
		// user logged with email as username
		FabberDTO fabberDTO = convertToDTO(fabberDAO.findByEmail(principal.getName()));
		Integer[] scores = calculateAndUpdateRankings(principal.getName());
		
		fabberDTO.setGeneralRanking(scores[3]);
		fabberDTO.setCoordinatorRanking(scores[2]);
		fabberDTO.setCollaboratorRanking(scores[1]);
		fabberDTO.setReplicatorRanking(scores[0]);
		
		return fabberDTO;
	}
	
	@RequestMapping(value = "/{idFabber}", method = RequestMethod.GET)
    public FabberDTO findOne(@PathVariable("idFabber") Integer idFabber) {
        return convertToDTO(fabberDAO.findById(idFabber));
    }
	
	@RequestMapping(value = "/me/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateMe(@RequestBody FabberDTO fabberDTO) {
        Fabber fabber = convertToEntity(fabberDTO);
        if (!fabberDTO.getIsFabAcademyGrad()) {
			fabber.setFabAcademyGradYear(null);
		}

		// dependencies
		if (fabberDTO.getLabId() != null) {
			fabber.setLab(labDAO.findById(fabberDTO.getLabId()));
			fabber.setIsNomade(false);
		} else {
			fabber.setLab(null);
			fabber.setIsNomade(true);
		}
        
        fabberDAO.makePersistent(fabber);
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	// TODO: store scores and calculate ranking position
	private Integer[] calculateAndUpdateRankings(String email) {
		Integer replicatorScore = workshopTutorDAO.countAllByFabber(email);
		Integer collaboratorScore = groupMemberDAO.countAllByFabberAsCollaborator(email) 
				+ subGroupMemberDAO.countAllByFabberAsCollaborator(email);
		Integer coordinatorScore = groupMemberDAO.countAllByFabberAsCoordinator(email)
				+ subGroupMemberDAO.countAllByFabberAsCoordinator(email);
		Integer generalScore = replicatorScore + collaboratorScore + coordinatorScore;
		
		return new Integer[]{replicatorScore, collaboratorScore, coordinatorScore, generalScore};
	}
	
	
	// ========== DTO conversion ==========
	// guide: http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
	private FabberDTO convertToDTO(Fabber fabber) {
		// TypeMap<Fabber, FabberDTO> typeMap = modelMapper.createTypeMap(Fabber.class, FabberDTO.class);
		modelMapper.typeMap(Fabber.class, FabberDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getLab().getIdLab(), FabberDTO::setLabId);
			mapper.map(src -> src.getLab().getName(), FabberDTO::setLabName);
		});
		
		FabberDTO fabberDTO = modelMapper.map(fabber, FabberDTO.class);
	    return fabberDTO;
	}
	
	private Fabber convertToEntity(FabberDTO fabberDTO) {
	    Fabber fabber = modelMapper.map(fabberDTO, Fabber.class);
	  
	    if (fabberDTO.getIdFabber() != null) {
	        Fabber oldFabber = fabberDAO.findById(fabberDTO.getIdFabber());
	        fabber.setEmail(oldFabber.getEmail());
	        fabber.setPassword(oldFabber.getPassword());
	        fabber.setEnabled(oldFabber.getEnabled());
	    }
	    return fabber;
	}
	
	

	
	
	
	
	
	

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<Void> update(@RequestBody Map<String, String> params, Principal principal) {

		// Fabber update
		Fabber fabber = fabberDAO.findByEmail(principal.getName());

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

}
