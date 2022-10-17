package com.oopsmails.springboothibernatesearch;

import com.oopsmails.springboothibernatesearch.index.Indexer;
import com.oopsmails.springboothibernatesearch.model.Plant;
import com.oopsmails.springboothibernatesearch.repository.PlantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
public class SpringBootHibernateSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHibernateSearchApplication.class, args);
	}

	@Bean
	@ConditionalOnProperty("db.using.embedded")
	public ApplicationRunner initializeData(PlantRepository plantRepository) throws Exception {
		return (ApplicationArguments args) -> {
			List<Plant> plants = Arrays.asList(
					new Plant("subalpine fir", "abies lasiocarpa", "pinaceae"),
					new Plant("sour cherry", "prunus cerasus", "rosaceae"),
					new Plant("asian pear", "pyrus pyrifolia", "rosaceae"),
					new Plant("asiab pear", "pyrus pyrifolia", "rosaceae"),
					new Plant("chinese witch hazel", "hamamelis mollis", "hamamelidaceae"),
					new Plant("silver maple", "acer saccharinum", "sapindaceae"),
					new Plant("cucumber tree", "magnolia acuminata", "magnoliaceae"),
					new Plant("korean rhododendron", "rhododendron mucronulatum", "ericaceae"),
					new Plant("water lettuce", "pistia", "araceae"),
					new Plant("sessile oak", "quercus petraea", "fagaceae"),
					new Plant("common fig", "ficus carica", "moraceae")
			);
			plantRepository.saveAll(plants);
		};
	}

	@Bean
	public ApplicationRunner buildIndex(Indexer indexer) {
		log.info("Creating index ................................");
		return (ApplicationArguments args) -> {
			indexer.indexPersistedData("com.oopsmails.springboothibernatesearch.model.Plant");
		};
	}
}
