import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;





public class RoomList implements RoomListInterface{//Comparable<RoomList>

	private int maxClients = DEFAULT_MAX_CLIENTS;
	private int clientCount = 0;
	
	
	
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
    
    public Room getRoom(String room)
    {
		for (int y = 0; y < list.size(); y++) 
		{
			Room tempRoom = list.get(y);
			
			
			if (tempRoom.getName().toLowerCase().equals(room.toLowerCase()))			
			return tempRoom;
		}
		return null;
    }
    
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
			
			if (tempClient.getId() == id)			
			return tempClient;
		}
		return null;
    }
	
	public ClientInfo getInfoByClient(ConnectionToClient client)
	{
		for (int y = 0; y < list.size(); y++) 
		{
			Room tempRoom = list.get(y);
			ClientInfo tempClient = tempRoom.getInfoByClient(client);
			
			if (tempClient.getClient() == client)			
			return tempClient;
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
			//ClientInfo tempClient = tempRoom.getClientInfoById(id);
			ClientInfo tempClient = tempRoom.getClientInfoByName(name);
			
			if (tempClient.getUserName().toLowerCase().equals(name))
			{
				System.out.println(true);
				return true;
			}
		}
		System.out.println(false);
		return false;
    }
    
    /**
	 * Adds a client to a room. a client is a list item, that will be added to the list rooms.
	 * This method checks to see if rooms exist, if room already exists it adds client to existing list
	 * Else creates new list.
	 * @param clientInfo Client List Item being added to room.
	 * @param room The room you would like the client to join, referenced by room name
	 */
	public void add(ClientInfo clientInfo, String room) {

		room = room.toLowerCase();
		boolean foundRoom = false;
		for (int i = 0; i < list.size(); i++) {
			Room currentRoom = list.get(i);
			if (currentRoom.getName().toLowerCase().equals(room)) {
				clientInfo.setRoom(room);
				currentRoom.add(clientInfo);
				foundRoom = true;
				System.out.println("Added client " + clientInfo + " to room "
						+ currentRoom);
			}
		}
		if (!foundRoom) {
			System.out.println("Couldn't find room, creating new room");
			Room newRoom = new Room();
			newRoom.setName(room);
			clientInfo.setRoom(room);
			newRoom.add(clientInfo);
			list.add(newRoom);
			System.out.println("Added client " + clientInfo + " to new room "
					+ newRoom);
		}
	}
	
	public void remove(ClientInfo clientInfo) {
		String room = clientInfo.getRoom().toLowerCase();
		boolean foundRoom = false;
		for (int i = 0; i < list.size(); i++) {
			Room currentRoom = list.get(i);
			if (currentRoom.getName().toLowerCase().equals(room)) {
				if (currentRoom.remove(clientInfo))
					System.out.println("Removed client");
				else
					System.out.println("Client not found.");
				foundRoom = true;
				System.out.println("Removed client " + clientInfo + " to room "
						+ currentRoom);

				if (currentRoom.isEmpty()) {
					list.remove(currentRoom);
					System.out.println("Removed empty room.");
				}

			}
		}
		if (!foundRoom) {
			System.out.println("Couldn't find room");
		}

	}
	
    //From List. If i want to implement more List functions, need to add here.

    public int getMaxClients() {
		return maxClients;
	}

	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}


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
	public boolean addAll(Collection<? extends Room> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Room> c) {
		return list.addAll(index, c);
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
	public List<Room> subList(int fromIndex, int toIndex) {
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