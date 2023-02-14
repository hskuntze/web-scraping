package br.com.kuntzeprojects.webscraping.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.kuntzeprojects.webscraping.services.ScrapingService;
import br.com.kuntzeprojects.webscraping.util.DataUtil;

@Configuration
@EnableScheduling
public class PartidaTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PartidaTask.class);
	private static final String TIMEZONE = "America/Sao_Paulo";
	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	@Autowired
	private ScrapingService scrapService;
	
	@Scheduled(cron = "0/30 * 19-23 * * WED", zone = TIMEZONE)
	public void taskPartidaQuartasFeira() {
		startAgenda("taskPartidaQuartasFeiras()");
	}
	
	@Scheduled(cron = "0/30 * 19-23 * * THU", zone = TIMEZONE)
	public void taskPartidaQuintasFeira() {
		startAgenda("taskPartidaQuintasFeira()");
	}
	
	@Scheduled(cron = "0/30 * 16-23 * * SAT", zone = TIMEZONE)
	public void taskPartidaSabados() {
		startAgenda("taskPartidaSabados()");
	}
	
	@Scheduled(cron = "0/30 * 11-14 * * SUN", zone = TIMEZONE)
	public void taskPartidaDomingosMatutino() {
		startAgenda("taskPartidaDomingosMatutino()");
	}
	
	@Scheduled(cron = "0/30 * 16-23 * * SUN", zone = TIMEZONE)
	public void taskPartidaDomingosVespertino() {
		startAgenda("taskPartidaDomingosVespertino()");
	}
	
	@Scheduled(cron = "0/30 * 16-19 * * TUE", zone = TIMEZONE)
	public void teste() {
		startAgenda("taskTeste()");
	}
	
	private void startAgenda(String diaSemana) {
		recordLog(String.format("%s: %s", diaSemana, DataUtil.formatDate(new Date(), DATE_FORMAT)));
		
		scrapService.checkMatchPeriod();
	}
	
	private void recordLog(String msg) {
		LOGGER.info("[TASK] Mensagem: "+msg);
	}
}
