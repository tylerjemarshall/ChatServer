package server;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;





public class RoomList implements RoomListInterface{//Comparable<RoomList>

	private int maxClients = DEFAULT_MAX_CLIENTS;
	private int clientCount = 0;
	private String defaultRoom = "commons";
	
	
	
    protected ArrayList<Room> list;

    /**
     * Class that acts as a container for the list Room.
     * Will be able to pull specific information from the RoomList
     */
    public RoomList(){
        list = new ArrayList<Room>();
    }
    
    
    public String toString()
    {
    	return list.toString();
    }
    
    public int getClientCount() {
    	countClients();
		return clientCount;
	}
    
    public boolean roomOpen(String room)
    {
    	try
    	{
    		Room tempRoom = getRoom(room);
    		return tempRoom.isOpen();
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    }
    
    

//	public boolean roomIsJoinable(Room room) {
//		boolean a = false;
//		boolean b = false;
//		try {
//			a = room.isOpen();
//		} catch (Exception e) {
//			e.printStackTrace();
//			a = false;
//		}
//		try {
//			b = room.size() < room.getLimit();
//		} catch (Exception e) {
//			b = false;
//		}
//		return a && b;
//
//	}

//	public boolean roomIsJoinable(String room) {
//		try
//		{
//			Room tempRoom = getRoom(room.toLowerCase());
//			return roomIsJoinable(tempRoom);
//		}
//		catch (Exception e){return false;}	
//	}
    
    public String getDefaultRoom() {
		return defaultRoom;
	}


	public void setDefaultRoom(String defaultRoom) {
		this.defaultRoom = defaultRoom;
	}


	public Room getRoom(String room)
    {
		for (int y = 0; y < list.size(); y++) 
		{
			Room tempRoom = list.get(y);
			try{
			if (tempRoom.getName().toLowerCase().equals(room.toLowerCase()))
			{
				return tempRoom;
			}
				}
			catch(Exception e){}
		}
		return null;
    }
    
	/**
	 * Method that is private, to count the sum of all clients in all room lists.
	 */
    private void countClients()
    {
    	int count = 0;
    	for (int y = 0; y < list.size(); y++)
    	{
    		count+=list.get(y).size();
    	}
    	this.clientCount = count;
    }

	/**
	 * Method that finds a client by their ID
	 * Returns null if not found
	 * 
	 * @param itemName The ID that is being searched for.
	 * @return ClientInfo client if found, else null
	 */
	public ClientInfo getInfoById(int id)
    {
	
		for (int y = 0; y < list.size(); y++) 
		{
			Room tempRoom = list.get(y);
			ClientInfo tempClient = tempRoom.getClientInfoById(id);
			
			try {
				if (tempClient.getId() == id)			
					return tempClient;
			} catch(Exception e){}
			
		}
		return null;
    }
	/**
	 * Method that finds a client by their ConnectionToClient
	 * Returns null if not found
	 * 
	 * @param itemName The ID that is being searched for.
	 * @return ClientInfo client if found, else null
	 */
	public ClientInfo getInfoByClient(ConnectionToClient client)
	{
		for (int y = 0; y < list.size(); y++) 
		{
			Room tempRoom = list.get(y);
			ClientInfo tempClient = tempRoom.getInfoByClient(client);
			try {
				if (tempClient.getClient() == client)
					return tempClient;	
			} catch (Exception e){}
		}
		return null;
		
	}
    
	
	
    /**
     * Returns true if this list contains the specified element.
     * More formally, returns true if and only if this list contains 
     * at least one element e such that (o==null ? e==null : o.equals(e)).
     * @param name
     * @return
     */
    public boolean contains(String name)
    {
    	name.toLowerCase();
	
		for (int y = 0; y < list.size(); y++) 
		{
			Room tempRoom = list.get(y);
			ClientInfo tempClient = tempRoom.getClientInfoByName(name);
			
			if (tempClient.getUserName().toLowerCase().equals(name))
			{
				return true;
			}
		}
		return false;
    }
    
    /**
	 * Adds a client to a room. a client is a list item, that will be added to the list rooms.
	 * This method checks to see if rooms exist, if room already exists it adds client to existing list
	 * Else creates new list.
	 * @param clientInfo Client List Item being added to room.
	 * @param room The room you would like the client to join, referenced by room name
	 */
	public boolean add(ClientInfo clientInfo, String room) {
		room = room.toLowerCase();
		boolean foundRoom = false;
		for (int i = 0; i < list.size(); i++) {
			Room currentRoom = list.get(i);
			if (currentRoom.getName().toLowerCase().equals(room)) {
				clientInfo.setRoom(room);
				foundRoom = true;
				currentRoom.add(clientInfo);
				System.out.println("Added client " + clientInfo + " to room "
					+ currentRoom);
				
				
				
				if (currentRoom.isEmpty()) {
					list.remove(currentRoom);
					System.out.println("Removed empty room.");
				}
				
//				
//				if (!roomOpen(room))
//				{
//					moveClient(clientInfo, "commons");
//				}
				
				
				return true;
			}
		}
		if (!foundRoom) {
			System.out.println("Couldn't find room, creating new room");
			Room newRoom = new Room();
			newRoom.setName(room);
			if (room.equals(defaultRoom)) newRoom.setLimit(200);
			clientInfo.setRoom(room);
			newRoom.add(clientInfo);
			list.add(newRoom);
			return true;
		}
		return false;
		
		
		
	}
	
	
	
	
	/**
	 * 
	 * @param info
	 * @param room
	 * @return Returns false when unable to move client from either Client Doesn't Exist or Room is Closed.
	 */
	public boolean moveClient(ClientInfo client, String room)
	{
		return (remove(client)) ? add(client, room) : false;
	}
	
	public boolean remove(ClientInfo clientInfo) {
	
		String room = clientInfo.getRoom().toLowerCase();
		
		for (int i = 0; i < list.size(); i++) {
			Room currentRoom = list.get(i);
			if (currentRoom.getName().toLowerCase().equals(room)) {
				
				boolean exit = currentRoom.remove(clientInfo);
				
				if (exit)
				System.out.println("Removed client " + clientInfo + " from room "
						+ currentRoom);

				if (currentRoom.isEmpty()) {
					list.remove(currentRoom);
					System.out.println("Removed empty room.");
				}
				return exit;

			}
		}
		return false;
	
	

	}
	
    public int getMaxClients() {
		return maxClients;
	}

	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}

	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	public Iterator<Room> getIter(){
        return list.iterator();
    }

	@Override
	public boolean add(Room e) {
		return list.add(e);
	}


	@Override
	public void add(int index, Room element) {
		list.add(index, element);
	}


	@Override
	public List<Room> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
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
	public Room get(int index) {
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
	public Iterator<Room> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<Room> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Room> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public Room remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public Room set(int index, Room element) {
		return list.set(index, element);
	}

	@Override
	public int size() {
		return list.size();
	}


	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	
	
	@Override
	public boolean addAll(Collection<? extends Room> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Room> c) {
		return list.addAll(index, c);
	}
}