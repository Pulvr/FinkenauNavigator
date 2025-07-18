package com.example.FinkenauNavigator;

import com.example.FinkenauNavigator.building.Building;
import com.example.FinkenauNavigator.building.BuildingRepository;
import com.example.FinkenauNavigator.room.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinkenauNavigatorApplicationTests {

    @Autowired
    private BuildingRepository buildingRepository;

	@Test
	void assertIdEmptyWhenUsingNameFloor(){
		Building myBuilding = new Building("Finkenau",buildingRepository.findAllSelectableRoomsWithNameFloorByBuildingId(1));
		int actualSumOfIds = 0;
		List<Room> myBuildingRooms = myBuilding.getRooms();

		for(Room room : myBuildingRooms){
			actualSumOfIds += room.getId();
		}
        assertEquals(0, actualSumOfIds);
	}

}