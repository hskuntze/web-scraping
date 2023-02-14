package br.com.kuntzeprojects.webscraping.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.kuntzeprojects.webscraping.entities.Partida;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank
	private String nomeEquipeCasa;
	
	@NotBlank
	private String nomeEquipeVisitante;
	
	@NotBlank
	private String localPartida;
	
	@NotNull
	@ApiModelProperty(example = "dd/MM/yyyy HH:mm")
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
	private Date dataHoraPartida;

	public PartidaDTO(Partida obj) {
		this.id = obj.getId();
		this.nomeEquipeCasa = obj.getEquipeCasa().getNomeEquipe();
		this.nomeEquipeVisitante = obj.getEquipeVisitate().getNomeEquipe();
		this.localPartida = obj.getLocalPartida();
		this.dataHoraPartida = obj.getDataHoraPartida();
	}
}
