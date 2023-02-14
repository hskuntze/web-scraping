package br.com.kuntzeprojects.webscraping.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import br.com.kuntzeprojects.webscraping.entities.Equipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank
	private String nomeEquipe;
	
	//@NotBlank
	//private String urlLogoEquipe;
	
	public EquipeDTO(Equipe equipe) {
		this.id = equipe.getId();
		this.nomeEquipe = equipe.getNomeEquipe();
	}
}
