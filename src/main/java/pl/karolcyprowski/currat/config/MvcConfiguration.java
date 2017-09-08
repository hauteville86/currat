package pl.karolcyprowski.currat.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
//import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import pl.karolcyprowski.currat.controller.ControllerDataProvider;
import pl.karolcyprowski.currat.controller.ControllerDataProviderImpl;
import pl.karolcyprowski.currat.core.DataProcessor;
import pl.karolcyprowski.currat.core.DataProcessorImpl;
import pl.karolcyprowski.currat.core.TaskHandler;
import pl.karolcyprowski.currat.core.TaskHandlerImpl;
import pl.karolcyprowski.currat.dao.NBPDataDAO;
import pl.karolcyprowski.currat.dao.NBPDataDAOImpl;
import pl.karolcyprowski.currat.monitor.NBPMonitor;
import pl.karolcyprowski.currat.nbp.NBPRequestBuilder;
import pl.karolcyprowski.currat.nbp.NBPRequestBuilderImpl;
import pl.karolcyprowski.currat.nbp.NBPRestConsummer;
import pl.karolcyprowski.currat.nbp.NBPRestConsummerImpl;
import pl.karolcyprowski.currat.service.DBService;
import pl.karolcyprowski.currat.service.DBServiceImpl;
import pl.karolcyprowski.currat.service.NBPService;
import pl.karolcyprowski.currat.service.NBPServiceImpl;
import pl.karolcyprowski.currat.service.QueryParameterProcessor;
import pl.karolcyprowski.currat.service.QueryParameterProcessorImpl;
import pl.karolcyprowski.currat.spark.SparkTool;
import pl.karolcyprowski.currat.spark.SparkToolImpl;

@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	@Bean
	public SparkTool sparkTool() {
		return new SparkToolImpl();
	}
	
	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory()
	{
		return new HibernateJpaSessionFactoryBean();
	}
	
	@Bean
	public ViewResolver viewResolver(){
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;		
	}
	
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
//		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		return engine;
	}
	
	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("/WEB-INF/view/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		return resolver;
	}
	
	@Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	} 
	
	@Bean
	public TaskHandler taskHandler() {
		return new TaskHandlerImpl();
	}
	
	@Bean
	public NBPRestConsummer nbpRestConsummer() {
		return new NBPRestConsummerImpl();
	}
	
	@Bean
	public DBService dbService() {
		return new DBServiceImpl();
	}
	
	@Bean
	public NBPService nbpService() {
		return new NBPServiceImpl();
	}
	
	@Bean
	public NBPRequestBuilder nbpRequestBuilder() {
		return new NBPRequestBuilderImpl();
	}
	
	@Bean
	public NBPDataDAO nbpDataDAO() {
		return new NBPDataDAOImpl();
	}
	
	@Bean
	public DataProcessor dataProcessor() {
		return new DataProcessorImpl();
	}
	
	@Bean
	public QueryParameterProcessor queryParameterProcessor() {
		return new QueryParameterProcessorImpl();
	}
	
	@Bean
	public NBPMonitor nbpMonitor() {
		return new NBPMonitor(taskHandler());
	}
	
	@Bean
	public ControllerDataProvider controllerDataProvider() {
		return new ControllerDataProviderImpl();
	}
	
	public static ApplicationContext getContext() {
		return applicationContext;
	}
	
	
	
	
}
