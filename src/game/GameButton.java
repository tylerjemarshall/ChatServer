package game;
import javax.swing.JButton;


/**
 * Adds functionality to the JButton, including setTag tag, setValue value.
 * @author Tyler M
 *
 */
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
