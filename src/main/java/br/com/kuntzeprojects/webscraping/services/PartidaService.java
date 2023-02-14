package br.com.kuntzeprojects.webscraping.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.kuntzeprojects.webscraping.dto.PartidaDTO;
import br.com.kuntzeprojects.webscraping.dto.PartidaGoogleDTO;
import br.com.kuntzeprojects.webscraping.entities.Partida;
import br.com.kuntzeprojects.webscraping.exceptions.DatabaseException;
import br.com.kuntzeprojects.webscraping.exceptions.NotFoundException;
import br.com.kuntzeprojects.webscraping.repositories.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository repository;
	
	@Autowired
	private EquipeService equipeService;

	public List<PartidaDTO> findAll() {
		if(repository.findAll().size() != 0) {
			return repository.findAll().stream().map(x -> new PartidaDTO(x)).collect(Collectors.toList());
		} else {
			throw new DatabaseException("Something is wrong. We didn't find any teams in our database.");
		}
	}

	public PartidaDTO findById(Long id) {
		return new PartidaDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("No team with id "+id+" was found.")));
	}

	public PartidaDTO save(PartidaDTO dto) {
		Partida obj = new Partida();
		dtoToEntity(dto, obj);
		obj = repository.save(obj);
		return new PartidaDTO(obj);
	}

	public PartidaDTO update(Long id, PartidaDTO dto) {
		boolean exists = repository.existsById(id);
		if(!exists) {
			throw new NotFoundException("A match with id "+id+" was not found.");
		}
		
		try {
			Partida obj = repository.findById(id).get();
			dtoToEntity(dto, obj);
			obj = repository.save(obj);
			return new PartidaDTO(obj);
		} catch (EntityNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new NotFoundException(e.getMessage());
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	private void dtoToEntity(PartidaDTO dto, Partida entity) {
		entity.setDataHoraPartida(dto.getDataHoraPartida());
		entity.setEquipeCasa(equipeService.findByNomeEquipe(dto.getNomeEquipeCasa()));
		entity.setEquipeVisitate(equipeService.findByNomeEquipe(dto.getNomeEquipeVisitante()));
		entity.setLocalPartida(dto.getLocalPartida());
	}

	public Integer checkForMatchesInPeriod() {
		return repository.checkForMatchesInPeriod();
	}

	public List<Partida> listMatchesInPeriod() {
		return repository.listMatchesInPeriod();
	}

	public void updateMatchFromGoogle(Partida partida, PartidaGoogleDTO partidaGoogle) {
		partida.setGolsEquipeCasa(partidaGoogle.getGolsEquipeCasa());
		partida.setGolsEquipeVisitante(partidaGoogle.getGolsEquipeVisitante());
		partida.setPlacarEquipeCasa(partidaGoogle.getPlacarEquipeCasa());
		partida.setPlacarEquipeVisitante(partidaGoogle.getPlacarEquipeVisitante());
		partida.setPlacarEstendidoEquipeCasa(partidaGoogle.getPlacarEstendidoEquipeCasa());
		partida.setPlacarEstendidoEquipeVisitante(partidaGoogle.getPlacarEstendidoEquipeVisitante());
		partida.setStatusPartida(partida.getStatusPartida());
		partida.setTempoPartida(partida.getTempoPartida());
		
		repository.save(partida);
	}
}
