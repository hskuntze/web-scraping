package br.com.kuntzeprojects.webscraping.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.kuntzeprojects.webscraping.dto.PartidaGoogleDTO;
import br.com.kuntzeprojects.webscraping.entities.Partida;
import br.com.kuntzeprojects.webscraping.util.ScrapingUtil;
import br.com.kuntzeprojects.webscraping.util.Status;

@Service
public class ScrapingService {

	@Autowired
	private ScrapingUtil scrapingUtil;

	@Autowired
	private PartidaService partidaService;

	public void checkMatchPeriod() {
		Integer qtd = partidaService.checkForMatchesInPeriod();

		if (qtd > 0) {
			List<Partida> partidas = partidaService.listMatchesInPeriod();

			partidas.forEach(partida -> {
				String urlPartida = scrapingUtil.assembleGoogleURL(partida.getEquipeCasa().getNomeEquipe(),
						partida.getEquipeVisitate().getNomeEquipe());
				
				PartidaGoogleDTO partidaGoogle = scrapingUtil.getMatchInfo(urlPartida);
				if(partidaGoogle.getStatusPartida() != Status.NAO_INICIADO)
					partidaService.updateMatchFromGoogle(partida, partidaGoogle);
			});
		}
	}
}
