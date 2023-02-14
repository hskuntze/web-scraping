package br.com.kuntzeprojects.webscraping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final String BASE_PACKAGE = "br.com.kuntzeprojects.webscraping.controllers";
	private static final String API_TITLE = "Demo Web Scrapping with Java";
	private static final String API_DESC = "REST API that obtains data from soccer matches in real time";
	private static final String API_VERSION = "1.0.0";
	private static final String CONTACT_NAME = "Hassan Kuntze Rodrigues da Cunha";
	private static final String CONTACT_GITHUB = "https://github.com/hskuntze";
	private static final String CONTACT_EMAIL = "hskuntze@gmail.com";
	
	@Bean
	Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(basePackage(BASE_PACKAGE))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(buildApiInfo());
	}

	private ApiInfo buildApiInfo() {
		return new ApiInfoBuilder()
				.title(API_TITLE)
				.version(API_VERSION)
				.description(API_DESC)
				.contact(new Contact(CONTACT_NAME, CONTACT_GITHUB, CONTACT_EMAIL))
				.build();
	}
}
