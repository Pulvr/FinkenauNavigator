package com.example.FinkenauNavigator.building;

import com.example.FinkenauNavigator.room.Room;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends ListCrudRepository<Building, Integer> {

    @Query("""
            SELECT ROOM.NAME,ROOM.FLOOR
            FROM ROOM
            JOIN BUILDING ON ROOM.BUILDING_ID = BUILDING.ID
            WHERE ROOM.SELECTABLE AND BUILDING.ID = (:id)
            """)
    List<Room> findAllSelectableRoomsWithNameFloorByBuildingId(int id);

    //allgemeinere Query, um alle Parameter inkl. Type zu erhalten
    @Query("""
            SELECT *
            FROM ROOM
            WHERE BUILDING_ID = :id
            """)
    List<Room> findAllRoomsByBuildingId(int id);

    //Query für die Übergabe der Koordinaten
    @Query("SELECT X_COORDINATE FROM ROOM WHERE ROOM.NAME = :room")
    double getXCoordinate(String room);

    @Query("SELECT Y_COORDINATE FROM ROOM WHERE ROOM.NAME = :room")
    double getYCoordinate(String room);
}