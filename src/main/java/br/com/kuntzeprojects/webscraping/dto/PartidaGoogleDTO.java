package br.com.kuntzeprojects.webscraping.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaGoogleDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String statusPartida;
	private String tempoPartida;
	
	//EQUIPE CASA
	private String nomeEquipeCasa;
	//private String urlLogoEquipeCasa;
	private String golsEquipeCasa;
	private Integer placarEquipeCasa;
	private Integer placarEstendidoEquipeCasa;
	
	//EQUIPE VISITANTE
	private String nomeEquipeVisitante;
	//private String urlLogoEquipeVisitante;
	private String golsEquipeVisitante;
	private Integer placarEquipeVisitante;
	private Integer placarEstendidoEquipeVisitante;
}
