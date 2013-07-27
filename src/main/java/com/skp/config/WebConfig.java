package com.skp.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.skp.controller.DataFeedController;



@Configuration
@EnableWebMvc
//@Import(value = {ComponentConfig.class, DataConfig.class, ExplicitSocialConfig.class})
//@PropertySource("classpath:config.properties")
@ComponentScan(basePackageClasses={DataFeedController.class})
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/WEB-INF/views/");
		internalResourceViewResolver.setSuffix(".jsp");
		return internalResourceViewResolver;
	}


	/*@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}*/

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
     
    PageableArgumentResolver resolver = new PageableArgumentResolver();
    
    resolver.setFallbackPagable(new PageRequest(1, 10, Direction.DESC,"created"));
     
    argumentResolvers.add(new ServletWebArgumentResolverAdapter(resolver));
}
		
}
