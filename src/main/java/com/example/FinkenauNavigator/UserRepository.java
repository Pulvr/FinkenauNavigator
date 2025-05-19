package com.example.FinkenauNavigator;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface UserRepository extends ListCrudRepository<UserAccounts, Long> {

    @Query("SELECT * FROM USERACCOUNTS WHERE LOWER(name) = LOWER(:name)")
    List<UserAccounts> findByName(String name);

}
