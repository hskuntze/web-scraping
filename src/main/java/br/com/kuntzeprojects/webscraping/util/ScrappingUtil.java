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
		String url = BASE_URL_GOOGLE + "palmeiras+x+corinthians+08/08/2020" + COMPLEMENTO_URL_GOOGLE;
		
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
		} catch (IOException e) {
			LOGGER.error("[JSOUP] Erro na conexão");
			throw new JSoupConnectionException(e.getMessage());
		}
		
		return partida;
	}
}
