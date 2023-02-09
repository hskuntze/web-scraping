package br.com.kuntzeprojects.webscraping.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.kuntzeprojects.webscraping.dto.PartidaGoogleDTO;
import br.com.kuntzeprojects.webscraping.exceptions.JSoupConnectionException;

public class ScrappingUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrappingUtil.class);
	
	private static final String BASE_URL_GOOGLE = "https://www.google.com/search?q=";
	private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";
	
	public static void main(String[] args) {
		String url = BASE_URL_GOOGLE + "flamengo+x+al-hilal" + COMPLEMENTO_URL_GOOGLE;
		
		ScrappingUtil scrapping = new ScrappingUtil();
		scrapping.getMatchInfo(url);
	}
	
	public PartidaGoogleDTO getMatchInfo(String url) {
		PartidaGoogleDTO partida = new PartidaGoogleDTO();
		
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			
			String title = doc.title();
			LOGGER.info("Título da página: {}", title);
			
			Status matchStatus = getMatchStatus(doc);
			LOGGER.info("STATUS DA PARTIDA: {}", matchStatus);
			
			//getAllMatches(doc);
		} catch (IOException e) {
			LOGGER.error("[JSOUP] Erro na conexão");
			throw new JSoupConnectionException(e.getMessage());
		}
		
		return partida;
	}
	
	public Status getMatchStatus(Document doc) {
		Status matchStatus = Status.NAO_INICIADO;
		
		Boolean isMatchTime = doc.select("span[class*=imso_mh__ft-mtch]").isEmpty();
		
		//ENCERRADO
		if(!isMatchTime) {
			String status = doc.select("span[class*=imso_mh__ft-mtch]").first().text();

			if(status.contains("Encerrado")) {
				matchStatus = Status.ENCERRADO;
			}
		}
		
		//EM ANDAMENTO OU EM PÊNALTIS
		isMatchTime = doc.select("div[class*=imso_mh__lv-m-stts-cont]").isEmpty();
		
		if(!isMatchTime) {
			String status = doc.select("div[class*=imso_mh__lv-m-stts-cont]").first().text();
			matchStatus = Status.EM_ANDAMENTO;
			
			if(status.contains("Pênaltis")) {
				matchStatus = Status.PENALIDADES;
			}
		}
		
		return matchStatus;
	}
	
	public void getAllMatches(Document doc) {
		Boolean areThereMatches = doc.select("td[class*=liveresults-sports-immersive__match-tile]").isEmpty();
		
		if(!areThereMatches) {
			Integer numOfMatchesFound = doc.select("td[class*=liveresults-sports-immersive__match-tile]").size();
			LOGGER.info("There are! Total of: {}", numOfMatchesFound);
		}
	}
}
