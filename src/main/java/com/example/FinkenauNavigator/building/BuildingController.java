package com.example.FinkenauNavigator.building;

import com.example.FinkenauNavigator.navigation.NavigationController;
import com.example.FinkenauNavigator.navigation.Navigator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    List<Building> allBuildings ;

    @GetMapping("/")
    String landingPage(Model model) {
        loadLandingPage(model);
        return "index";
    }
    @PostMapping("/navigate")
    public String resultPage(@RequestParam("start-location") String startLocation, @RequestParam("destination") String destination, Model model) {
        //Routenschritte berechnen und als Liste zurückgeben lassen, aktuell nur ein Gebäude, also nur für das erste berechnen
        Navigator currentNavigation = new Navigator(startLocation,destination);
        currentNavigation.setPath(navigationController.findPathBFS(allBuildings.getFirst().getID(), startLocation, destination));
        List<String> pathAsStrings = navigationController.convertPathToStringList(currentNavigation.getPath());
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

    @GetMapping("/navigate")
    String resultPage(Model model){
        loadLandingPage(model);
        return "index";
    }

    /**
     * zwar aktuell nur ein Gebäude, aber immer alle Gebäude durchgehen, alle rooms per Building ID
     * @param model
     */
    private void loadLandingPage(Model model) {
        allBuildings = buildingRepository.findAll();
        for (Building building : allBuildings){
            building.setRooms(buildingRepository.findAllSelectableRoomsWithNameFloorByBuildingId(building.getID()));
            model.addAttribute("allSelectableRooms",building.getRooms());
        }
        model.addAttribute("x", 0.0);
        model.addAttribute("y", 0.0);

        // Einstellung der Visibility: Versteckt die Flagge im Startscreen
        model.addAttribute("visibility", "hidden");
    }
}