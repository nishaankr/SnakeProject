import javax.swing.JFrame;

public class GameFrame extends JFrame {
	GameFrame(){
	this.add(new GamePanel());
	this.setTitle("Snake");//title of Jframe
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits the Jframe window on clicking the close icon
	this.setResizable(false);
	this.pack();
	this.setVisible(true);
	this.setLocationRelativeTo(null);
	}
}
