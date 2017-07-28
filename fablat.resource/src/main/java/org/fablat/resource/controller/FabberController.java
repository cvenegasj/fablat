package org.fablat.resource.controller;

import java.security.Principal;

import org.fablat.resource.dto.FabberDTO;
import org.fablat.resource.entities.Fabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.fablat.resource.model.dao.LabDAO;
import org.fablat.resource.model.dao.RoleDAO;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.fablat.resource.model.dao.WorkshopTutorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	    fabber.setCellPhoneNumber(fabberDTO.getCellPhoneNumber());
	    fabber.setIsNomade(fabberDTO.getIsNomade());
	    fabber.setMainQuote(fabberDTO.getMainQuote());
	    fabber.setCity(fabberDTO.getCity());
	    fabber.setCountry(fabberDTO.getCountry());
	    fabber.setWeekGoal(fabberDTO.getAvatarUrl());
	    
	    return fabber;
	}

}
