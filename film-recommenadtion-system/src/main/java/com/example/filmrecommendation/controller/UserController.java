package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.UserStorage;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserStorage userStorage;

    @GetMapping("/")
    public String home() {
        return "redirect:/register";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        if (userStorage.addUser(user)) {
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, 
                       @RequestParam("password") String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        if (userStorage.validateUser(username, password)) {
            User user = userStorage.getUserByUsername(username);
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        user.setUsername(loggedInUser.getUsername()); 
        userStorage.updateUser(user);
        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        return "redirect:/profile";
    }

    @PostMapping("/changePassword")
    public String changePassword(
    		@RequestParam(name = "oldPassword") String oldPassword,
    	    @RequestParam(name = "newPassword") String newPassword,
    	    HttpSession session,
    	    RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        if (newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters long");
            return "redirect:/profile";
        }
        
        if (userStorage.changePassword(loggedInUser.getUsername(), oldPassword, newPassword)) {
            redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Old password is incorrect");
        }
        return "redirect:/profile";
    }
}