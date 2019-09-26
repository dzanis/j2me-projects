package cosmos;

import java.util.Vector;
import main.GE;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import main.CollisionGrid;
import main.Coordinat;

public class Explosion{

   static Vector v;
   static Sprite explosion;
   static int frame_lenght;

    

    public  Explosion() {
        v = new Vector();
        try {

        explosion = new Sprite(Image.createImage("/explosion.png"),32,32);
         } catch (Exception ex) {ex.printStackTrace();}
        frame_lenght = explosion.getFrameSequenceLength();

    }

    public static void add(float speed,float x, float y){

       Coordinat t = new Coordinat();
        t.x = x;
        t.y = y;
        t.speed = speed;
        t.frame = 0F;
        t.frame_speed = 0.25F;
        v.addElement(t);

    }
    
    public static void update(float delta){

        if (v.size()>0)
            for(int i=0;i<v.size();i++){
                Coordinat t = (Coordinat)v.elementAt(i);
                t.frame += t.frame_speed * delta;
                t.y += t.speed * delta;
                if ((int)t.frame >= frame_lenght)
                    v.removeElementAt(i);
            }
    }

    public static void draw(Graphics g){

        if (v.size()>0)
            for(int i=0;i<v.size();i++){
                Coordinat t = (Coordinat)v.elementAt(i);
                explosion.setFrame((int)t.frame);
                explosion.setPosition((int)t.x,(int)t.y);
                explosion.paint(g);
 
            }
    }

}
