package com.example.FinkenauNavigator.classes;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ROOM")
public class Room {
    @Id
    private int id;
    private int buildingId;
    private boolean selectable;
    private String name;
    private String floor;

    public Room(){
        
    }
    
    public Room(int buildingId, String name,String floor){
        this.buildingId = buildingId;
        this.name = name;
        this.floor = floor;
    }

    public String getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return "Room "+ this.getName()+ " on Floor " + this.getFloor();
    }
}
