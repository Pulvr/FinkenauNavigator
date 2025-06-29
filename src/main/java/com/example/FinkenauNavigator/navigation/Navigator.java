package com.example.FinkenauNavigator.navigation;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("NAVIGATOR")
public class Navigator {
    @Id
    int id;
    String fromName;
    String toName;

    Navigator() {

    }

    Navigator(String fromName, String toName){
        this.fromName = fromName;
        this.toName = toName;
    }

    public String getFromName(){
        return this.fromName;
    }

    public String getToName(){
        return this.toName;
    }
}
