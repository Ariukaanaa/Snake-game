import javax.swing.JFrame;

public class GameFrame extends JFrame 
{
    GameFrame()
    {
        this.add(new GamePanel()); 
        this.setTitle("snake"); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /* Exit on close */ 
        this.setResizable(false); /* Window size is fixed */
        this.pack(); 
        this.setVisible(true); 
        this.setLocationRelativeTo(null); 
    }
}