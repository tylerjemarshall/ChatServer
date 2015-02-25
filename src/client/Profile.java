package client;
import java.awt.Font;


/**
 * @author Tyler M
 *
 */
public class Profile {

	
	//Client
	private String userName;
	
	
	//Fonts
	private String fontName;
	private int fontStyle;
	private int fontSize;
	private Font font = new javax.swing.plaf.FontUIResource("Tahoma",Font.PLAIN,12);
	
	public Profile() {
		
	}
	
	public Font setFont(String name, int style, int size)
	{
		setFontName(name);
		setFontStyle(style);
		setFontSize(size);
		this.font = (new javax.swing.plaf.FontUIResource(name,style,size));
		return getFont();
	}


	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		
		this.fontName = fontName;
		
	}

	public int getFontStyle() {
		return fontStyle;
	}
	
	

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
		
	}

	public void setFontStyle(String fontStyle)
	{
		this.fontStyle = fontStyleToInt(fontStyle);
	}
	
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * Sets Font Size
	 * @param fontSize Has a limit of 24.
	 */
	public void setFontSize(int fontSize) {
		int max = 16;
		this.fontSize = (fontSize>max) ? max : fontSize;
	}

	public Font getFont() {
		
		this.font = (new javax.swing.plaf.FontUIResource(this.fontName,this.fontStyle,this.fontSize));
		
		return font;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private int fontStyleToInt(String font)
	{
		switch (font)
		{
		case "bold":
			return 1;
		case "italic":
			return 2;
		case "plain":
			return 0;
		default:
			return 0;
		}		
	}

	
	
}
