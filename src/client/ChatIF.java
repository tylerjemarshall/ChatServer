package client;
public interface ChatIF 
{
  /**
   * Method that when overridden is used to display objects onto
   * a UI.
   */
  public abstract void display(String message);
  public abstract void display(String message, String user); 
  public abstract void sendToUI(Object o);
   
}