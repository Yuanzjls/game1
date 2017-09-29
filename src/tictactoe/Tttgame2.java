package tictactoe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Tttgame2 extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final int ROWS = 3;
	public static final int COLUMNS = 3;
	
	public static final int CELL_SIZE = 100;
	public static final int CANVAS_WIDTH = CELL_SIZE * COLUMNS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	public static final int GRID_WIDTH = 8;
	public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
	public static final int DISTANCE_BOARD = 10;
	public static final int ROUND_RADIUS = 40;
	
	public enum GameState
	{
		PLAYING, TIE, HUMAN_WON, COMPUTER_WON
	}
	private GameState currentState;
	
	public enum Seed
	{
		COMPUTER, EMPTY, HUMAN
		
//		private final int id;
//		
//		Seed(int id) {this.id = id;}
//		public int getValue() {return id;}
	}
	
	private Seed currentPlayer;
	
	private Seed[][] board;
	private GameCanvas canvas;
	private JLabel statusBar;

	
	public Tttgame2()
	{
		canvas = new GameCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_WIDTH));
		
		canvas.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				int rowselected = mouseX / CELL_SIZE;
				int columnselected = mouseY / CELL_SIZE;
				
				if (currentState == GameState.PLAYING)
				{
					if (rowselected >= 0 && rowselected < ROWS &&
							columnselected >= 0 && columnselected < COLUMNS)
					{
						if (board[rowselected][columnselected] == Seed.EMPTY)
						{
							board[rowselected][columnselected] = currentPlayer;
							checkWineer(currentPlayer, rowselected, columnselected);
							currentPlayer = (currentPlayer == Seed.HUMAN) ? Seed.COMPUTER : Seed.HUMAN;
							if (currentState == GameState.PLAYING)
							{
								MoveInfor Movecom = findCompMove();
								board[Movecom.row][Movecom.column] = Seed.COMPUTER;
								checkWineer(Seed.COMPUTER, Movecom.row, Movecom.column);
								currentPlayer = (currentPlayer == Seed.HUMAN) ? Seed.COMPUTER : Seed.HUMAN;								
							}
						}
					}
				}
				else
				{
					initGame();
				}
				repaint();
			}
		});
		
		setResizable(false);

		statusBar = new JLabel(" ");//一定要加上“ ”，有部分画布会被遮挡住
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
		
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(statusBar, BorderLayout.PAGE_END);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();  // pack all the components in this JFrame
		setTitle("Tic Tac Toe version 2");
		setVisible(true);  // show this JFrame		
		
		board = new Seed[ROWS][COLUMNS];		
		initGame();

	}	
	
	public void initGame() 
	{
		for (int i = 0; i < ROWS; i++) 
		{
			for (int j = 0; j < COLUMNS; j++)
			{
				board[i][j] = Seed.EMPTY;
			}
		}
		currentState = GameState.PLAYING;
		currentPlayer = Seed.HUMAN;
//		MoveInfor Movecom = findCompMove();
//		board[Movecom.row][Movecom.column] = Seed.COMPUTER;
//		checkWineer(Seed.COMPUTER, Movecom.row, Movecom.column);
	}
	
	public void checkWineer(Seed theSeed, int rowSelected, int columnSelected)
	{
		if (haswon(theSeed, rowSelected, columnSelected))
		{
			currentState = (theSeed == Seed.HUMAN)? GameState.HUMAN_WON : GameState.COMPUTER_WON; 
		}
		else if (isfull())
		{
			currentState = GameState.TIE;
		}
	}
	
	public boolean isfull()
	{
		for (int i=0; i<ROWS; i++)
		{
			for (int j = 0; j < COLUMNS; j++)
			{
				if (board[i][j] == Seed.EMPTY)
				{
					return false;
				}
			}
		}
		return true;
		
	}
	
	public boolean haswon(Seed theSeed, int rowSelected, int columnSelected)
	{
		if ((board[rowSelected][0] == theSeed && board[rowSelected][1] == theSeed && board[rowSelected][2] == theSeed)||
			 (board[0][columnSelected] == theSeed && board[1][columnSelected] == theSeed && board[2][columnSelected] == theSeed) ||
			 (rowSelected == columnSelected && board[0][0] == theSeed && board[1][1] == theSeed && board[2][2] == theSeed) ||
			 (rowSelected + columnSelected == 2 && board[0][2] == theSeed && board[1][1] == theSeed && board[2][0] == theSeed))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public boolean haswon(Seed theSeed)
	{
		
		if ((board[0][0] == theSeed && board[0][1] == theSeed && board[0][2] == theSeed) ||
				(board[1][0] == theSeed && board[1][1] == theSeed && board[1][2] == theSeed) ||
				(board[2][0] == theSeed && board[2][1] == theSeed && board[2][2] == theSeed) ||
				(board[0][0] == theSeed && board[1][0] == theSeed && board[2][0] == theSeed) ||
				(board[0][1] == theSeed && board[1][1] == theSeed && board[2][1] == theSeed) ||
				(board[0][2] == theSeed && board[1][2] == theSeed && board[2][2] == theSeed) ||
				(board[0][0] == theSeed && board[1][1] == theSeed && board[2][2] == theSeed) ||
				(board[0][2] == theSeed && board[1][1] == theSeed && board[2][0] == theSeed))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
//	public int value_score(Seed theSeed, int rowSelected, int columnSelected, int depth)
//	{
//		if (haswon(theSeed, rowSelected, columnSelected, board))
//		{
//			if (theSeed == Seed.COMPUTER)
//			{
//				return 10 - depth;
//			}
//			else if (theSeed == Seed.HUMAN)
//			{
//				return depth - 10;
//			}
//		}
//		return 0;
//	}	

	public class MoveInfor
	{
		public int row;
		public int column;
		public int value;
		
		public MoveInfor(int row, int column, int value)
		{
			this.row = row;
			this.column = column;
			this.value = value;
		}
	}
	
	public static final int DRAW = 0;
	public static final int COMP_WIN = 1;
	public static final int COMP_LOSS = -1;
	
	public MoveInfor findCompMove()
	{
		int responseValue;
		int value, bestrow = 0, bestcolumn = 0;
		MoveInfor quickWinInfor;
		
		if (isfull())
		{
			value = DRAW;
		}
		else if (haswon(Seed.COMPUTER)) 
		{
			quickWinInfor = new MoveInfor(bestrow, bestcolumn, COMP_WIN);
			return quickWinInfor;
		}
		else 
		{
			value = COMP_LOSS;
			for (int i=0; i<ROWS; i++)
			{
				for (int j = 0; j < COLUMNS; j++)
				{
					if (board[i][j] == Seed.EMPTY)
					{
						board[i][j] = Seed.COMPUTER;
						responseValue = findHumanMove().value;
						board[i][j] = Seed.EMPTY;
						
						if (responseValue > value)
						{
							value = responseValue;
							bestrow = i;
							bestcolumn = j;
						}
					}
				}
			}
		}
		return new MoveInfor(bestrow, bestcolumn, value);
	}
	
	
	public MoveInfor findHumanMove()
	{
		int responseValue;
		int value, bestrow = 0, bestcolumn = 0;
		MoveInfor quickWinInfor;
		
		if (isfull())
		{
			value = DRAW;
		}
		else if (haswon(Seed.HUMAN))
		{
			quickWinInfor = new MoveInfor(bestrow, bestcolumn, COMP_LOSS);
			return quickWinInfor;
		}
		else
		{
			value = COMP_WIN;
			for (int i=0; i<ROWS; i++)
			{
				for (int j = 0; j < COLUMNS; j++)
				{
					if (board[i][j] == Seed.EMPTY)
					{
						board[i][j] = Seed.HUMAN;
						responseValue = findCompMove().value;
						board[i][j] = Seed.EMPTY;
						
						if (responseValue < value)
						{
							value = responseValue;
							bestrow = i;
							bestcolumn = j;
						}
					}
				}
			}
		}
		
		return new MoveInfor(bestrow, bestcolumn, value);
	}
	
	class GameCanvas extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			setBackground(Color.WHITE);
			
			g.setColor(Color.LIGHT_GRAY);
			
			for (int i = 1; i < ROWS; i++) 
			{
				g.fillRect(0, i * CELL_SIZE - GRID_WIDTH_HALF, CANVAS_WIDTH, GRID_WIDTH);
			}
			for (int i = 1; i < COLUMNS; i++) 
			{
				g.fillRect(i * CELL_SIZE - GRID_WIDTH_HALF, 0, GRID_WIDTH, CANVAS_WIDTH);
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			for (int i = 0; i < ROWS; i++) 
			{
				for (int j=0; j<COLUMNS; j++)
				{
					if (board[i][j] == Seed.HUMAN)
					{
						g2d.setColor(Color.blue);
						g2d.drawLine(i*CELL_SIZE + DISTANCE_BOARD, j*CELL_SIZE + DISTANCE_BOARD, (i+1) * CELL_SIZE - DISTANCE_BOARD, (j+1) * CELL_SIZE - DISTANCE_BOARD);
						g2d.drawLine(i*CELL_SIZE + DISTANCE_BOARD, (j+1) * CELL_SIZE - DISTANCE_BOARD, (i+1) * CELL_SIZE - DISTANCE_BOARD, j*CELL_SIZE + DISTANCE_BOARD);
					}
					else if (board[i][j] == Seed.COMPUTER)
					{
						g2d.setColor(Color.red);
						g2d.drawOval(i*CELL_SIZE + CELL_SIZE /2 -ROUND_RADIUS , j * CELL_SIZE + CELL_SIZE/2 - ROUND_RADIUS, 2*ROUND_RADIUS, 2*ROUND_RADIUS);
					}
				}
			}
			if (currentState == GameState.HUMAN_WON)
			{
				statusBar.setText("A has won the game");
			}
			else if (currentState == GameState.COMPUTER_WON)
			{
				statusBar.setText("B has won the game");
			}
			else if (currentState == GameState.TIE)
			{
				statusBar.setText("The result is tied");
			}
			else if (currentState == GameState.PLAYING)
			{
				if (currentPlayer == Seed.HUMAN)
				{
					statusBar.setText("A is playing");
				}
				else
				{
					statusBar.setText("B is playing");
				}
			}
		}
	}

	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new Tttgame2();
			}
		});
	}
}
