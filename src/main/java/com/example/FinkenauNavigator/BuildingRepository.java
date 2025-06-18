package com.example.FinkenauNavigator;

import com.example.FinkenauNavigator.classes.Building;
import com.example.FinkenauNavigator.classes.Room;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BuildingRepository extends ListCrudRepository<Building, Integer> {

    @Query("SELECT * FROM BUILDING WHERE LOWER(name) = LOWER(:name)")
    List<Building> findRoomByName(String name);

    @Query("""
            SELECT ROOM.ID,ROOM.BUILDING_ID,ROOM.NAME,ROOM.FLOOR,ROOM.TYPE
            FROM ROOM
            JOIN BUILDING ON ROOM.BUILDING_ID = BUILDING.ID
            WHERE ROOM.SELECTABLE AND BUILDING.ID = (:id)
            """)
    List<Room> findAllSelectableRoomsByBuildingId(int id);

    //allgemeinere Query, um alle Parameter inkl. Type zu erhalten
    @Query("""
        SELECT *
        FROM ROOM
        WHERE BUILDING_ID = :id
        """)
    List<Room> findAllRoomsByBuildingId(int id);
}
