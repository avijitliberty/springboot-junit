package host.honeycomb.room.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "rooms")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "room_number")
	private String roomNumber;
	@Column(name = "weekday_price")
	private double weekdayPrice;
	@Column(name = "weekend_price")
	private double weekendPrice;
	@Column(name = "room_type")
	private String roomType;
	@Column(name = "floor")
	private String floor;

	public Room() {
		super();
	}

	@JsonCreator
	public Room(@JsonProperty(value = "id", defaultValue = "0L") long id,
			@JsonProperty(value = "roomNumber", required = true) String roomNumber,
			@JsonProperty(value = "weekdayPrice", required = true) double weekdayPrice,
			@JsonProperty(value = "weekendPrice", required = true) double weekendPrice,
			@JsonProperty(value = "roomType", required = true) String roomType,
			@JsonProperty(value = "floor", required = true) String floor) {
		this.id = id;
		this.roomNumber = roomNumber;
		this.weekdayPrice = weekdayPrice;
		this.weekendPrice = weekendPrice;
		this.roomType = roomType;
		this.floor = floor;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public double getWeekdayPrice() {
		return weekdayPrice;
	}

	public void setWeekdayPrice(double weekdayPrice) {
		this.weekdayPrice = weekdayPrice;
	}

	public double getWeekendPrice() {
		return weekendPrice;
	}

	public void setWeekendPrice(double weekendPrice) {
		this.weekendPrice = weekendPrice;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
}
