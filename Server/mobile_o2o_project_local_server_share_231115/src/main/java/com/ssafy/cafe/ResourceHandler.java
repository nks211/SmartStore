package com.ssafy.cafe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandler implements WebMvcConfigurer {


	@Value("${uploadPath}")
    String uploadPath;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	//imgs/menu로 요청이 올 때 static/imgs/menu, uploadPath에서 찾는다. 
    	registry.addResourceHandler("/imgs/menu/**").addResourceLocations( "classpath:/static/imgs/menu/", uploadPath);
    }
}
