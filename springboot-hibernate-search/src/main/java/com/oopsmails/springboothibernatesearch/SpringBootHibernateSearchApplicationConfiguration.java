package com.oopsmails.springboothibernatesearch;

import com.oopsmails.springboothibernatesearch.repository.SearchRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class SpringBootHibernateSearchApplicationConfiguration {
}
