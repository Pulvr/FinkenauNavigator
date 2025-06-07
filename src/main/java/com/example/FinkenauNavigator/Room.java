package com.example.FinkenauNavigator;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ROOM")
public class Room {
    @Id
    private long id;
    private int buildingId;
    private String name;
    private String floor;
    
    public Room(){
        
    }
    
    public Room(int buildingId, String name,String floor){
        this.buildingId = buildingId;
        this.name = name;
        this.floor = floor;
    }
}
