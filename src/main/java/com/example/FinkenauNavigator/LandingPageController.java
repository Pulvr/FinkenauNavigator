package com.example.FinkenauNavigator;

import com.example.FinkenauNavigator.classes.Building;
import com.example.FinkenauNavigator.classes.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String resultPage(Model model) {
        //model.addAttribute;

        return "result";
    }

    @GetMapping("/navigate")
    public String resultPage() {
        return "result";
    }
}
