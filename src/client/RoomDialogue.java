package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;



public class RoomDialogue extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Passed Variables
	private GUIConsole parent;
	
	//Labels
	private JLabel createLB = new JLabel("Create a room", JLabel.CENTER);

	private JLabel nameLB = new JLabel("Name: ", JLabel.LEFT);
	private JLabel limitLB = new JLabel("Room Size:", JLabel.LEFT);
	private JLabel typeLB = new JLabel("Type: ", JLabel.LEFT);
	private JLabel nullLB = new JLabel(" ", JLabel.LEFT);
	private JLabel passwordLB = new JLabel("Password: ", JLabel.LEFT);

	private JLabel displayLB = new JLabel(" ",
			JLabel.CENTER);
	
	//Text fields
	private JTextField nameTxF = new JTextField();
	private JTextField limitTxF = new JTextField();
	private JPasswordField passwordPwF = new JPasswordField();
	
	
	// Buttons
	private JButton createBtn = new JButton("Create");
	private JButton cancelBtn = new JButton("Cancel");
	
	// My Font.
	private Font font = new javax.swing.plaf.FontUIResource("Tahoma",
				Font.BOLD, 16);
	
	//Radio Buttons.
	private JRadioButton open = new JRadioButton("public");
	private JRadioButton closed = new JRadioButton("private");
	private ButtonGroup bG = new ButtonGroup();
	

	
	//Constructor
	
	public RoomDialogue(GUIConsole parent) { 
		super(parent, "Create Room", true);
		this.parent = parent;
		
		createBtn.setFont(font);
		cancelBtn.setFont(font);

		JPanel box = new JPanel();

		JPanel center = new JPanel();
		JPanel north = new JPanel();
		JPanel south = new JPanel();

		box.setLayout(new BorderLayout(6, 5));

		box.add("North", north);
		box.add("Center", center);
		box.add("South", south);

	     bG.add(open);
	     bG.add(closed);
	     open.setSelected(true);

		
		north.setLayout(new GridLayout(1, 1, 30, 30));
		center.setLayout(new GridLayout(6, 2, 2, 2));
		south.setLayout(new GridLayout(1, 1, 30, 30));

		//segments of the gridlayout
		
		north.add(createLB);

		center.add(nameLB);			center.add(nameTxF);
		center.add(limitLB);		center.add(limitTxF);
		center.add(typeLB);			center.add(open);			
		center.add(nullLB);			center.add(closed);
		center.add(passwordLB);		center.add(passwordPwF);
		center.add(cancelBtn);		center.add(createBtn);

		south.add(displayLB);


		getContentPane().add(box, BorderLayout.CENTER);
		
		setSize(300, 200);
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationRelativeTo(parent);

		//Validate Room ActionListeners.
		
		createBtn.addActionListener(new RoomValidationAL() );
		passwordPwF.addActionListener(new RoomValidationAL() );
		limitTxF.addActionListener(new RoomValidationAL() );
		nameTxF.addActionListener(new RoomValidationAL() );
		
		//'Cancel' button ActionListner
		
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});	
	}
	
	
	//Triggered by the ActionListeners above.
	private class RoomValidationAL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (RoomValidation.authenticate(getArgs(), getParent())) {
					JOptionPane.showMessageDialog(RoomDialogue.this, 
							 "Room Created.",
							"Create a room", JOptionPane.INFORMATION_MESSAGE);
					//succeeded = true;
					dispose();
				} else {
					JOptionPane.showMessageDialog(RoomDialogue.this,
							"A problem has occurred", "Error",
							JOptionPane.ERROR_MESSAGE);
					//userTxF.setText(""); only clears password field.
					passwordPwF.setText("");
					displayLB.setText("A problem has happened");
				}
			} catch (Exception e1) {
				displayLB.setText(e1.getMessage());
			}
		
		}
	}
	
	
	public GUIConsole getParent() {
		return parent;
	}
	
	//sending information to validation
	
	@SuppressWarnings("deprecation")
	public String[] getArgs() {
		return new String[]{nameTxF.getText(), limitTxF.getText(), passwordPwF.getText(), Boolean.toString(open.isSelected())};
	}
	
}
