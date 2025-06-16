package com.example.FinkenauNavigator;

import com.example.FinkenauNavigator.classes.Building;
import com.example.FinkenauNavigator.classes.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LandingPageController {


    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public Map<Integer, Room> getRoomGraph(int buildingId){
        //Alle Räume aus gleichem Gebäude holen
        List<Room> rooms = buildingRepository.findAllRoomsByBuildingId(buildingId);

        //Map für schnellen Zugriff erstellen und alle Objekte ablegen
        Map<Integer, Room> roomMap = new HashMap<>();
        for (Room room : rooms) {
            roomMap.put(room.getId(), room);
        }

        //Nachbaren laden und in der Map abspeichern
        jdbcTemplate.query("SELECT * FROM ROOM_CONNECTION", (resultSet) -> {
            int fromId = resultSet.getInt("FROM_ID");
            int toId = resultSet.getInt("TO_ID");

            Room from = roomMap.get(fromId);
            Room to = roomMap.get(toId);

            if (from != null && to != null) {
                from.addNeighbour(to);
                to.addNeighbour(from);
            }
        });
        return roomMap;
    }
}
