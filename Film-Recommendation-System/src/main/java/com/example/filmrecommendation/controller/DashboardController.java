package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<String> genres = Arrays.asList("Action", "Comedy", "Sci-Fi", "Romance");
        model.addAttribute("user", loggedInUser);
        model.addAttribute("genres", genres);

        return "dashboard";
    }
}
