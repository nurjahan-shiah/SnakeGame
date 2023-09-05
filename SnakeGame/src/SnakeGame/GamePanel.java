package SnakeGame;


 import java.awt.*;

 
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel<Restart> extends JPanel implements ActionListener{

	JFrame frame = new JFrame();
    JPanel panel = new JPanel();
   
	static final int SCREEN_WIDTH = 1000;
	static final int SCREEN_HEIGHT = 500;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 75; //speed up the game
	
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten=0;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	public void createGUI() {
        panel.add(new GamePanel());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			
			g.setColor(Color.green); //apple color
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green); //head color of the snake
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(50,100,100)); //body color of the snake
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			//score at the top
			g.setColor(Color.magenta); 
			g.setFont( new Font("Helvetica",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
			
		}
		else {
			gameOver(g);
			
		}
		
	}
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		// snake head touches left wall
		if(x[0] < 0) {
			running = false;
		}
		//snake head touches right wall
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//snake head touches top wall
		if(y[0] < 0) {
			running = false;
		}
		//snake head touches bottom wall
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Score
		
		g.setColor(Color.red);
		g.setFont( new Font("Helvetica",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		
		//Game Over text
		
		g.setColor(Color.red);
		g.setFont( new Font("Helvetica",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//play again
		
		g.setColor(Color.white);
		g.setFont( new Font("Helvetica",Font.BOLD, 35));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
	    g.drawString("Press 'Enter' to play again ", (SCREEN_WIDTH-metrics3.stringWidth("Press 'Enter' to play again "))/2, SCREEN_HEIGHT/2+70);
	    
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	 public void closeRunningWindow(KeyEvent e) {
	        JComponent comp = (JComponent) e.getSource();
	        Window win = SwingUtilities.getWindowAncestor(comp);
	        win.dispose();
	    }
	    
	    public void startAgain() {
	        EventQueue.invokeLater(() -> {
	            GamePanel game = new GamePanel();
	            game.createGUI();
	        });
	    }
	


	public class MyKeyAdapter extends KeyAdapter{
		
		
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				case KeyEvent.VK_ENTER:
                    closeRunningWindow(e);
                    startAgain();
                    break;
            

			}
			
				
		}

	}
	public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GamePanel game = new GamePanel();
            game.createGUI();
        });

}
	}
	



