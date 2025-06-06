package com.example.FinkenauNavigator;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BuildingRepository extends ListCrudRepository<Building, Integer> {

    @Query("SELECT * FROM BUILDING WHERE LOWER(name) = LOWER(:name)")
    List<Building> findRoomByName(String name);

}
