package com.example.FinkenauNavigator.classes;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

enum RoomType {ROOM, FLOOR, STAIRWAY, ENTRANCE}

@Table("ROOM")
public class Room {
    @Id
    private int id;
    private int buildingId;
    private boolean selectable;
    private String name;
    private String floor;
    private RoomType type;

    public List<Room> neighbours = new ArrayList<Room>();

    public Room(){
        
    }
    
    public Room(int buildingId, String name,String floor, RoomType type){
        this.buildingId = buildingId;
        this.name = name;
        this.floor = floor;
        this.type = type;
    }

    public int getId() { return id; }

    public String getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

    public void addNeighbour(Room neighbourRoom) {
        neighbours.add(neighbourRoom);
    }

    @Override
    public String toString(){
        return "Room "+ this.getName()+ " on Floor " + this.getFloor();
    }
}
