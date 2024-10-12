package com.project.chess_be;

import com.project.chess_be.entities.User;
import com.project.chess_be.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChessBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessBeApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			UserRepository repository
//	){
//		return args -> {
//			var user = User.builder().name("Passawut").build();
//			repository.save(user);
//		};
//	};

}
