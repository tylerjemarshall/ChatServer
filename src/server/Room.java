package server;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;





public class Room implements RoomInterface, Comparable<Room>{

    protected ArrayList<ConnectionToClient> list;

    
    private int limit = DEFAULT_LIMIT;
    private String name = "commons";
    //private int id;
    private boolean isFull = true;
    private ConnectionToClient owner;
    private char[] password;
    private boolean reserved = false;
    
	/**
     * Class that acts as a container for the list ClientInfo.
     * Will have a limit to clients in a room and custom attributes.
     */
    public Room(){
        list = new ArrayList<ConnectionToClient>();
    }
    
//    public void setPassword(String password) {
//		
//		
//    }
    
    public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public ConnectionToClient getOwner() {
		return owner;
	}



	public void setOwner(ConnectionToClient owner) {
		this.owner = owner;
	}



	public int getClientIndex(int itemName) {
		for (int i = 0; i < list.size(); i++) {
			ClientInfo client = list.get(i).getClientInfo();
			if (itemName == (Integer) client.getId()) {
				return i;
			}
		}

		return -1;
	}
    
    public ConnectionToClient getClientById(int id)
    {
	
		for (int y = 0; y < list.size(); y++) {
			ConnectionToClient tempClient = list.get(y);
			if (tempClient.getId() == id) {
				return tempClient;
			}
		}
		return null;
    }
    
    
    
    public ConnectionToClient getClientByName(String name)
    {
    	name=name.toLowerCase();
    	for (int y = 0; y < list.size(); y++) {
			ConnectionToClient tempClient = list.get(y);
			if (tempClient.getClientInfo().getUserName().toLowerCase().equals(name)) {
				return tempClient;
			}
		}
    	return null;
    }
    
//    public ClientInfo getInfoByClient(ConnectionToClient client)
//    {
//    	for (int y = 0; y < list.size(); y++) {
//			ClientInfo tempClient = list.get(y).getClientInfo();
//			if (tempClient.getClient().equals(client)) {
//				return tempClient;
//			}
//		}
//    	return null;
//    	
//    }
//	
	//Basic Getters and Setters for custom variables.
	public String getName() {
		return name;
	}


	public void setName(String name) {
		for (int i = 0; i < list.size(); i++) {
			ClientInfo client = list.get(i).getClientInfo();
			client.setRoom(name);
		}
		
		this.name = name;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

//	public int getId() {
//		return id;
//	}
//
//
//	public void setId(int id) {
//		this.id = id;
//	}


	/**
	 * If the Room is accepting new clients, true. Else is closed and sending new clients to their default room.
	 * @return Returns true if Room is open. False if closed.
	 */
	public boolean isFull() {
		return isFull;
	}



//	public void setOpen(boolean open) {
//		this.isFull = open;
//	}



	public String toString()
	{
		return name + "(" + list.size()+"/"+getLimit()+")";
	}
	
	
    public String[] toStringArray()
    {
    	Collections.sort(list);
    	String[] array = new String[this.size()];
    	for (int y = 0; y < list.size(); y++) 
    	{
    		array[y] = list.get(y).toString();
    	}
		return array;
    	
    }
    
    public ClientInfo[] toClientInfoArray()
    {
    	ClientInfo[] clientList = new ClientInfo[this.size()];
    	for (int x = 0; x < this.size(); x++)
    	{
    		clientList[x] = this.get(x).getClientInfo();
    	}
    	return clientList;
    }
	
	
	//From Comparable, since I have a list of these rooms.

	
   @Override
    public int compareTo(Room o) {
        return this.getName().compareTo(o.getName());
    }   

	@Override
	public boolean equals(Object o)    {

	       if(this == o) return true;
		      
	       	
	       if(o == null || (this.getClass() != o.getClass())){
	           return false;
	       }
	       
	     //use instanceof instead of getClass here for two reasons
	       //1. if need be, it can match any supertype, and not just one class;
	       //2. it renders an explict check for "that == null" redundant, since
	       //it does the check for null already - "null instanceof [type]" always
	       //returns false. (See Effective Java by Joshua Bloch.)
	       if ( !(o instanceof ConnectionToClient) ) return false;
	       
	       ConnectionToClient guest = (ConnectionToClient) o;
	       return (this.name == guest.getName()); //&&
	       
	       
	}
	
    @Override
   public int hashCode(){
       //int result = 0;
       //result = 31*result + id;
       //result = 31*result + (name !=null ? name.hashCode() : 0);
      
       return (name !=null ? name.hashCode() : 0);
   }

    //From List. If i want to implement more List functions, need to add here.

    public Iterator<ConnectionToClient> getIter(){
        return list.iterator();
    }

	@Override
	public boolean add(ConnectionToClient e) {
		boolean b = list.add(e);
		isFull = (list.size()>limit) ? false : true;
		return b;
	}

	@Override
	public void add(int index, ConnectionToClient element) {
		list.add(index, element);
		isFull = (list.size()>limit) ? false : true;
	}

	@Override
	public boolean addAll(Collection<? extends ConnectionToClient> c) {
		boolean b = list.addAll(c);
		isFull = (list.size()>limit) ? false : true;
		return b;
	}

	@Override
	public boolean addAll(int index, Collection<? extends ConnectionToClient> c) {
		boolean b = list.addAll(index, c);
		isFull = (list.size()>limit) ? false : true;
		return b;
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public ConnectionToClient get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<ConnectionToClient> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<ConnectionToClient> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<ConnectionToClient> listIterator(int index) {
		return list.listIterator(index);
	}


	@Override
	public boolean remove(Object o) {
		boolean b = list.remove(o);
		isFull = (list.size()>limit) ? false : true;
		return b;
	}

	@Override
	public ConnectionToClient remove(int index) {
		ConnectionToClient client = list.remove(index);
		isFull = (list.size()>limit) ? false : true;
		return client;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean b = removeAll(c);
		isFull = (list.size()>limit) ? false : true;
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public ConnectionToClient set(int index, ConnectionToClient element) {
		return list.set(index, element);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<ConnectionToClient> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}



	

}