package com.example.FinkenauNavigator.navigation;


import com.example.FinkenauNavigator.room.Room;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

/**
 * Navigator soll auch so wie ein Navigationsergebnis sein.
 * Beinhaltet auch Informationen über die Verbindung der Räume.
 */
@Table("NAVIGATOR")
public class Navigator {
    @Id
    int id;
    String fromName;
    String toName;

    @Transient
    List<Room> path;

    Navigator() {

    }

    public Navigator(String fromName, String toName){
        this.fromName = fromName;
        this.toName = toName;
    }

    public String getFromName(){
        return this.fromName;
    }

    public String getToName(){
        return this.toName;
    }

    public List<Room> getPath() {
        return path;
    }

    public void setPath(List<Room> path) {
        this.path = path;
    }

}
