package org.interledger.client;

import org.interledger.messages.RemoteLedgerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);    

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		builder.requestFactory(new HttpComponentsClientHttpRequestFactory());
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
	        RemoteLedgerInfo ledgerInfo = restTemplate.getForObject(
	        		"https://red.ilpdemo.org/ledger/", RemoteLedgerInfo.class);
			log.info(ledgerInfo.toString());
		};
	}
	
}
