package br.com.kuntzeprojects.webscraping.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.kuntzeprojects.webscraping.dto.EquipeDTO;
import br.com.kuntzeprojects.webscraping.entities.Equipe;
import br.com.kuntzeprojects.webscraping.exceptions.BadRequestException;
import br.com.kuntzeprojects.webscraping.exceptions.DatabaseException;
import br.com.kuntzeprojects.webscraping.exceptions.NotFoundException;
import br.com.kuntzeprojects.webscraping.repositories.EquipeRepository;

@Service
public class EquipeService {
	
	@Autowired
	private EquipeRepository repository;
	
	public Equipe findByNomeEquipe(String nomeEquipe) {
		Equipe obj = repository.findByNomeEquipe(nomeEquipe).orElseThrow(() -> new NotFoundException("No team with name "+nomeEquipe+" was found."));
		return obj;
	}

	public EquipeDTO findById(Long id) {
		return new EquipeDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("No team with id "+id+" was found.")));
	}
	
	public List<EquipeDTO> findAll() {
		if(repository.findAll().size() != 0) {
			return repository.findAll().stream().map(x -> new EquipeDTO(x)).collect(Collectors.toList());
		} else {
			throw new DatabaseException("Something is wrong. We didn't find any teams in our database.");
		}
	}

	public EquipeDTO save(EquipeDTO dto) {
		boolean exists = repository.existsByNomeEquipe(dto.getNomeEquipe());
		
		if(!exists) {
			Equipe obj = new Equipe();
			obj.setNomeEquipe(dto.getNomeEquipe());
			obj = repository.save(obj);
			return new EquipeDTO(obj);
		} else {
			throw new BadRequestException("Theres a team that already exists with that name.");
		}
	}
	
	public EquipeDTO update(Long id, EquipeDTO dto) {
		try {
			Equipe obj = repository.findById(id).get();
			obj.setNomeEquipe(dto.getNomeEquipe());
			obj = repository.save(obj);
			return new EquipeDTO(obj);
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
}
