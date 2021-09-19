package host.honeycomb.room.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import host.honeycomb.room.model.Room;
import host.honeycomb.room.service.RoomService;

@WebMvcTest(controllers = RoomController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@DirtiesContext
class TestRoomController {

	@MockBean
	private RoomService service;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("GET /rooms - success")
	void testGetAllRooms() throws Exception {
		Room room1 = new Room(1L, "202", 102.00, 135.00, "double", "2");
		Room room2 = new Room(2L, "302", 112.00, 155.00, "deluxe", "3");
		when(service.getAllRooms()).thenReturn(Arrays.asList(room1, room2));
		mockMvc.perform(get("/rooms")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2)))
				.andDo(document("rooms",
						responseFields(fieldWithPath("[].id").description("The room's Id"),
								fieldWithPath("[].roomNumber").description("The roomNumber"),
								fieldWithPath("[].weekdayPrice").description("The weekdayPrice"),
								fieldWithPath("[].weekendPrice").description("The weekendPrice"),
								fieldWithPath("[].roomType").description("The roomType"),
								fieldWithPath("[].floor").description("The floor"))));
	}

	@Test
	@DisplayName("GET /rooms/1")
	void testFindRoomById() throws Exception {

		Room room = new Room(1L, "202", 102.00, 135.00, "double", "2");
		doReturn(Optional.of(room)).when(service).findRoom(1l);

		// Execute the GET request
		mockMvc.perform(get("/rooms/{id}", 1L))
				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.roomNumber", is("202")))
				.andExpect(jsonPath("$.roomType", is("double"))).andExpect(jsonPath("$.floor", is("2")));
	}

	@Test
	@DisplayName("GET /rooms/1 - Not Found")
	void testFindRoomByIdNotFound() throws Exception {
		// Setup our mocked service
		doReturn(Optional.empty()).when(service).findRoom(1l);

		// Execute the GET request
		mockMvc.perform(get("/rooms/{id}", 1L))
				// Validate the response code
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("POST /rooms")
	void testAddRoom() throws Exception {
		// Setup our mocked service
		Room roomToPost = new Room(1L, "202", 102.00, 135.00, "double", "2");
		Room roomToReturn = new Room(1L, "202", 102.00, 135.00, "double", "2");
		doReturn(roomToReturn).when(service).addRoom(any());

		// Execute the POST request
		mockMvc.perform(post("/rooms").contentType(MediaType.APPLICATION_JSON).content(asJsonString(roomToPost)))

				// Validate the response code and content type
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate headers
				.andExpect(header().string(HttpHeaders.LOCATION, "/rooms/1"))

				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.roomNumber", is("202")))
				.andExpect(jsonPath("$.roomType", is("double"))).andExpect(jsonPath("$.floor", is("2")));
	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@DisplayName("PUT /rooms/1")
	void testUpdateRoom() throws Exception {
		// Setup our mocked service
		Room roomToPut = new Room(1L, "202", 102.00, 135.00, "double", "2");
		Room roomToReturnFindBy = new Room(1L, "202", 102.00, 135.00, "double", "2");
		Room roomToReturnSave = new Room(1L, "202", 102.00, 135.00, "single", "2");
		doReturn(Optional.of(roomToReturnFindBy)).when(service).findRoom(1L);
		doReturn(roomToReturnSave).when(service).updateRoom(any());

		// Execute the POST request
		mockMvc.perform(put("/rooms/{id}", 1l).contentType(MediaType.APPLICATION_JSON).content(asJsonString(roomToPut)))

				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate headers
				.andExpect(header().string(HttpHeaders.LOCATION, "/rooms/1"))

				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.roomNumber", is("202")))
				.andExpect(jsonPath("$.roomType", is("single"))).andExpect(jsonPath("$.floor", is("2")));
	}

	@Test
	@DisplayName("PUT /rooms/1 - Not Found")
	void testUpdateRoomNotFound() throws Exception {
		// Setup our mocked service
		Room roomToPut = new Room(1L, "202", 102.00, 135.00, "single", "2");
		doReturn(Optional.empty()).when(service).findRoom(1L);

		// Execute the POST request
		mockMvc.perform(put("/rooms/{id}", 1l).contentType(MediaType.APPLICATION_JSON).content(asJsonString(roomToPut)))

				// Validate the response code and content type
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("GET /search/byRoomNumber")
	void testFindRoomByRoomNumber() throws Exception {
		// LinkedMultiValueMap<String, String> requestParams = new
		// LinkedMultiValueMap<>();
		// requestParams.add("roomNumber", "202");

		Room room = new Room(1L, "202", 102.00, 135.00, "double", "2");
		// doReturn(Optional.of(room)).when(service).findByRoomNumber("202");
		when(service.findByRoomNumber("202")).thenReturn(Optional.of(room));

		// Execute the GET request
		mockMvc.perform(get("/rooms/search/byRoomNumber").param("roomNumber", "202"))
				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.roomNumber", is("202")))
				.andExpect(jsonPath("$.roomType", is("double"))).andExpect(jsonPath("$.floor", is("2")));
	}

}
