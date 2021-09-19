package host.honeycomb.room.service.impl;

import java.util.List;
import java.util.Optional;

import org.h2.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import host.honeycomb.room.exception.RoomServiceClientException;
import host.honeycomb.room.model.Room;
import host.honeycomb.room.repository.RoomRepo;
import host.honeycomb.room.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	private RoomRepo repo;
	private RestTemplate restTemplate;

	public RoomServiceImpl(RoomRepo repo, RestTemplate restTemplate) {
		this.repo = repo;
		this.restTemplate = restTemplate;
	}

	@Override
	public Iterable<Room> getAllRooms() {
		return repo.findAll();
	}

	@Override
	public Optional<Room> findRoom(long roomId) {
		return repo.findById(roomId);
	}

	@Override
	public Room updateRoom(Room room) {
		return repo.save(room);
	}

	@Override
	public Room addRoom(Room room) {
		return repo.save(room);
	}

	@Override
	public Optional<Room> findByRoomNumber(String roomNumber) {
		if (!StringUtils.isNullOrEmpty(roomNumber) && StringUtils.isNumber(roomNumber)) {
			Optional<Room> room = repo.findByRoomNumber(roomNumber);
			if (room == null) {
				throw new RoomServiceClientException("Room number: " + roomNumber + ", does not exist.");
			}
			return room;
		} else {
			throw new RoomServiceClientException("Room number: " + roomNumber + ", is an invalid room number format.");
		}
	}

	@Override
	public List<Room> findRoomsByFloor(String floorNumber) {
		return repo.findRoomsByFloor(floorNumber);
	}

}
