package com.example.FinkenauNavigator.navigation;

import com.example.FinkenauNavigator.building.BuildingRepository;
import com.example.FinkenauNavigator.room.Room;
import com.example.FinkenauNavigator.room.RoomType;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class NavigationController {

    private final NavigationRepository navigationRepository;
    private final BuildingRepository buildingRepository;

    public NavigationController(BuildingRepository buildingRepository, NavigationRepository navigationRepository) {
        this.buildingRepository = buildingRepository;
        this.navigationRepository = navigationRepository;
    }

    public Map<String, Room> getRoomGraph(int buildingId) {
        //Alle Räume aus gleichem Gebäude holen, refactored um repository zu verwenden
        List<Room> rooms = buildingRepository.findAllRoomsByBuildingId(buildingId);
        List<Navigator> allConnections = navigationRepository.findAllConnections();
        Room from,to;

        //Map für schnellen Zugriff erstellen und alle Objekte ablegen
        Map<String, Room> roomMap = new HashMap<>();
        for (Room room : rooms) {
            roomMap.put(room.getName(), room);
        }
        //Hier selbe Logik wie mit der jdbcQuery, nur das wir alle Connections aus einer Liste durchgehen.
        for(Navigator nav : allConnections){
            from = roomMap.get(nav.getFromName());
            to = roomMap.get(nav.getToName());
            if(from != null && !from.getNeighbours().contains(to)){
                from.addNeighbour(to);
            }
            if(to != null && !to.getNeighbours().contains(from)){
                to.addNeighbour(from);
            }
        }
        return roomMap;
    }

    //Aufruf mit buildingId in aktueller Iteration redundant, da dieser Wert im Aufruf fest auf 1 ist. Bei Hinzufügen von weiteren Gebäuden in der Datenbank müsste dies überarbeitet werden.
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
            for (Room neighbour : current.getNeighbours()) {
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
        String floorToEntrance = "Der %s ist vom Ende von %s aus sichtbar.";
        String floorToMainEntrance = "Der Haupteingang befindet sich hinter der Holztür auf der linken Seite.";
        String stairwayToMainEntrance = "Biege am Ende von %s nach links ab. Du solltest den %s nun sehen können.";
        String stairwayToFloor = "Biege am Ende von %s nach s% ab und folge dem %s.";

        List<String> result = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {

            //Potenzielle Kantentypen
            //Startraum → Flur
            if (path.get(i).getRoomType() == RoomType.ROOM && path.get(i + 1).getRoomType() == RoomType.FLOOR) {
                if (path.getLast().getId() > path.get(i).getId() && path.get(i).isOnRightSide()) {
                    result.add(String.format(roomToFloor, path.get(i).getName(), "rechts"));
                }
                else if (path.getLast().getId() < path.get(i).getId() && path.get(i).isOnRightSide()) {
                    result.add(String.format(roomToFloor, path.get(i).getName(), "links"));
                }
                else if (path.getLast().getId() < path.get(i).getId() && path.get(i).isOnLeftSide()) {
                    if (path.getLast().getName().equals("Eingang E57")) {
                        result.add(String.format(roomToFloor, path.get(i).getName(), "rechts"));
                    } else {
                        result.add(String.format(roomToFloor, path.get(i).getName(), "links"));
                    }
                } else {
                    if (path.getLast().getName().equals("Eingang E57")) {
                        result.add(String.format(roomToFloor, path.get(i).getName(), "links"));
                    } else {
                        result.add(String.format(roomToFloor, path.get(i).getName(), "rechts"));
                    }
                }
            }
            //Flur → Flur
            if (path.get(i).getRoomType() == RoomType.FLOOR && path.get(i + 1).getRoomType() == RoomType.FLOOR) {
                if ((path.get(i).getName().equals(("Flur E27 - E57")))) {
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
                    result.add(String.format(floorToRoom, path.get(i + 1).getName(), "rechten"));
                } else if (path.getFirst().getId() > path.get(i).getId() && path.get(i).isOnRightSide()) {
                    result.add(String.format(floorToRoom, path.get(i + 1).getName(), "linken"));
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

            //Flur - Eingang
            if(path.get(i).getRoomType() == RoomType.FLOOR && path.get(i + 1).getRoomType() == RoomType.ENTRANCE) {
                if(path.get(i + 1).getName().equals("Haupteingang")) {
                    result.add(String.format(floorToMainEntrance));
                } else {
                    result.add(String.format(floorToEntrance, path.get(i + 1).getName(), path.get(i).getName()));
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
