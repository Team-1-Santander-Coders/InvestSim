package com.team1.investsim;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.services.AssetService;
import com.team1.investsim.services.DataLoaderService;
import com.team1.investsim.services.HistoricalDataService;
import com.team1.investsim.test.TestCadastro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

import static com.team1.investsim.test.TestCadastro.*;

@SpringBootApplication(scanBasePackages = "com.team1.investsim")
@EnableJpaRepositories(basePackages = "com.team1.investsim.repositories")
@EnableTransactionManagement
@EntityScan(basePackages = "com.team1.investsim.entities")
public class InvestsimApplication {

	public static void main(String[] args) throws HistoricalDataNotFoundException, IllegalDateException {

		ApplicationContext context = SpringApplication.run(InvestsimApplication.class, args);

		TestCadastro testCadastro = context.getBean(TestCadastro.class);

		testCadastro.testeCriarEntidades();


	}

}
