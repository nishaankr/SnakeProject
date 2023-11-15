import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyLength = 6;
	int applesEaten;
	int appleX;	// co-ord of where the apple is going to spawn randomly
	int appleY; 
	long startTime;
	char direction = 'R';
	boolean running = true;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setFocusable(true);  //ability to focus on screen on click in the window
		this.setBackground(Color.black);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		//sets starting position ----------------------------
		/*
	    int startX = 5;
        int startY = 5;
        
        for (int i = 0; i < bodyLength; i++) {
            x[i] = startX * UNIT_SIZE;
            y[i] = startY * UNIT_SIZE;
        }
        */
        //---------------------------------------------------
		
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this); // need to use javas.swing.timer for this and not java utils
		timer.start();
        startTime = System.currentTimeMillis();

		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	
	public void draw(Graphics g) {
		
		if (running) {
			/* Grid lines --- remove comments to re-enable
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.setColor(Color.yellow);
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			//Adding head and body of snake by adding color to it
			for(int i = 0; i< bodyLength; i++) {
				if (i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
				}
			g.setColor(Color.red);
			g.setFont(new Font("Helvetica",Font.BOLD, 30 ));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
			}
		else {
			gameOver(g);
		}
	}
	
	public void newApple() { //generates new APPLE
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*(UNIT_SIZE);
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*(UNIT_SIZE);
	}
	
	public void move() {

		for(int i = bodyLength; i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U' :
			y[0] = y[0] - UNIT_SIZE; // dimensions in CG are set where Origin is on top left.
			break;
		case 'D' :
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'R' :
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'L' :
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX)&&(y[0]==appleY)){
			bodyLength++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		for (int i = bodyLength; i>0;i--) {
			if ((x[0] == x[i]) && (y[0]==y[i]))
				running = false;
		}
		
		//collisions with edges
		if ((x[0]< 0)||(x[0]>SCREEN_WIDTH)||(y[0]< 0)||(y[0]>SCREEN_HEIGHT)) {
			running =false;
		}
		
		if (!running) {
			timer.stop();
		}
		
			
	}
	public void gameOver(Graphics g) {
		//time in ms
        long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        //time in s
        long elapsedTimeSec = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis);
        //score
		g.setColor(Color.red);
		g.setFont(new Font("Helvetica",Font.PLAIN, 40 ));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Final Score: "+ applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Final Score: "+ applesEaten))/2, g.getFont().getSize()+320);
		
		//timer
		g.setColor(new Color(3, 237, 233));
		g.setFont(new Font("Helvetica",Font.PLAIN, 30 ));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Time Elapsed: "+ elapsedTimeSec, (SCREEN_WIDTH - metrics.stringWidth("Time Elapsed: "+ elapsedTimeSec))/2, g.getFont().getSize()+360);
		
		//game over
		g.setColor(Color.red);
		g.setFont(new Font("Helvetica",Font.BOLD, 60 ));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_UP :
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN :
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_LEFT :
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT :
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			}
		}
	}
	

}
