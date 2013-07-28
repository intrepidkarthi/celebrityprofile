package com.skp.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataFeedController {

	@RequestMapping("/fetchdata")
	public ModelAndView fetchData(){
		HashMap<String, String> values = new HashMap<String, String>();
		ModelAndView modelAndView = new ModelAndView("profile");
		values.put("Name", "Rajini");
		values.put("Occupation", "Film Actor");
		values.put("ImageUrl", "http://www.karomasti.in/telugu-cinema/wp-content/uploads/2013/02/rajini.jpg");
		values.put("Occupation1", "Film Actor");
		values.put("Occupation2", "Film Actor");
		values.put("Occupation3", "Film Actor");
		
		modelAndView.addObject("data", values);
		
		return modelAndView;
	}
	
	@RequestMapping("/")
	public ModelAndView defaultpage(){
		HashMap<String, String> values = new HashMap<String, String>();
		ModelAndView modelAndView = new ModelAndView("profile");
		values.put("Name", "Rajini");
		values.put("Occupation", "Film Actor");
		values.put("ImageUrl", "http://www.karomasti.in/telugu-cinema/wp-content/uploads/2013/02/rajini.jpg");
		values.put("Occupation", "Film Actor");
		values.put("Occupation", "Film Actor");
		values.put("Occupation", "Film Actor");
		
		modelAndView.addObject("data", values);
		
		return modelAndView;
	}
	
}
