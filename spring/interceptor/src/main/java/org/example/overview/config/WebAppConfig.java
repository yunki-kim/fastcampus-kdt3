package org.example.overview.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@PropertySources(@PropertySource("classpath:/property/datasource.properties"))
@ComponentScan(basePackages = "org.example.overview",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Component.class, Repository.class, Service.class})}
)
@MapperScan(basePackageClasses = org.example.overview.members.mapper.MemberMapper.class)
public class WebAppConfig implements EnvironmentAware {

    Environment environment; // null 임을 방지

    @Override
    public void setEnvironment(Environment environment) { // environment 객체가 null임을 방지
        this.environment = environment;
    }

    @Bean
    public BasicDataSource basicDataSource() {
        // org.apache.commons.dbcp2.BasicDataSource

        org.apache.commons.dbcp2.BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));
        basicDataSource.setUrl(environment.getProperty("spring.datasource.url"));
        basicDataSource.setUsername(environment.getProperty("spring.datasource.username"));
        basicDataSource.setPassword(environment.getProperty("spring.datasource.password"));

        basicDataSource.setInitialSize(Integer.parseInt(environment.getProperty("apache.commons.dbcp2.config.initialSize")));
        basicDataSource.setMaxTotal(Integer.parseInt(environment.getProperty("apache.commons.dbcp2.config.maxTotal")));
        basicDataSource.setMinIdle(Integer.parseInt(environment.getProperty("apache.commons.dbcp2.config.maxIdle")));
        basicDataSource.setMinIdle(Integer.parseInt(environment.getProperty("apache.commons.dbcp2.config.minIdle")));
        basicDataSource.setMaxWaitMillis(Long.parseLong(environment.getProperty("apache.commons.dbcp2.config.maxWaitMillis")));

        return basicDataSource;
    }

    @Bean
    public ComboPooledDataSource comboPooledDataSource() {
        // com.mchange.v2.c3p0.ComboPooledDataSource

        com.mchange.v2.c3p0.ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(environment.getProperty("spring.datasource.driverClassName"));
            comboPooledDataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
            comboPooledDataSource.setUser(environment.getProperty("spring.datasource.username"));
            comboPooledDataSource.setPassword(environment.getProperty("spring.datasource.password"));

            comboPooledDataSource.setInitialPoolSize(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.initialPoolSize")));
            comboPooledDataSource.setMaxPoolSize(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.maxPoolSize")));
            comboPooledDataSource.setMinPoolSize(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.minPoolSize")));
            comboPooledDataSource.setMaxIdleTime(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.maxIdleTime")));
            comboPooledDataSource.setAcquireIncrement(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.acquireIncrement")));
            comboPooledDataSource.setAcquireRetryAttempts(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.acquireRetryAttempts")));
            comboPooledDataSource.setAcquireRetryDelay(Integer.parseInt(environment.getProperty("com.mchange.v2.c3p0.config.acquireRetryDelay")));

        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        return comboPooledDataSource;
    }

    @Bean
    public HikariDataSource hikariDataSource() {
        // com.zaxxer.hikari.HikariConfig

        com.zaxxer.hikari.HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));
        hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        hikariConfig.setUsername(environment.getProperty("spring.datasource.username"));
        hikariConfig.setPassword(environment.getProperty("spring.datasource.password"));

        hikariConfig.setPoolName(environment.getProperty("com.zaxxer.hikari.config.poolName"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("com.zaxxer.hikari.config.maximumPoolSize")));
        hikariConfig.setConnectionTimeout(Long.parseLong(environment.getProperty("com.zaxxer.hikari.config.connectionTimeOut")));
        hikariConfig.setIdleTimeout(Long.parseLong(environment.getProperty("com.zaxxer.hikari.config.idleTimeout")));
        hikariConfig.setAutoCommit(Boolean.parseBoolean(environment.getProperty("com.zaxxer.hikari.config.autoCommit")));
        hikariConfig.setAutoCommit(Boolean.parseBoolean(environment.getProperty("com.zaxxer.hikari.config.readOnly")));

        com.zaxxer.hikari.HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        System.out.println(hikariDataSource);

        return hikariDataSource;
    }

    @Bean
    public DataSource dataSource() {
//        return basicDataSource(); // dbcp2
//        return comboPooledDataSource(); // c3p0
        return hikariDataSource(); // hikariConfig

    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource()); // 명시적인 데이터 소스 설정
        return sqlSessionFactoryBean.getObject();
    }

//    @Bean
//    public SqlSessionTemplate sqlSession() throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory());
//    }

    @Bean
    public org.springframework.jdbc.datasource.DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

}

