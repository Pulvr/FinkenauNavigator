package com.example.FinkenauNavigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LandingPageController {

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("/")
    String landingPage(Model model){
        List<Room> allSelectableRooms = buildingRepository.findAllSelectableRoomsByBuildingId(1);
        Building myBuilding = new Building("Finkenau",allSelectableRooms);
        //for(Room room : allSelectableRooms){ System.out.println(room); }
        model.addAttribute("allSelectableRooms",allSelectableRooms);


        return "index";
    }

    @PostMapping("/navigate")
    String resultPage() {
        return "result";
    }
}
