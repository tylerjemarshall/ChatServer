package server;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;





public class Room implements RoomInterface, Comparable<Room>{

    protected ArrayList<ClientInfo> list;

    
    private int limit = DEFAULT_LIMIT;
    private String name = "commons";
    private int id;
    private boolean open = true;
    
    
    /**
     * Class that acts as a container for the list ClientInfo.
     * Will have a limit to clients in a room and custom attributes.
     */
    public Room(){
        list = new ArrayList<ClientInfo>();
    }
    
	
    
	public int getClientIndex(int itemName) {
		for (int i = 0; i < list.size(); i++) {
			ClientInfo client = list.get(i);
			if (itemName == (Integer) client.getId()) {
				return i;
			}
		}

		return -1;
	}
    
    public ClientInfo getClientInfoById(int id)
    {
	
		for (int y = 0; y < list.size(); y++) {
			ClientInfo tempClient = list.get(y);
			if (tempClient.getId() == id) {
				return tempClient;
			}
		}
		return null;
    }
    
    public ClientInfo getClientInfoByName(String name)
    {
    	name=name.toLowerCase();
    	for (int y = 0; y < list.size(); y++) {
			ClientInfo tempClient = list.get(y);
			if (tempClient.getUserName().toLowerCase().equals(name)) {
				return tempClient;
			}
		}
    	return null;
    }
    
    public ClientInfo getInfoByClient(ConnectionToClient client)
    {
    	for (int y = 0; y < list.size(); y++) {
			ClientInfo tempClient = list.get(y);
			if (tempClient.getClient().equals(client)) {
				return tempClient;
			}
		}
    	return null;
    	
    }
	
	//Basic Getters and Setters for custom variables.
	public String getName() {
		return name;
	}


	public void setName(String name) {
		for (int i = 0; i < list.size(); i++) {
			ClientInfo client = list.get(i);
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

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	/**
	 * If the Room is accepting new clients, true. Else is closed and sending new clients to their default room.
	 * @return Returns true if Room is open. False if closed.
	 */
	public boolean isOpen() {
		return open;
	}



	public void setOpen(boolean open) {
		this.open = open;
	}



	public String toString()
	{
		return name + "(" + list.size()+"/"+getLimit()+")";
	}
	
	
	//From Comparable, since I have a list of these rooms.

	
   @Override
    public int compareTo(Room o) {
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
       result = 31*result + (name !=null ? name.hashCode() : 0);
      
       return result;
   }

    //From List. If i want to implement more List functions, need to add here.

    public Iterator<ClientInfo> getIter(){
        return list.iterator();
    }

	@Override
	public boolean add(ClientInfo e) {
		return list.add(e);
	}

	@Override
	public void add(int index, ClientInfo element) {
		list.add(index, element);

	}

	@Override
	public boolean addAll(Collection<? extends ClientInfo> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends ClientInfo> c) {
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
	public ClientInfo get(int index) {
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
	public Iterator<ClientInfo> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<ClientInfo> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<ClientInfo> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public ClientInfo remove(int index) {
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
	public ClientInfo set(int index, ClientInfo element) {
		return list.set(index, element);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<ClientInfo> subList(int fromIndex, int toIndex) {
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