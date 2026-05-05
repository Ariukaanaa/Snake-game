

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;


public class GamePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH  = 600 ; 
    static final int SCREEN_HEIGHT = 600 ; 
    static final int UNIT_SIZE = 35; // itemiin hemjeeg todorhoilno 
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT/UNIT_SIZE); 
    static final int DELAY = 100;

    final int x[] = new int[GAME_UNITS]; 
    final int y[] = new int[GAME_UNITS]; 

    JButton btnRes ; 
    int bodyParts  = 6 ; // we begin with 6 body parts 
    int applesEaten; 
    int appleX; 
    int appleY; 
    int appleX2;  
    int appleY2;
    char direction = 'R'; 
    boolean running = false ; 
    Random random ; 
    Timer timer ; 
    GamePanel()
    {
        random = new Random(); 
        btnRes = new JButton("Restart"); 
        this.setPreferredSize( new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); 
        this.setBackground(Color.black); 
        this.setFocusable(true); 
        this.addKeyListener(new  MyKeyAdapter()); 
        this.add(btnRes); 
        btnRes.addActionListener(this);
        startGame(); 
    }

    public void startGame()
    {
        newApple1(); 
        newApple2(); 
        btnRes.setVisible(false);
        running = true ; 
        timer = new Timer(DELAY, this); 
        timer.start(); 
    }

    public void restartGame()
    {
        // Reset game state
        bodyParts   = 6;
        applesEaten = 0;
        direction   = 'R';
        for(int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame(); 
    }
    public void paintComponent( Graphics g )
    {
        super.paintComponent(g); 
        draw(g); 
    }

    public void draw(Graphics g)
    {
        if(running)
        {
            for( int i = 0 ; i < SCREEN_HEIGHT/UNIT_SIZE; i++ )
            {
                g.drawLine( i*UNIT_SIZE, 0 ,  i*UNIT_SIZE, SCREEN_HEIGHT );
                g.drawLine( 0,  i*UNIT_SIZE, SCREEN_HEIGHT ,  i*UNIT_SIZE  );  
            }
            g.setColor(Color.red); 
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
            g.fillOval(appleX2, appleY2, UNIT_SIZE, UNIT_SIZE);
             

            for(int i= 0 ; i< bodyParts; i++ )
            {
                if( i == 0 )
                {
                    g.setColor(Color.green); 
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE ); 
                }
                else
                {
                    g.setColor( new Color(45, 180, 0 ));
                    //g.setColor( new Color( random.nextInt(255),  random.nextInt(255),  random.nextInt(255))) ; 
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE ); 
                }
            }
            g.setColor(Color.red); 
            g.setFont( new Font("Ink Free", Font.BOLD,40)); 
            FontMetrics metrics = getFontMetrics(g.getFont()); 
            g.drawString("Score:" + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize() ); 
        }
        else 
        {
            gameOver(g); 
        }
        
    }

    public void newApple1()
    {
        appleX = random.nextInt((int)(SCREEN_WIDTH /UNIT_SIZE)) * UNIT_SIZE; 
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE; 
       
    }

    public void newApple2()
    {
        
        appleX2 = random.nextInt((int)(SCREEN_WIDTH /UNIT_SIZE)) * UNIT_SIZE; 
        appleY2 = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move()
    {
        for( int i = bodyParts ; i > 0 ; i-- )
        {
            x[i] = x[i-1]; 
            y[i] = y[i-1];
        }

        checkBorder(); 

        switch(direction)
        {
            case 'U': 
                y[0] = y[0] - UNIT_SIZE; 
                break ; 
            case 'D': 
                y[0] = y[0] + UNIT_SIZE; 
                break ; 
            case 'L': 
                x[0] = x[0] - UNIT_SIZE; 
                break ; 
            case 'R': 
                x[0] = x[0] + UNIT_SIZE; 
                break ; 
        }
    }

    public void checkApple()
    {
        if( (x[0] == appleX) && ( y[0] == appleY ))
        {
            bodyParts++; 
            applesEaten++ ; 
            newApple1(); 
        }

        if( (x[0] == appleX2) && ( y[0] == appleY2 ))
        {
            bodyParts++; 
            applesEaten++ ; 
            newApple2(); 
        }
    }

    public void checkBorder()
    {
        if( x[0] < 0 )  x[0] = SCREEN_WIDTH - UNIT_SIZE;   // 565
        if( x[0] >= SCREEN_WIDTH )  x[0] = 0;

        if( y[0] < 0 )  y[0] = SCREEN_HEIGHT - UNIT_SIZE;  // 565
        if( y[0] >= SCREEN_HEIGHT ) y[0] = 0;
    }

    public void chekcCollisions()
    {
        // check if head collides with body 
        for( int i = bodyParts; i > 0 ; i-- )
        {
            if( ( x[0] == x[i]) && ( y[0] == y[i]) )
            {
                running = false ; 
            }
        }

        if( !running) timer.stop(); 
        
    }

    public void gameOver(Graphics g )
    {
        // score 
        g.setColor(Color.red); 
        g.setFont( new Font("Ink Free", Font.BOLD,40)); 
        FontMetrics metrics1 = getFontMetrics(g.getFont()); 
        g.drawString("Score:" + applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize() ); 
        // Game Over text 
        g.setColor(Color.red); 
        g.setFont( new Font("Ink Free", Font.BOLD, 75)); 
        FontMetrics metrics2 = getFontMetrics(g.getFont()); 
        g.drawString("Game Over!!",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2 ); 
        btnRes.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
       if(e.getSource() == btnRes) {
           restartGame();
       } 
       else if(running)
       {
            move(); 
            chekcCollisions(); 
            checkApple(); 
           
       }
       repaint(); 
    }
    
    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e )
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    if( direction != 'R')
                    {
                        direction = 'L'; 
                    }
                    break; 
                
                case KeyEvent.VK_RIGHT:
                    if( direction != 'L')
                    {
                        direction = 'R'; 
                    }
                    break; 
                case KeyEvent.VK_UP:
                    if( direction != 'D')
                    {
                        direction = 'U'; 
                    }
                    break; 
                case KeyEvent.VK_DOWN:
                    if( direction != 'U')
                    {
                        direction = 'D'; 
                    }
                    break; 
            }
        }
    }
}
