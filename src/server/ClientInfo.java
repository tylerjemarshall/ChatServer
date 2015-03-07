package server;
import java.io.Serializable;

/**
 * This class holds clients information, including userName, id, room, and isYellable
 * 
 * @author Tyler M
 */
public class ClientInfo implements Comparable<ClientInfo>, Serializable 
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName = "User";
	private int id = 0;
	private String room = "commons";
	
	private boolean yellable = false;
	
	
	
	/**
	 * This class holds basic info for client including room, id, and userName.
	 * 
	 * @param userName Declares the userName for client
	 * @param room Declares the room for client
	 */
	public ClientInfo(String userName, int id, String room) {
		this.userName = userName;
		this.room = room;
	}
	
	/**
	 * This class holds basic info for client including room, id, and userName.
	 * Sets default values for userName to User, and room to Commons.
	 * 
	 */
	public ClientInfo(){}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoom() {
		return this.room;
	}

	public void setRoom(String room) {
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
	
	
	public boolean isYellable() {
		return yellable;
	}

	public void setYellable(boolean yellable) {
		this.yellable = yellable;
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


	


	

}
