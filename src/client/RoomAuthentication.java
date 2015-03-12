package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

	public class RoomAuthentication extends JDialog{

		private static final long serialVersionUID = 1L;
		
		// Passed Variables
		private GUIConsole parent;
		//private char[] msg;
		
		//Labels
		private JLabel descriptionLB = new JLabel("A password is required to join room", JLabel.CENTER);
		private JLabel passwordLB = new JLabel("Enter the password", JLabel.CENTER);
		
		private JLabel displayLB = new JLabel(" ", JLabel.CENTER);
		
		//Text fields
		private JPasswordField passwordPwF = new JPasswordField();
		
		// Buttons
		private JButton submitBtn = new JButton("Submit");
		private JButton cancelBtn = new JButton("Cancel");
		
		// My Font.
		private Font font = new javax.swing.plaf.FontUIResource("Tahoma",
					Font.BOLD, 16);
		
		//Constructor
		
		public RoomAuthentication(GUIConsole parent) { 
			super(parent, "Password Required", true);
			this.parent = parent;
			//this.msg = msg;
			
			submitBtn.setFont(font);
			cancelBtn.setFont(font);

			JPanel box = new JPanel();

			JPanel center = new JPanel();
			JPanel north = new JPanel();
			JPanel south = new JPanel();

			box.setLayout(new BorderLayout(6, 5));

			box.add("North", north);
			box.add("Center", center);
			box.add("South", south);

			
			north.setLayout(new GridLayout(1, 1, 30, 30));
			center.setLayout(new GridLayout(2, 2, 2, 2));
			south.setLayout(new GridLayout(1, 1, 30, 30));

			
			//segments of the gridlayout
			
			north.add(descriptionLB);

			center.add(passwordLB);		center.add(passwordPwF);
			center.add(cancelBtn);		center.add(submitBtn);

			south.add(displayLB);


			getContentPane().add(box, BorderLayout.CENTER);
			
//			setSize(300, 200);
			setResizable(false);
			setAlwaysOnTop(true);
			setLocationRelativeTo(parent);
			pack();
			
			//Validate Room ActionListeners.
			
			submitBtn.addActionListener(new RoomAuthenticationAL() );
			passwordPwF.addActionListener(new RoomAuthenticationAL() );
			
		
			
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});	
		}
		
		
		//Triggered by the ActionListeners above.
		private class RoomAuthenticationAL implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					if (RoomHandshake.authenticate(passwordPwF.getPassword(), getParent())) {
						JOptionPane.showMessageDialog(RoomAuthentication.this, 
								 "Password Sent.",
								"Sent", JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} else {
						JOptionPane.showMessageDialog(RoomAuthentication.this,
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
		
	}

	

