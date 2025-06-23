package com.example.FinkenauNavigator.building;

import com.example.FinkenauNavigator.room.Room;
import com.example.FinkenauNavigator.room.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class BuildingController {


    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("/")
    String landingPage(Model model) {
        Building myBuilding = new Building("Finkenau", buildingRepository.findAllSelectableRoomsWithNameFloorByBuildingId(1));
        model.addAttribute("allSelectableRooms", myBuilding.getRooms());

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

