package com.team1.investsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import static com.team1.investsim.test.TestCadastro.*;

@SpringBootApplication(scanBasePackages = "com.team1.investsim")
@EnableJpaRepositories(basePackages = "com.team1.investsim.repositories")
@EntityScan(basePackages = "com.team1.investsim.entities")
public class InvestsimApplication {
	public static void main(String[] args) {
		SpringApplication.run(InvestsimApplication.class, args);
	}
}
