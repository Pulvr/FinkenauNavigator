package com.example.FinkenauNavigator.building;

import com.example.FinkenauNavigator.navigation.NavigationController;
import com.example.FinkenauNavigator.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BuildingController {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private NavigationController navigationController;

    @GetMapping("/")
    String landingPage(Model model) {
        Building myBuilding = new Building("Finkenau", buildingRepository.findAllSelectableRoomsWithNameFloorByBuildingId(1));
        model.addAttribute("allSelectableRooms", myBuilding.getRooms());

        // Titel bleibt leer
        //model.addAttribute("title", "");

        //Übergabe der Koordinaten des Raums, um die Flagge anzeigen zu lassen
        model.addAttribute("x", 0.0);
        model.addAttribute("y", 0.0);

        // Einstellung der Visibility: Versteckt die Flagge im Startscreen
        model.addAttribute("visibility", "hidden");

        return "index";
    }

    //not sure if this should stay in here or move to NavigationController
    @GetMapping("/navigate")
    public String resultPage(@RequestParam("start-location") String startLocation, @RequestParam("destination") String destination, Model model) {
        //Routenschritte berechnen und als Liste zurückgeben lassen
        List<Room> path = navigationController.findPathBFS(1, startLocation, destination);
        List<String> pathAsStrings = navigationController.convertPathToStringList(path);
        model.addAttribute("path", pathAsStrings);

        // Anzeige des Weges
        model.addAttribute("title", " - navigiert dich von " +  startLocation + " zu " + destination);

        //Übergabe der Koordinaten des Raums, an denen die Flagge angezeigt werden soll
        model.addAttribute("x", buildingRepository.getXCoordinate(destination));
        model.addAttribute("y", buildingRepository.getYCoordinate(destination));

        // Einstellung der Visibility: Zeigt die Flagge am Ziel an
        model.addAttribute("visibility", "visible");

        return "result";
    }
}