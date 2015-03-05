package client;

import java.io.Serializable;

public class RoomInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String room;
	private int limit;
	private Boolean open;
	private String password;

	public RoomInfo ( String room, int limit, Boolean open, String password ){
		
		this.room = room;
		this.limit = limit;
		this.open = open;
		this.password = password;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean b) {
		open = b;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
