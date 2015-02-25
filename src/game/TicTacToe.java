package game;
/**
 * @author Tyler M
 *
 */

import javax.imageio.ImageIO;
import javax.swing.*;

import client.ChatClient;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TicTacToe extends JFrame {

	private static final long serialVersionUID = 1L;

	// JFrame variables
	private ImageIcon xImgIcon = null;
	private ImageIcon oImgIcon = null;
	private final Panel game;
	
	// GameButton extends JButton and adds setTag and setValue
	private GameButton button[][] = new GameButton[3][3]; 

	// ScoreBoard Stuff
	private int xWins = 0;
	private int oWins = 0;
	private JLabel xScore = new JLabel("X Score: " + xWins, JLabel.LEFT);
	private JLabel oScore = new JLabel("O Score: " + oWins, JLabel.LEFT);

	// Game Variables
	private int turn = 0;
	private boolean gameover = false;

	// Online Variables
	private boolean online = false;
	private ChatClient client = null;
	

	public TicTacToe() {
		super("TicTacToe");
		setSize(300, 300);

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				button[x][y] = (GameButton) new GameButton("");
				button[x][y].setToolTipText(x + " " + y);
				button[x][y].setTag("");

			}

		}
		// Draw Game

		setLayout(new BorderLayout(5, 5));

		game = new Panel();

		final Panel score = new Panel();

		add("Center", game);

		add("South", score);

		game.setLayout(new GridLayout(3, 3, 5, 5));

		score.setLayout(new GridLayout(1, 2, 5, 5));

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				game.add(button[x][y]);
			}
		}

		// Image Stuff goes here - throws IOE
		BufferedImage xImg;
		try {
			xImg = ImageIO.read(new File("x.png"));
			BufferedImage oImg = ImageIO.read(new File("o.png"));
			xImgIcon = new ImageIcon(xImg);
			oImgIcon = new ImageIcon(oImg);
		} catch (IOException e1) {
			System.out.println("missing picture files for X and O");
		}

		score.add(xScore);
		score.add(oScore);

		setVisible(true);

		// This handles closing the client with the X Button
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (promptUser(game, "Close the window?", "Exit") == 1)
					dispose();
			}
		});

		// All the ActionListeners!
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				button[x][y].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// This is making a copy of button[x][y] since it is
						// unreachable
						JButton tempJButton = (JButton) e.getSource();
						int x = Integer.parseInt(tempJButton.getToolTipText()
								.substring(0, 1));
						int y = Integer.parseInt(tempJButton.getToolTipText()
								.substring(2, 3));
						movePiece(x, y);
					}
				});
			}
		}
	}

	
	// ///////////////////////////////////////////////////////////////////
	// checkWin
	// Returns Winner as String ("X", "O", "", "T")
	// ///////////////////////////////////////////////////////////////////

	private String checkWin() {
		// checks Cats Game (Tie)
		int count = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (button[x][y].getTag() != "") {
					count++;
					if (count == 9)
						return "T";
				}
			}
		}

		// checks Horizontal
		for (int y = 0; y < 3; y++) {
			if (button[0][y].getTag() == button[1][y].getTag()
					&& button[1][y].getTag() == button[2][y].getTag()) {
				return button[1][y].getTag();
			}
		}
		// checks Vertical
		for (int x = 0; x < 3; x++) {
			if (button[x][0].getTag() == button[x][1].getTag()
					&& button[x][1].getTag() == button[x][2].getTag()) {
				return button[x][1].getTag();
			}
		}
		// Checks Diagonal
		if (button[0][0].getTag() == button[1][1].getTag()
				&& button[1][1].getTag() == button[2][2].getTag()) {
			return button[1][1].getTag();
		}
		if (button[2][0].getTag() == button[1][1].getTag()
				&& button[1][1].getTag() == button[0][2].getTag()) {
			return button[1][1].getTag();
		}
		return "";
	}

	
	// ///////////////////////////////////////////////////////////////////
	// promptUser
	// return 1 if yes, 0 if no	
	// ///////////////////////////////////////////////////////////////////

	private int promptUser(Panel panel, String message, String title) {
		if (JOptionPane.showConfirmDialog(panel, message, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			return 1;
		}
		return 0;

	}

	// ///////////////////////////////////////////////////////////////////
	// updateButton
	// ///////////////////////////////////////////////////////////////////

	private void updateButton(int x, int y, String text) {
		if (!gameover) {

			button[x][y].setTag(text);
			if (text == "X")
				button[x][y].setIcon(xImgIcon);
			if (text == "O")
				button[x][y].setIcon(oImgIcon);
			if (text == "")
				button[x][y].setIcon(null);
		} else {
			button[x][y].setTag("");
			button[x][y].setIcon(null);
		}
	}

	// ///////////////////////////////////////////////////////////////////
	// clearBoard
	// ///////////////////////////////////////////////////////////////////

	private void clearBoardWork() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				try {
					updateButton(x, y, "");
				} catch (Exception e) {
					System.out.println("Invalid Move");
				}
			}
		}
	}

	// Offline Version
	private void clearBoard() {
		gameover = true;
		if (online)
			clearBoard(false);
		else
			clearBoardWork();

	}

	// Online Version
	public void clearBoard(boolean fromServer) {
		if (!fromServer) {
			try {
				sendCommand("#clearboard");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			clearBoardWork();
	}

	// ///////////////////////////////////////////////////////////////////
	// movePiece
	// ///////////////////////////////////////////////////////////////////

	private void movePieceWork(int x, int y) {

		if (gameover) {
			gameover = false;
			System.out.println("Seting gameover to false");
		}
		String contents = button[x][y].getTag();
		if (contents == "") {
			if (turn == 0) {
				try {
					updateButton(x, y, "X");
				} catch (Exception e) {
					System.out.println("Invalid Move");
				}
				turn = 1;

			} else if (turn == 1) {
				try {
					updateButton(x, y, "O");
				} catch (Exception e) {
					System.out.println("Invalid Move");
				}
				turn = 0;
			}
			String winner = checkWin();
			switch (winner) {
			case "X":
				xWins++;
				xScore.setText("X Score: " + xWins);
				if (promptUser(game, "Yes-Restart\nNo-Quit", "Congrats player "
						+ winner) == 1)
					clearBoard();
				else
					dispose();
				break;
			case "O":
				oWins++;
				oScore.setText("O Score: " + oWins);
				if (promptUser(game, "Yes-Restart\nNo-Quit", "Congrats player "
						+ winner) == 1)
					clearBoard();
				else
					dispose();
				break;
			case "":
				break;
			case "T":
				if (promptUser(game, "Yes to Restart\nNo to Quit", "Cats game!") == 1)
					clearBoard();
				else
					dispose();
				break;
			default:
				if (promptUser(game, "Yes to Restart\nNo to Quit",
						"Weird, error code: " + winner) == 1)
					clearBoard();
				else
					dispose();
			}
		}
		return;

	}

	// Offline version
	private void movePiece(int x, int y) {
		if (online) {
			movePiece(x, y, false);
		} else {
			movePieceWork(x, y);
		}
	}

	// Online Version
	public void movePiece(int x, int y, boolean fromServer) {
		if (!fromServer) {
			try {
				//System.out.println("Asking server to move piece");
				sendCommand("#move " + x + " " + y);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//System.out.println("Moving piece");
			movePieceWork(x, y);

		}
	}

	// ///////////////////////////////////////////////////////////////////
	// sendCommand
	// ///////////////////////////////////////////////////////////////////

	private void sendCommand(String command) throws IOException {
		//System.out.println("Sending command: " + command);
		client.sendToServer(command);
	}

	/////////////////////////////////////////////////////////////////////
	//getters and setters
	/////////////////////////////////////////////////////////////////////
	
	public int getxWins() {
		return xWins;
	}

	public void setxWins(int xWins) {
		this.xWins = xWins;
	}

	public int getoWins() {
		return oWins;
	}

	public void setoWins(int oWins) {
		this.oWins = oWins;
	}

	public JLabel getxScore() {
		return xScore;
	}

	public void setxScore(JLabel xScore) {
		this.xScore = xScore;
	}

	public JLabel getoScore() {
		return oScore;
	}

	public void setoScore(JLabel oScore) {
		this.oScore = oScore;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public ChatClient getClient() {
		return client;
	}

	public void setClient(ChatClient chatClient) {
		this.client = chatClient;
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		TicTacToe g = new TicTacToe();
	}

}
