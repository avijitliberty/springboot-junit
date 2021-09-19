package host.honeycomb.room.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import host.honeycomb.room.model.Room;

public interface RoomRepo extends CrudRepository<Room, Long> {

	Optional<Room> findByRoomNumber(String anyString);

	List<Room> findRoomsByFloor(String floorNumber);

}