package client;

import java.io.Serializable;

public class RoomInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String room;
	private int limit;
	private Boolean reserved;
	private char[] password;

	public RoomInfo ( String room, int limit, Boolean open, char[] password ){
		
		this.room = room;
		this.limit = limit;
		this.reserved = open;
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

	public Boolean isReserved() {
		return reserved;
	}

	public void setReserved(Boolean b) {
		reserved = b;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}
	
	public String toString()
	{
		return this.getRoom();
	}
	

}
