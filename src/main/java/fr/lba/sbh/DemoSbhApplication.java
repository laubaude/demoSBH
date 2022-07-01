package fr.lba.sbh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import fr.lba.sbh.commons.NaturalRepositoryImpl;

@EnableCaching
@Configuration
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = NaturalRepositoryImpl.class)
public class DemoSbhApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSbhApplication.class, args);
    }

}
