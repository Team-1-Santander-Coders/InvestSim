package com.team1.investsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "com.team1.investsim")
@EnableJpaRepositories(basePackages = "com.team1.investsim.repositories")
@EnableTransactionManagement
@EntityScan(basePackages = "com.team1.investsim.entities")
public class InvestsimApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(InvestsimApplication.class, args);
	}

}
