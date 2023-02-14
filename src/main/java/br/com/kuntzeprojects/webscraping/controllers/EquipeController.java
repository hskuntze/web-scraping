package br.com.kuntzeprojects.webscraping.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzeprojects.webscraping.dto.EquipeDTO;
import br.com.kuntzeprojects.webscraping.exceptions.StandardError;
import br.com.kuntzeprojects.webscraping.services.EquipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Teams API")
@RestController
@RequestMapping(value = "/equipes")
public class EquipeController {
	
	@Autowired
	private EquipeService service;
	
	@ApiOperation(value = "Buscar por todas as equipes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = EquipeDTO.class),
			@ApiResponse(code = 400, message = "BAD REQUEST", response = StandardError.class),
			@ApiResponse(code = 401, message = "UNAUTHORIZED", response = StandardError.class),
			@ApiResponse(code = 403, message = "FORBIDDEN", response = StandardError.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = StandardError.class),
			@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = StandardError.class)

	})
	@GetMapping
	public ResponseEntity<List<EquipeDTO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@ApiOperation(value = "Buscar equipe por id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = EquipeDTO.class),
			@ApiResponse(code = 400, message = "BAD REQUEST", response = StandardError.class),
			@ApiResponse(code = 401, message = "UNAUTHORIZED", response = StandardError.class),
			@ApiResponse(code = 403, message = "FORBIDDEN", response = StandardError.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = StandardError.class),
			@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = StandardError.class)

	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<EquipeDTO> getEquipeId(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}
	
	@ApiOperation(value = "Inserir equipe")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "CREATED", response = EquipeDTO.class),
			@ApiResponse(code = 400, message = "BAD REQUEST", response = StandardError.class),
			@ApiResponse(code = 401, message = "UNAUTHORIZED", response = StandardError.class),
			@ApiResponse(code = 403, message = "FORBIDDEN", response = StandardError.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = StandardError.class),
			@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = StandardError.class)

	})
	@PostMapping
	public ResponseEntity<EquipeDTO> insert(@Valid @RequestBody EquipeDTO dto) {
		dto = service.save(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@ApiOperation(value = "Atualizar equipe")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = EquipeDTO.class),
			@ApiResponse(code = 400, message = "BAD REQUEST", response = StandardError.class),
			@ApiResponse(code = 401, message = "UNAUTHORIZED", response = StandardError.class),
			@ApiResponse(code = 403, message = "FORBIDDEN", response = StandardError.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = StandardError.class),
			@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = StandardError.class)

	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<EquipeDTO> update(@PathVariable Long id, @RequestBody EquipeDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@ApiOperation(value = "Deletar uma equipe por id")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "NO CONTENT", response = Void.class),
			@ApiResponse(code = 400, message = "BAD REQUEST", response = StandardError.class),
			@ApiResponse(code = 401, message = "UNAUTHORIZED", response = StandardError.class),
			@ApiResponse(code = 403, message = "FORBIDDEN", response = StandardError.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = StandardError.class),
			@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = StandardError.class)

	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
