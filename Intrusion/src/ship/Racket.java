package ship;


import main.GE;
import javax.microedition.lcdui.*;
import java.util.Vector;
import java.io.*;
import javax.microedition.lcdui.game.Sprite;
import main.Coordinat;

/**
 *
 * @author dzanis
 */
public class Racket {

    static public Vector bullet_counts;
    static Graphics g = GE.g;
    static Image raketa;
    static int scr_W = GE.width;// Ширина
    static int scr_H = GE.height;// и высота дисплея
    static Sprite rturbo;
    static int[] turb = {0, 0, 0, 0, 1, 1, 1, 1, 1};


    public Racket() {

        bullet_counts = new Vector();
        try {
           // raketa = Image.createImage("/racket.png");
            rturbo = new Sprite(Image.createImage(GE._8bit+"/rocket.png"), 9, 30);
        } catch (Exception e) {
        }
        rturbo.setFrameSequence(turb);

    }

    static long begin;

    public static void add(float x, float y) {
        if (System.currentTimeMillis() - begin < 250) {
            return;
        }
        Coordinat t = new Coordinat();
        t.x = x + 17;
        t.y = y + 14;
        t.speed = -0.5F;
        bullet_counts.addElement(t);
        begin = System.currentTimeMillis();

    }
    

    public static void draw() {


        if (bullet_counts.size() > 0) {
            for (int i = 0; i < bullet_counts.size(); i++) {
                Coordinat t = (Coordinat) bullet_counts.elementAt(i);

                t.speed += -0.05;
                t.y +=  t.speed;

                if (t.y + 14 < 0) {
                    bullet_counts.removeElementAt(i);
                }

                //g.drawImage(raketa, t.x, t.y, 20);
                rturbo.setPosition((int)t.x,(int) t.y );
                rturbo.nextFrame();
                rturbo.paint(g);

            }

        }




    }


}

  
