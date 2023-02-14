package br.com.kuntzeprojects.webscraping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.kuntzeprojects.webscraping.util.ScrapingUtil;

@Configuration
public class AppConfig {

	@Bean
	ScrapingUtil getScrapingUtil() {
		return new ScrapingUtil();
	}
}
