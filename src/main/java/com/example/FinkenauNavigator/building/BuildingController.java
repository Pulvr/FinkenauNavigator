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

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @GetMapping("/test")
    String testPath(Model model) {
        List<Room> path = findPathBFS(1, "Toilette E37", "E62");
        List<String> pathAsStrings = convertPathToStringList(path);
        model.addAttribute("path", pathAsStrings);
        return "testingFile";
    }

    public Map<String, Room> getRoomGraph(int buildingId) {
        //Alle Räume aus gleichem Gebäude holen
        List<Room> rooms = buildingRepository.findAllRoomsByBuildingId(buildingId);

        //Map für schnellen Zugriff erstellen und alle Objekte ablegen
        Map<String, Room> roomMap = new HashMap<>();
        for (Room room : rooms) {
            roomMap.put(room.getName(), room);
        }

        //Nachbarn laden und in der Map abspeichern
        jdbcTemplate.query("SELECT * FROM ROOM_CONNECTION", (resultSet) -> {
            String fromName = resultSet.getString("FROM_NAME");
            String toName = resultSet.getString("TO_NAME");

            Room from = roomMap.get(fromName);
            Room to = roomMap.get(toName);

            if (from != null && to != null) {
                from.addNeighbour(to);
                to.addNeighbour(from);
            }
        });
        return roomMap;
    }

    public List<Room> findPathBFS(int buildingId, String startRoomName, String targetRoomName) {
        Map<String, Room> roomGraph = getRoomGraph(buildingId); //erstellten Graphen lokal referenzieren

        //Referenz für Start und Ziel setzen
        Room startRoom = roomGraph.get(startRoomName);
        Room targetRoom = roomGraph.get(targetRoomName);

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

        //Pfadfindungsalgorithmus
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

    public List<String> convertPathToStringList(List<Room> path) {
        String roomToFloor = "Verlasse %s und gehe nach %s.";
        String floorToFloor = "Folge dem %s und biege am Ende %s ab, um in %s zu gelangen.";
        String floorToRoom = " %s befindet sich auf der %s Seite.";
        String entranceToFloor = "Betrete das Gebäude durch den %s und betrete %s.";
        String stairwayToMainEntrance = "Biege am Ende von %s nach links ab. Du solltest den %s nun sehen können.";
        String stairwayToFloor = "Biege am Ende von %s nach s% ab und folge dem %s.";

        List<String> result = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {

            //Potenzielle Kantentypen
            //Startraum → Flur
            if (path.get(i).getRoomType() == RoomType.ROOM && path.get(i + 1).getRoomType() == RoomType.FLOOR) {
                if (path.getLast().getId() > path.get(i).getId() && path.get(i).isOnRightSide()) {
                    result.add(String.format(roomToFloor, path.get(i).getName(), "links"));
                } else if (path.getLast().getId() < path.get(i).getId() && path.get(i).isOnRightSide()) {
                    result.add(String.format(roomToFloor, path.get(i).getName(), "rechts"));
                } else if (path.getLast().getId() < path.get(i).getId() && path.get(i).isOnLeftSide()) {
                    result.add(String.format(roomToFloor, path.get(i).getName(), "links"));
                } else {
                    result.add(String.format(roomToFloor, path.get(i).getName(), "rechts"));
                }
            }

            //Flur → Flur
            if (path.get(i).getRoomType() == RoomType.FLOOR && path.get(i + 1).getRoomType() == RoomType.FLOOR) {
                if ((path.get(i).getName().equals(("Flur E27 - E37")))) {
                    result.add(String.format(floorToFloor, path.get(i).getName(), "links", path.get(i + 1).getName()));
                } else {
                    result.add(String.format(floorToFloor, path.get(i).getName(), "rechts", path.get(i + 1).getName()));
                }
            }

            //Flur -> (Raum || Treppenhaus)
            if (path.get(i).getRoomType() == RoomType.FLOOR && path.get(i + 1).getRoomType() == RoomType.ROOM || path.get(i).getRoomType() == RoomType.FLOOR && path.get(i + 1).getRoomType() == RoomType.STAIRWAY) {

                //String.format für "normale" Ausrichtung (siehe Kommentare in Datenbank)
                if (path.getFirst().getId() < path.get(i).getId() && path.get(i + 1).isOnLeftSide()) {
                    result.add(String.format(floorToRoom,path.get(i + 1).getName(), "rechten"));
                } else if (path.getFirst().getId() < path.get(i).getId() && path.get(i + 1).isOnRightSide()) {
                    result.add(String.format(floorToRoom,path.get(i + 1).getName(), "linken"));
                }
                //String.format für "invertierte" Ausrichtung (siehe Kommentare in Datenbank)
                else if (path.getFirst().getId() > path.get(i).getId() && path.get(i).isOnLeftSide()) {
                    result.add(String.format(floorToRoom, "rechten"));
                } else if (path.getFirst().getId() > path.get(i).getId() && path.get(i).isOnRightSide()) {
                    result.add(String.format(floorToRoom, "linken"));
                }
            }

            //Eingang - Flur
            if (path.get(i).getRoomType() == RoomType.ENTRANCE && path.get(i + 1).getRoomType() == RoomType.FLOOR) {
                result.add(String.format(entranceToFloor, path.get(i).getName(), path.get(i + 1).getName()));
            }

            //Treppenhaus - Flur
            if (path.get(i).getRoomType() == RoomType.STAIRWAY && path.get(i + 1).getRoomType() == RoomType.FLOOR) {
                if (path.getLast().getId() > path.get(i).getId()) {
                    result.add(String.format(stairwayToFloor, path.get(i).getName(),"rechts", path.get(i + 1).getName()));
                } else {
                    result.add(String.format(roomToFloor,path.get(i).getName(), "links", path.get(i + 1).getName()));
                }
            }

            //Treppenhaus - Haupteingang
            if (path.get(i).getRoomType() == RoomType.STAIRWAY && path.get(i + 1).getRoomType() == RoomType.ENTRANCE) {
                result.add(String.format(stairwayToMainEntrance, path.get(i).getName(), path.get(i + 1).getName()));
            }
        }
        return result;
    }
}

