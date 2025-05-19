package com.example.FinkenauNavigator;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("USERACCOUNTS")
public class UserAccounts {
    @Id
    private long id;

    private String name;

    private String passw;

    public UserAccounts() {

    }

    public UserAccounts(String name, String passw) {
        this.name = name;
        this.passw = passw;
    }

}
