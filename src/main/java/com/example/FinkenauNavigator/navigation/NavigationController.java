package com.example.FinkenauNavigator.navigation;

import com.example.FinkenauNavigator.navigation.Navigator;
import com.example.FinkenauNavigator.building.BuildingRepository;
import com.example.FinkenauNavigator.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NavigationController {

    private final Navigator navigator;

    @Autowired
    public NavigationController(Navigator navigator) {
        this.navigator = navigator;
    }

    @GetMapping("/test")
    String testPath(Model model) {

        List<Room> path = navigator.findPathBFS(1, "Toilette E37", "E62");
        List<String> pathAsStrings = navigator.convertPathToStringList(path);
        model.addAttribute("path", pathAsStrings);
        return "testingFile";
    }
}
