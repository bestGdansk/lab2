import java.awt.*;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Sterowanie extends JFrame{

    private Image       tlo;
    private Image       tlo1;
    private Image       tlo2;
    private Image       sterowiec;
    private Image       sterowiec1;
    private Image       sterowiec2;
    private Image       balon;
    private boolean     klawisze[];
    private int         wsp[];
    private Timer       zegar;
    private boolean     notPressed = true;
    private boolean     isCollision = false;


    class Zadanie extends TimerTask{

        public void run()
        {

            if(klawisze[0])
                wsp[1]-=1;
            if(klawisze[1])
                wsp[1]+=1;

            if(klawisze[2])
                wsp[0]-=1;
            if(klawisze[3])
                wsp[0]+=1;
            //obrót wiatraczka przy ruchu lewo||prawo
            if(sterowiec != balon && (klawisze[2] || klawisze[3]))
                sterowiec = (sterowiec == sterowiec1) ? sterowiec2 : sterowiec1;

           wsp[0] = (wsp[0]<0)?0:wsp[0];
           wsp[0] = (wsp[0]>720)?720:wsp[0];
           wsp[1] = (wsp[1]<20)?20:wsp[1];
           wsp[1] = (wsp[1]>540)?540:wsp[1];
            repaint();
        }

  }
    
    Sterowanie(){
        super("Grafika 2D - sterowanie");
        setBounds(50,50,800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        createBufferStrategy(2);

        klawisze        = new boolean[4];
        wsp             = new int[2];
        tlo = tlo1      = new ImageIcon("obrazki/tlo1.jpg").getImage();
        tlo2            = new ImageIcon("obrazki/tlo2.jpg").getImage();
        sterowiec1      = new ImageIcon("obrazki/sterowiec.png").getImage();
        sterowiec2      = new ImageIcon("obrazki/sterowiec2.png").getImage();
        balon           = new ImageIcon("obrazki/balon.png").getImage();
        sterowiec = balon;

        wsp[0] = 20;
        wsp[1] = 40;


        zegar = new Timer();
        zegar.scheduleAtFixedRate(new Zadanie(),0,20);

        this.addKeyListener(new KeyListener(){

            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:      klawisze[0] = true; break;
                    case KeyEvent.VK_DOWN:    klawisze[1] = true; break;
                    case KeyEvent.VK_LEFT:    klawisze[2] = true; break;
                    case KeyEvent.VK_RIGHT:   klawisze[3] = true; break;
                    case KeyEvent.VK_ENTER:
                        if (notPressed)
                            tlo = (tlo == tlo1) ? tlo2 : tlo1;
                        break;
                    case KeyEvent.VK_SPACE:
                        if(notPressed)
                            sterowiec = (sterowiec == balon) ? sterowiec1 : balon;
                }
                notPressed = false;

            }
            public void keyReleased(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:      klawisze[0] = false; break;
                    case KeyEvent.VK_DOWN:    klawisze[1] = false; break;
                    case KeyEvent.VK_LEFT:    klawisze[2] = false; break;
                    case KeyEvent.VK_RIGHT:   klawisze[3] = false; break;
                }
                notPressed = true;
            }

            public void keyTyped(KeyEvent e){

            }
        }
    );
  }

    public static void main(String[] args)
    {
        Sterowanie okno = new Sterowanie();
        okno.repaint();

    }

    public void paint(Graphics g)
    {

        BufferStrategy bstrategy = this.getBufferStrategy();
        Graphics2D g2d = (Graphics2D)bstrategy.getDrawGraphics();

        g2d.drawImage(tlo, 0,0,null);
        g2d.drawImage(sterowiec,wsp[0],wsp[1],null);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial",Font.BOLD,20));
        int x2Points[] = {10 , 120, 200 ,300, };
        int y2Points[] = {450, 250, 200,450 , };
        GeneralPath polyline =
                new GeneralPath(GeneralPath.WIND_EVEN_ODD, x2Points.length);
        polyline.moveTo (x2Points[0], y2Points[0]);
        for (int index = 1; index < x2Points.length; index++) {
            polyline.lineTo(x2Points[index], y2Points[index]);
        }
        g2d.fill(polyline);
        g2d.drawString("Wsp x: " + wsp[0], 5, 55);
        g2d.drawString("Wsp y: "+ wsp[1], 5, 100);

        Rectangle bounds = getBounds(sterowiec);
        if(polyline.intersects(bounds))
            g2d.drawString("KOLIZJA", 15, 155);

        g2d.dispose();
        bstrategy.show();
    }
    public Rectangle getBounds(Image sterowiec)
    {
        //wymiary balonu 43x63
        //wymiary sterowca 69x42
        if(sterowiec == balon)
            return new Rectangle(wsp[0]+19, wsp[1]+8, 43, 63);
        else
            return new Rectangle(wsp[0]+7, wsp[1]+14, 69, 42);
    }
}


