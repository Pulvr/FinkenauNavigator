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
    //wird in der Landingpage gefüllt, dann berufen sich andere Methoden wie resultPage darauf
    List<Building> allBuildings ;

    @GetMapping("/")
    String landingPage(Model model) {
        loadLandingPage(model);
        return "index";
    }

    /**
     * Haupt Nav Logik hier läuft alles zusammen. Navigationsanfragen werden verarbeitet und weitergeleitet.
     * @param startLocation wo starten User mit der Navigation
     * @param destination was ist das Ziel des Users
     * @param model
     * @return
     */
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
        goalPost(model, buildingRepository.getXCoordinate(destination), buildingRepository.getYCoordinate(destination), "visible");

        return "result";
    }

    /**
     * getMapping für /navigate, leitet einen auf Hauptseite weiter, damit man nicht /navigate direkt als URL eingeben kann
     * @param model
     * @return
     */
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
        goalPost(model, 0.0, 0.0, "hidden");
    }

    /**
     * Hilfsmethode um den Goalpost zu zeigen
     * @param model das Website Model
     * @param xCoordinate x Koordinate der Flagge
     * @param yCoordinate y Koordinate der Flagge
     * @param visibility Sichtbarkeit
     */
    private void goalPost(Model model, double xCoordinate, double yCoordinate, String visibility) {
        model.addAttribute("x", xCoordinate);
        model.addAttribute("y", yCoordinate);

        // Einstellung der Visibility: Zeigt die Flagge am Ziel an
        model.addAttribute("visibility", visibility);
    }
}