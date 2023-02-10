package br.com.kuntzeprojects.webscraping.util;

import java.io.IOException;
import java.util.List;

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
		String url = BASE_URL_GOOGLE + "flamengo+x+al+ahly" + COMPLEMENTO_URL_GOOGLE;
		
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
			
			String matchStatus = handleMatchStatus(getMatchStatus(doc));
			LOGGER.info("Status da partida: {}", matchStatus);
			
			String matchTime = getMatchTime(doc);
			if(!matchTime.equals("")) {
				LOGGER.info("Tempo da partida: {}", matchTime);
			}
			
			Integer totalMatches = getAllMatches(doc);
			if(!totalMatches.equals(0)) {
				LOGGER.info("Existem {} partidas registradas na página inicial", totalMatches);
			}
			
			List<String> names = getTeamNames(doc);
			if(!names.isEmpty()) {
				LOGGER.info("Time da casa: {}", names.get(0));
				LOGGER.info("Time visitante: {}", names.get(1));
			}
			
//			List<String> logos = getTeamLogos(doc);
//			for(String s : logos) {
//				LOGGER.info(s);
//			}
			
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
	
	public String getMatchTime(Document doc) {
		String matchTime = null;
		
		Boolean isMatchTime = doc.select("div[class*=imso_mh__lv-m-stts-cont]").isEmpty();
		if(!isMatchTime) {
			matchTime = doc.select("div[class*=imso_mh__lv-m-stts-cont]").first().text();
		}
		
		isMatchTime = doc.select("span[class*=imso_mh__ft-mtch]").isEmpty();
		if(!isMatchTime) {
			matchTime = doc.select("span[class*=imso_mh__ft-mtch]").first().text();
		}
		
		return fixMatchTime(matchTime);
	}
	
	public int getAllMatches(Document doc) {
		Boolean areThereMatches = doc.select("td[class*=liveresults-sports-immersive__match-tile]").isEmpty();
		Integer numOfMatchesFound = 0;
		
		if(!areThereMatches) {
			numOfMatchesFound = doc.select("td[class*=liveresults-sports-immersive__match-tile]").size();
		}
		
		return numOfMatchesFound;
	}
	
//	public String getHomeTeamName(Document doc) {
//		Element element = doc.selectFirst("div[class*=imso_mh__first-tn-ed]");
//		return element.select("span").text();
//	}
//	
//	public String getVisitorTeamName(Document doc) {
//		Element element = doc.selectFirst("div[class*=imso_mh__second-tn-ed]");
//		return element.select("span").text();
//	}
	
	public List<String> getTeamNames(Document doc) {
		return doc.select("div[class*=imso_mh__tnal-cont]").select("span").eachText();
	}
	
	public List<String> getTeamLogos(Document doc) {
		return doc.select("div[class*=imso_mh__tnal-cont]").select("img").eachAttr("src");
	}
	
//	public String test(Document doc) {
//		String src = doc.select("img[class*=imso_btl__mh-logo]").first().attr("src");
//		return src;
//	}
	
	private String fixMatchTime(String matchTime) {
		if(matchTime != null) {
			if(matchTime.contains("'")) {
				return matchTime.replace("'", "min");
			} else if (matchTime.contains("+")) {
				return matchTime.replace(" ", "").concat("min");
			} else {
				return matchTime;
			}
		} else {
			return "";
		}
	}
	
	private String handleMatchStatus(Status status) {
		String parsedStatus = "";
		switch(status) {
		    case NAO_INICIADO:
		    	parsedStatus = "Não iniciada";
		    	break;
		    case EM_ANDAMENTO:
		    	parsedStatus = "Em andamento";
		    	break;
		    case ENCERRADO:
		    	parsedStatus = "Encerrada";
		    	break;
		    case PENALIDADES:
		    	parsedStatus = "Pênaltis";
		    	break;
			default:
				break;
		}
		return parsedStatus;
	}
}
