package com.example.FinkenauNavigator;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BuildingRepository extends ListCrudRepository<Building, Integer> {

    @Query("SELECT * FROM BUILDING WHERE LOWER(name) = LOWER(:name)")
    List<Building> findRoomByName(String name);

    @Query("""
            SELECT ROOM.NAME,ROOM.FLOOR
            FROM ROOM
            JOIN BUILDING ON ROOM.BUILDING_ID = BUILDING.ID
            WHERE ROOM.SELECTABLE AND BUILDING.ID = (:id)
            """)
    List<Room> findAllSelectableRoomsByBuildingId(int id);
}
