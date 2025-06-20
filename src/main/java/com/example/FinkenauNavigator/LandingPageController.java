package com.example.FinkenauNavigator;

import com.example.FinkenauNavigator.classes.Building;
import com.example.FinkenauNavigator.classes.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class LandingPageController {


    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    String landingPage(Model model){
        Building myBuilding = new Building("Finkenau",buildingRepository.findAllSelectableRoomsWithNameFloorByBuildingId(1));
        model.addAttribute("allSelectableRooms",myBuilding.getRooms());

        return "index";
    }

    @PostMapping("/navigate")
    String resultPage() {
        return "result";
    }

    @GetMapping("/testPath")
    public String testPath(Model model) {
        List<Room> path = findPathBFS(1, 3, 10);  // z.B. von Raum ID 3 zu 10 im Gebäude 1
        model.addAttribute("path", path);
        return "result"; // In Thymeleaf kannst du path dann anzeigen
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

    public List<Room> findPathBFS(int buildingId, int startRoomId, int targetRoomId) {
        Map<Integer, Room> roomGraph = getRoomGraph(buildingId); //erstellten Graphen lokal referenzieren

        //Referenz für Start und Ziel setzen
        Room startRoom = roomGraph.get(startRoomId);
        Room targetRoom = roomGraph.get(targetRoomId);

        //Null-Check
        if (startRoom == null || targetRoom == null) return Collections.emptyList();

        // BFS-Queue
        Queue<Room> roomQueue = new LinkedList<>();
        // Map, um den Weg zurückzuverfolgen
        Map<Room, Room> parentMap = new HashMap<>();
        // Alle bereits überprüften Räume
        Set<Room> visitedRooms = new HashSet<>();

        //Startraum hinzufügen
        roomQueue.add(startRoom);
        visitedRooms.add(startRoom);

        //Pfadfindungs-Algorithmus
        while (!roomQueue.isEmpty()) {
            // Erstes Element aus der Queue entnehmen
            Room current = roomQueue.poll();

            // Rückverfolgung des Pfads, sobald Ziel erreicht ist
            if (current.equals(targetRoom)) {
                List<Room> path = new LinkedList<>();
                for (Room step = targetRoom; step != null; step = parentMap.get(step)) {
                    path.addFirst(step);
                }
                return path;
            }

            // Nachbarn prüfen und in Queue aufnehmen
            for (Room neighbour : current.neighbours) {
                if (!visitedRooms.contains(neighbour)) {
                    visitedRooms.add(neighbour);
                    parentMap.put(neighbour, current);
                    roomQueue.add(neighbour);
                }
            }
        }

        // Kein Pfad gefunden
        return Collections.emptyList();
    }
}
