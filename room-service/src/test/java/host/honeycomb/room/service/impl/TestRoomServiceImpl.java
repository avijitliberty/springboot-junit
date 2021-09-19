package host.honeycomb.room.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import host.honeycomb.room.model.Room;
import host.honeycomb.room.repository.RoomRepo;
import host.honeycomb.room.service.RoomService;

public class TestRoomServiceImpl {

	/*
	 * Here we test the Happy Path that RoomServiceImpl.findByRoomNumber() works as
	 * expected. We mock the RoomRepo.findByRoomNumber() method to return an empty
	 * room given any string as input and assert that the room is NotNull.
	 */

	@Test
	public void lookupExistingRoom() {
		RoomRepo mockRepo = mock(RoomRepo.class);
		when(mockRepo.findByRoomNumber(anyString())).thenReturn(Optional.of(new Room()));
		RoomService service = new RoomServiceImpl(mockRepo, null);

		Optional<Room> room = service.findByRoomNumber("100");

		assertNotNull(room);
	}

	/*
	 * Here we test exception is thrown if there was no room found for a given
	 * roomNumber. We mock the RoomRepo.findByRoomNumber() method to return null
	 * given any string as input and assert the expected Exception is thrown.
	 */
	@Test
	public void throwExceptionForNonExistingRoom() {
		RoomRepo mockRepo = mock(RoomRepo.class);
		when(mockRepo.findByRoomNumber(anyString())).thenReturn(null);
		RoomService service = new RoomServiceImpl(mockRepo, null);
		try {
			service.findByRoomNumber("100");
			fail("Exception should had been thrown");
		} catch (Exception e) {
			assertEquals("Room number: 100, does not exist.", e.getMessage());
		}
	}

	/*
	 * Here we test exception is thrown given a malformed roomNumber. Given any
	 * malformed string as input we assert that the expected Exception is thrown.
	 */
	@Test
	public void throwExceptionInvalidRoomNumberFormat() {
		RoomRepo mockRepo = mock(RoomRepo.class);
		RoomService service = new RoomServiceImpl(mockRepo, null);
		try {
			service.findByRoomNumber("BAD ROOM NUMBER!");
			fail("Exception should had been thrown");
		} catch (Exception e) {
			assertEquals("Room number: BAD ROOM NUMBER!, is an invalid room number format.", e.getMessage());
		}
	}

	/*
	 * Here we test exception is thrown given a null roomNumber. Given null input we
	 * assert that the expected Exception is thrown.
	 */
	@Test
	public void throwExceptionInvalidRoomNumberNull() {
		RoomRepo mockRepo = mock(RoomRepo.class);
		RoomService service = new RoomServiceImpl(mockRepo, null);
		try {
			service.findByRoomNumber(null);
			fail("Exception should had been thrown");
		} catch (Exception e) {
			assertEquals("Room number: null, is an invalid room number format.", e.getMessage());
		}
	}

	/*
	 * Here we test exception is thrown given a null roomNumber. Given -ve number
	 * input we assert that the expected Exception is thrown.
	 */
	@Test
	public void throwExceptionInvalidRoomNumberNegative() {
		RoomRepo mockRepo = mock(RoomRepo.class);
		RoomService service = new RoomServiceImpl(mockRepo, null);
		try {
			service.findByRoomNumber("-100");
			fail("Exception should had been thrown");
		} catch (Exception e) {
			assertEquals("Room number: -100, is an invalid room number format.", e.getMessage());
		}
	}

	@Test
	public void testFindByValidRoomNumber() {

		//
		// Given
		//
		RoomRepo roomRepo = mock(RoomRepo.class);
		Room room = new Room();
		room.setRoomNumber("1023");
		room.setFloor("10");
		room.setRoomType("Double");
		room.setWeekdayPrice(150.99);
		room.setWeekendPrice(180.99);
		when(roomRepo.findByRoomNumber("1023")).thenReturn(Optional.of(room));
		RoomService service = new RoomServiceImpl(roomRepo, null);

		//
		// When
		//
		Optional<Room> returnedRoom = service.findByRoomNumber("1023");

		//
		// Then
		//
		assertAll(() -> assertThat(returnedRoom.get().getRoomNumber()).isEqualTo("1023"),
				() -> assertThat(returnedRoom.get().getFloor()).isEqualTo("10"),
				() -> assertThat(returnedRoom.get().getRoomType()).isEqualTo("Double"),
				() -> assertThat(returnedRoom.get().getWeekdayPrice()).isEqualTo(150.99),
				() -> assertThat(returnedRoom.get().getWeekendPrice()).isEqualTo(180.99));
	}

	@Test
	@DisplayName("Testing room lookup by floor")
	public void testRoomsByFloor() {
		//
		// Given
		//
		RoomRepo roomRepo = mock(RoomRepo.class);
		Room room1 = new Room();
		room1.setRoomNumber("1023");
		room1.setFloor("10");
		room1.setRoomType("Double");
		room1.setWeekdayPrice(150.99);
		room1.setWeekendPrice(180.99);
		Room room2 = new Room();
		room2.setRoomNumber("1024");
		room2.setFloor("10");
		room2.setRoomType("Single");
		room2.setWeekdayPrice(100.99);
		room2.setWeekendPrice(120.99);
		Room room3 = new Room();
		room3.setRoomNumber("1025");
		room3.setFloor("10");
		room3.setRoomType("Suite");
		room3.setWeekdayPrice(200.99);
		room3.setWeekendPrice(250.99);
		List<Room> rooms = Arrays.asList(room1, room2, room3);
		when(roomRepo.findRoomsByFloor("10")).thenReturn(rooms);
		RoomService service = new RoomServiceImpl(roomRepo, null);

		//
		// When
		//
		List<Room> returnedRooms = service.findRoomsByFloor("10");

		//
		// Then
		//
		assertThat(returnedRooms).extracting("roomType", "roomNumber").containsExactly(tuple("Double", "1023"),
				tuple("Single", "1024"), tuple("Suite", "1025"));
	}

}
