package com.example.FinkenauNavigator.room;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table("ROOM")
public class Room {

    @Id
    private int id;
    private int buildingId;
    private boolean selectable;
    private String name;
    private String floor;
    private RoomType type;
    private boolean onLeftSide;
    private boolean onRightSide;
    private double xCoordinate;
    private double yCoordinate;

    @Transient
    private final List<Room> neighbours = new ArrayList<>();

    public Room(){

    }

    public Room(int buildingId, String name,String floor, RoomType type, boolean onLeftSide, boolean onRightSide){
        this.buildingId = buildingId;
        this.name = name;
        this.floor = floor;
        this.type = type;
        this.onLeftSide = onLeftSide;
        this.onRightSide = onRightSide;
    }

    public int getId() { return id; }

    public String getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

    public RoomType getRoomType() { return type; }

    public boolean isOnLeftSide() { return true; }

    public boolean isOnRightSide() { return true; }

    public void addNeighbour(Room neighbourRoom) {
        neighbours.add(neighbourRoom);
    }

    public List<Room> getNeighbours(){
        return this.neighbours;
    }

    @Override
    public String toString(){
        return  this.getName()+ " on Floor " + this.getFloor();
    }
}

