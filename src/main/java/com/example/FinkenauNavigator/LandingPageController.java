package com.example.FinkenauNavigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LandingPageController {

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("/")
    String landingPage(){
        return "index";
    }

    @PostMapping("/navigate")
    String resultPage(Model model, @RequestParam("start") String start, @RequestParam("goal") String goal) {
        Building myBuilding = new Building(start, goal);
        buildingRepository.save(myBuilding);

        if (!buildingRepository.findRoomByName(start).isEmpty()){
            model.addAttribute("Start", start);
            model.addAttribute("Goal", goal);
        }else{
            model.addAttribute("UserOrError", "Error");
        }

        return "result";
    }
}
