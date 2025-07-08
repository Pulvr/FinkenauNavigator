package com.example.FinkenauNavigator.building;

import com.example.FinkenauNavigator.navigation.NavigationController;
import com.example.FinkenauNavigator.room.Room;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BuildingController {


    private final BuildingRepository buildingRepository;
    private final NavigationController navigationController;

    public BuildingController(BuildingRepository buildingRepository, NavigationController navigationController) {
        this.buildingRepository = buildingRepository;
        this.navigationController = navigationController;
    }

    @GetMapping("/")
    String landingPage(Model model) {
        Building myBuilding = new Building("Finkenau", buildingRepository.findAllSelectableRoomsWithNameFloorByBuildingId(1));
        model.addAttribute("allSelectableRooms", myBuilding.getRooms());

        return "index";
    }

    //not sure if this should stay in here or move to NavigationController
    @GetMapping("/navigate")
    public String resultPage(@RequestParam("start-location") String startLocation, @RequestParam("destination") String destination, Model model) {
        //Routenschritte berechnen und als Liste zurückgeben lassen
        List<Room> path = navigationController.findPathBFS(1, startLocation, destination);
        List<String> pathAsStrings = navigationController.convertPathToStringList(path);
        model.addAttribute("path", pathAsStrings);

        //Übergabe Start- und Zielort
        model.addAttribute("start", startLocation);
        model.addAttribute("goal", destination);
        return "result";
    }
}