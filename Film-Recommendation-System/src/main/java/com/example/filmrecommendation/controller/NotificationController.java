package com.example.filmrecommendation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.filmrecommendation.model.Notification;
import com.example.filmrecommendation.service.NotificationStorage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController{
	
	
	@Autowired
	private NotificationStorage notificationStorage;
	
	@GetMapping("/")
	public String getNotifications(Model model) throws IOException, ClassNotFoundException{
		
		List<Notification> notifications = notificationStorage.getAllNotifications();
		
		Collections.reverse(notifications);
		
		model.addAttribute("notifications", notifications);
		
		return "notification";
	}
	
	@PostMapping("/clear")
	public String clearNotifications() throws IOException{
		
		notificationStorage.clearAllNotifications();
		
		return "redirect:/notifications/";
	}
	

	@PostMapping("/remove/{id}")
	public String removeNotificationById(@PathVariable("id") String id) throws IOException, ClassNotFoundException {
	    notificationStorage.removeNotificationById(id);
	    return "redirect:/notifications/"; 
	}

	
	@GetMapping("/test")
	public String testNotifications() throws IOException{
		
		notificationStorage.addNotification("test1title", "test1msg");
		notificationStorage.addNotification("test3title", "test3msg");
		notificationStorage.addNotification("test2title", "test2msg");
		notificationStorage.addNotification("test4title", "test4msg");
		
		return "redirect:/notifications/";
		
	}
}



