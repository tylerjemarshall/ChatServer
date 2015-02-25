import javax.swing.JButton;


public class GameButton extends JButton {

	private String tag;
	private int value;
	private static final long serialVersionUID = 1L;

	public GameButton(String text) {
		super(text);	
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}


}
