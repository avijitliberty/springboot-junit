package host.honeycomb.room.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import host.honeycomb.room.model.Room;
import host.honeycomb.room.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {
	private RoomService service;

	public RoomController(RoomService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Iterable<Room>> getAllRooms() {
		try {
			return ResponseEntity.ok().location((new URI("/rooms"))).body(service.getAllRooms());
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findRoomById(@PathVariable long id) {
		return service.findRoom(id).map(room -> {
			try {
				return ResponseEntity.ok().location(new URI("/rooms/" + room.getId())).body(room);
			} catch (URISyntaxException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search/byRoomNumber")
	public ResponseEntity<?> findRoomByRoomNumber(
			@RequestParam(value = "roomNumber", required = false) String roomNumber) {
		return service.findByRoomNumber(roomNumber).map(room -> {
			try {
				return ResponseEntity.ok().location(new URI("/rooms/" + room.getId())).body(room);
			} catch (URISyntaxException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> addRoom(Room room) {
		Room newRoom = service.addRoom(room);

		try {
			return ResponseEntity.created(new URI("/rooms/" + newRoom.getId())).body(newRoom);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateRoom(@RequestBody Room room, @PathVariable long id) {
		// Get the room with the specified id
		Optional<Room> existingRoom = service.findRoom(id);
		if (!existingRoom.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		// Update the room
		room.setId(id);
		room = service.updateRoom(room);

		try {
			// Return a 200 response with the updated room
			return ResponseEntity.ok().location(new URI("/rooms/" + room.getId())).body(room);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
