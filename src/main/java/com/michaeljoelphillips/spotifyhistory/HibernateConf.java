package com.michaeljoelphillips.spotifyhistory;

import java.util.Properties;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.core.env.Environment;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
public class HibernateConf {
  @Autowired
  private Environment env;

  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

    sessionFactory.setDataSource(dataSource());
    sessionFactory.setHibernateProperties(hibernateProperties());
    sessionFactory.setPackagesToScan(
        new String[] { "com.michaeljoelphillips.spotifyhistory.model" }
    );

    return sessionFactory;
  }

  @Bean
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName(env.getProperty("db.driver"));
    dataSource.setUrl(env.getProperty("db.url"));
    dataSource.setUsername(env.getProperty("db.username"));
    dataSource.setPassword(env.getProperty("db.password"));

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager hibernateTransactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory().getObject());

    return transactionManager;
  }

  private final Properties hibernateProperties() {
    Properties hibernateProperties = new Properties();

    hibernateProperties.setProperty(
        "hibernate.hbm2ddl.auto",
        env.getProperty("hibernate.hbm2ddl.auto")
    );

    hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));

    return hibernateProperties;
  }
}
