package server;


/**
 * This class takes in ConnectionToClient to be able to handle the HashMap of ConnectionToClient and other variables.
 * 
 * @author Tyler M
 */
public class ClientInfo implements Comparable<ClientInfo> {

	
	
	private String userName = "User";
	private int id = 0;
	private String room = "commons";
	
	private boolean yellable = false;
	
	

	private ConnectionToClient client = null;
	
	
	public ConnectionToClient getClient() {
		return client;
	}

	public void setClient(ConnectionToClient client) {
		this.client = client;
	}

	public String getUserName() {
		userName=client.getInfo("userName").toString();
		return userName;
	}

	public void setUserName(String userName) {
		client.setInfo("userName", userName);
		this.userName = userName;
	}

	public int getId() {
		id=(Integer)client.getInfo("id");
		return id;
	}

	public void setId(int id) {
		client.setInfo("id", id);
		this.id = id;
	}

	public String getRoom() {
		return this.room;
	}

	public void setRoom(String room) {
		client.setInfo("room", room);
		this.room = room;
	}
	
	public String toString()
	{
		return getUserName() + "#" + getId() + " [" + getRoom()+ "]";
	}
	
	public int toInt()
	{
		return getId();
	}
	
	
	/**
	 * This class takes in ConnectionToClient to be able to handle the HashMap of ConnectionToClient and other variables.

	 * @param client Uses ConnectionToClient to pull information from the client.
	 */
	public ClientInfo(ConnectionToClient client) {
		this.client = client;
		this.userName = getUserName();
		this.room = getRoom();
	}
	/**
	 * This class takes in ConnectionToClient and the client's info
	 * to be overwritten to be able to handle the HashMap of ConnectionToClient and other variables.

	 * @param client Uses ConnectionToClient to pull information from the client.
	 * @param userName Declares the userName for client
	 * @param room Declares the room for client
	 */
	public ClientInfo(ConnectionToClient client, String userName, int id, String room) {
		client.setInfo("userName", userName);
		client.setInfo("id", id);
		client.setInfo("room", room);
		this.client = client;
		this.userName = userName;
		this.room = room;
	}
	



	
   @Override
    public int compareTo(ClientInfo o) {
        return this.getId() - o.getId();
    }   

	@Override
	public boolean equals(Object o)    {

	       if(this == o) return true;
		      
	       if(o == null || (this.getClass() != o.getClass())){
	           return false;
	       }
	       
	       ClientInfo guest = (ClientInfo) o;
	       return (this.getId() == guest.getId()); //&&

	}
	
    @Override
   public int hashCode(){
       int result = 0;
       result = 31*result + id;
       result = 31*result + (userName !=null ? userName.hashCode() : 0);
       result = 31*result + (room  !=null ? room.hashCode() : 0);
      
       return result;
   }

	public boolean isYellable() {
		return yellable;
	}

	public void setYellable(boolean yellable) {
		this.yellable = yellable;
	}
	


	

}
