package host.honeycomb.room.service;

import java.util.List;
import java.util.Optional;

import host.honeycomb.room.model.Room;

public interface RoomService {

	Iterable<Room> getAllRooms();

	Optional<Room> findRoom(long roomId);

	Room updateRoom(Room room);

	Room addRoom(Room room);

	Optional<Room> findByRoomNumber(String string);

	List<Room> findRoomsByFloor(String floorNumber);

}
