package com.zhou.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/*
读取 jdbc.properties 配置文件，配置连接池
*/
@Configuration
@PropertySource("classpath:jdbc.properties")
@EnableJpaRepositories("com.zhou.dao")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.zhou",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value= EnableWebMvc.class)})
public class RootConfig {

    private static final Logger logger=Logger.getLogger(RootConfig.class);

    //通过读取配置文件的将值赋予定义的字段
    @Value("${druid.driver}")
    private String driver;
    @Value("${druid.url}")
    private String url;
    @Value("${druid.username}")
    private String username;
    @Value("${druid.password}")
    private String password;

    /*
    配置 Druid 连接池
     */
    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        logger.info(driver);
        logger.info(url);
        logger.info(username);
        logger.info(password);

        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /*
    配置 JdbcTemplate 模板
     */
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
    /*
    配置 NamedParameterJdbcTemplate 模板,可以使用参数命名来赋值
     */
    //@Bean
    //public NamedParameterJdbcTemplate jdbcTemplate(){
    //    return new NamedParameterJdbcTemplate(dataSource());
    //}


    /*
    配置 Jpa,基本的 Jpa配置
    获取   @PersistenceUnit  EntityManagerFactory emf;
    emf.createEntityManager().persist(对象);  添加
    emf.createEntityManager().find();        查找
    emf.createEntityManager().merge();       保存（更新）

    获取   @PersistenceContext  EntityManagerFactory emf;
    emf.persist(对象);  添加
    emf.find();        查找
    emf.merge();       保存（更新）

    @PersistenceUnit  @PersistenceContext  必须配置
    使用<context:annotation-config />或<context:component-scan />则不用配置
    @Bean
    public PersistenceAnnotationBeanPostProcessor postProcessor(){
        return new PersistenceAnnotationBeanPostProcessor();
    }
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource());
        emfb.setJpaVendorAdapter(jpaVendorAdapter());
        emfb.setPackagesToScan("com.zhou.domain");
        return emfb;
    }
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        //adapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
        return adapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

}
