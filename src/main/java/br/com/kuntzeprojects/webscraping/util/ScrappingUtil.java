package br.com.kuntzeprojects.webscraping.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.kuntzeprojects.webscraping.dto.PartidaGoogleDTO;
import br.com.kuntzeprojects.webscraping.exceptions.JSoupConnectionException;

public class ScrappingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrappingUtil.class);

	private static final String BASE_URL_GOOGLE = "https://www.google.com/search?q=";
	private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";

	private static final String CASA = "casa";
	private static final String VISITANTE = "visitante";

	private static final String DIV_PENALTIS = "div[class=imso_mh_s__psn-sc]";
	private static final String DIV_MAIN = "div[class*=imso_mh__tnal-cont]";
	private static final String DIV_PLAYER_GOAL = "div[class=imso_gs__gs-r]";
	private static final String DIV_MATCH_STATUS = "div[class*=imso_mh__lv-m-stts-cont]";
	private static final String SPAN_MATCH_TIME = "span[class*=imso_mh__ft-mtch]";
	private static final String TD_MATCH_TILE = "td[class*=liveresults-sports-immersive__match-tile]";

	private static final String DIV_HOME_SCORE = "div[class*=imso_mh__l-tm-sc]";
	private static final String DIV_VISITOR_SCORE = "div[class*=imso_mh__r-tm-sc]";

	private static final String DIV_HOME_SCORING_PLAYERS = "div[class*=imso_gs__left-team]";
	private static final String DIV_VISITOR_SCORING_PLAYERS = "div[class*=imso_gs__right-team]";

	public static void main(String[] args) {
		ScrappingUtil scrapping = new ScrappingUtil();
		
		String url = scrapping.assembleGoogleURL("são paulo", "santos");

		scrapping.getMatchInfo(url);
	}

	public PartidaGoogleDTO getMatchInfo(String url) {
		PartidaGoogleDTO partida = new PartidaGoogleDTO();

		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();

			String title = doc.title();
			LOGGER.info("Título da página: {}", title);

			//STATUS DA PARTIDA
			String matchStatus = handleMatchStatus(getMatchStatus(doc));
			LOGGER.info("Status da partida: {}", matchStatus);
			partida.setStatusPartida(matchStatus);

			String matchTime = getMatchTime(doc);
			if (!matchTime.equals("") && !matchStatus.equals("Não iniciada")) {
				
				//TEMPO DA PARTIDA
				LOGGER.info("Tempo da partida: {}", matchTime);
				partida.setTempoPartida(matchTime);
				
				//PLACARES
				Integer placarEquipeCasa = formatScoreToInteger(getTeamScore(doc, DIV_HOME_SCORE));
				Integer placarEquipeVisitante = formatScoreToInteger(getTeamScore(doc, DIV_VISITOR_SCORE));
				LOGGER.info("Placar do time da casa: {}", placarEquipeCasa);
				LOGGER.info("Placar do time visitante: {}", placarEquipeVisitante);
				partida.setPlacarEquipeCasa(placarEquipeCasa);
				partida.setPlacarEquipeVisitante(placarEquipeVisitante);
				
				//QUEM FEZ GOLS
				String golsEquipeCasa = getTeamScoringPlayers(doc, DIV_HOME_SCORING_PLAYERS);
				String golsEquipeVisitante = getTeamScoringPlayers(doc, DIV_VISITOR_SCORING_PLAYERS);
				LOGGER.info("Jogadores que fizeram gol da equipe casa: {}", golsEquipeCasa);
				LOGGER.info("Jogadores que fizeram gol da equipe visitante: {}", golsEquipeVisitante);
				partida.setGolsEquipeCasa(golsEquipeCasa);
				partida.setGolsEquipeVisitante(golsEquipeVisitante);

				//PLACAR DE PENALTIS
				Integer extendedHomeScore = getPenalities(doc, CASA);
				Integer extendedVisitorScore = getPenalities(doc, VISITANTE);
				LOGGER.info("Placar estendido da casa: {}", extendedHomeScore);
				LOGGER.info("Placar estendido visitantes: {}", extendedVisitorScore);
				partida.setPlacarEstendidoEquipeCasa(extendedHomeScore);
				partida.setPlacarEstendidoEquipeVisitante(extendedVisitorScore);
			}

			//TOTAL DE PARTIDAS NA PAGINA INICIAL
			Integer totalMatches = getAllMatches(doc);
			if (!totalMatches.equals(0)) {
				LOGGER.info("Existem {} partidas registradas na página inicial", totalMatches);
			}

			//NOME DAS EQUIPES
			List<String> names = getTeamNames(doc);
			if (!names.isEmpty()) {
				String casa = names.get(0);
				String visitante = names.get(1);
				
				LOGGER.info("Time da casa: {}", casa);
				LOGGER.info("Time visitante: {}", visitante);
				
				partida.setNomeEquipeCasa(casa);
				partida.setNomeEquipeVisitante(visitante);
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

		Boolean isMatchTime = doc.select(SPAN_MATCH_TIME).isEmpty();

		// ENCERRADO
		if (!isMatchTime) {
			String status = doc.select(SPAN_MATCH_TIME).first().text();

			if (status.contains("Encerrado")) {
				matchStatus = Status.ENCERRADO;
			}
		}

		// EM ANDAMENTO OU EM PÊNALTIS
		isMatchTime = doc.select(DIV_MATCH_STATUS).isEmpty();

		if (!isMatchTime) {
			String status = doc.select(DIV_MATCH_STATUS).first().text();
			matchStatus = Status.EM_ANDAMENTO;

			if (status.contains("Pênaltis")) {
				matchStatus = Status.PENALIDADES;
			}
		}

		return matchStatus;
	}

	public String getMatchTime(Document doc) {
		String matchTime = null;

		Boolean isMatchTime = doc.select(DIV_MATCH_STATUS).isEmpty();
		if (!isMatchTime) {
			matchTime = doc.select(DIV_MATCH_STATUS).first().text();
		}

		isMatchTime = doc.select(SPAN_MATCH_TIME).isEmpty();
		if (!isMatchTime) {
			matchTime = doc.select(SPAN_MATCH_TIME).first().text();
		}

		return fixMatchTime(matchTime);
	}

	public int getAllMatches(Document doc) {
		Boolean areThereMatches = doc.select(TD_MATCH_TILE).isEmpty();
		Integer numOfMatchesFound = 0;

		if (!areThereMatches) {
			numOfMatchesFound = doc.select(TD_MATCH_TILE).size();
		}

		return numOfMatchesFound;
	}

	public List<String> getTeamNames(Document doc) {
		return doc.select(DIV_MAIN).select("span").eachText();
	}

	public List<String> getTeamLogos(Document doc) {
		return doc.select(DIV_MAIN).select("img").eachAttr("src");
	}

	public String getTeamScore(Document doc, String htmlItem) {
		return doc.select(htmlItem).text();
	}

	public String getTeamScoringPlayers(Document doc, String htmlItem) {
		List<String> gols = new ArrayList<>();

		Elements elements = doc.select(htmlItem).select(DIV_PLAYER_GOAL);
		for (Element e : elements) {
			String gol = e.select(DIV_PLAYER_GOAL).text();
			gols.add(gol);
		}

		return String.join(", ", gols);
	}

	public Integer getPenalities(Document doc, String team) {
		Boolean isTherePenalities = doc.select(DIV_PENALTIS).isEmpty();

		if (!isTherePenalities) {
			String penalty = doc.select(DIV_PENALTIS).text().substring(0, 5).replace(" ", "");
			String[] division = penalty.split("-");

			return team.equals(CASA) ? formatScoreToInteger(division[0]) : formatScoreToInteger(division[1]);
		}

		return null;
	}

	private String fixMatchTime(String matchTime) {
		if (matchTime != null) {
			if (matchTime.contains("'")) {
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
		switch (status) {
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

	private Integer formatScoreToInteger(String score) {
		Integer valor;
		try {
			valor = Integer.parseInt(score);
		} catch (Exception e) {
			valor = 0;
		}
		return valor;
	}
	
	private String assembleGoogleURL(String homeTeam, String visitorTeam) {
		try {
			String home = homeTeam.replace(" ", "+").replace("-", "+");
			String visitor = visitorTeam.replace(" ", "+").replace("-", "+");
			
			return BASE_URL_GOOGLE + home + "+x+" + visitor + COMPLEMENTO_URL_GOOGLE;
		} catch(Exception e) {
			LOGGER.error("{}", e.getMessage());
		}
		
		return null;
	}
}
