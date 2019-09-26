package ship;

import cosmos.Asteroids;
import main.GE;
import main.Coordinat;
import javax.microedition.lcdui.*;
import java.util.Vector;
import javax.microedition.lcdui.game.Sprite;
import main.CollisionGrid;

/**
 *
 * @author dzanis
 */
public class Lazer {

    public static Coordinat coordinat[] = new Coordinat[10];
    static Graphics g = GE.g;
    static int scr_W = GE.width;// Ширина
    static int scr_H = GE.height;// и высота дисплея
    static public Sprite lazer;
   // static int[] turb = {0, 0, 0, 0, 1, 1, 1, 1, 1};


    public Lazer() {

        for (int i = 0; i < coordinat.length; i++) {
            coordinat[i] = new Coordinat();
            coordinat[i].ID = CollisionGrid.LAZER;
            coordinat[i].y = -30;
        }
        try {
            lazer = new Sprite(Image.createImage("/plasma.png"));
        } catch (Exception e) {
        }


    }


static int count;
    public static void add(float x, float y) {


        //delay = 150;

        coordinat[count].x = x ;
        coordinat[count].y = y ;
        coordinat[count].speed = -8F;
        count++;
        if(count >= coordinat.length)
            count = 0;

        //System.out.print("Lazer.add\n");
        
        

    }

 public static void update(float delta) {

       
    
            for (int i = 0; i < coordinat.length; i++) {
                coordinat[i].y += coordinat[i].speed * delta;     
                if (coordinat[i].y > 0)
                    GE.Grid.add(coordinat[i]);
                

            }

        

}

    public static void draw() {

       


            for (int i = 0; i < coordinat.length; i++) {
                lazer.setPosition((int)coordinat[i].x,(int) coordinat[i].y );
                lazer.paint(g);
               // System.out.print(t.y+"Lazer\n");
                
            }

       

}
    

 /**
 * сортировка по координатам y
 * меньшие значения y будут начинаться с начала вектора
 */
    public static void quickSort(Vector a,int low, int high) {  
     
    int i = low;
    int j = high;
    //выбираем середину в качестве опорного элемента
    Coordinat ser = (Coordinat)a.elementAt(low + (high-low)/2);
    
    //сдвигаем меньшие члены влево от опорного, а большие вправо  
    while (i <= j) {
       
        while (((Coordinat)a.elementAt(i)).y < ser.y)  ++i;
        while (((Coordinat)a.elementAt(j)).y > ser.y)  --j;
        
        if ( i <= j )
        {
                //Меняем местами
                Coordinat t= (Coordinat)a.elementAt(i);
                a.setElementAt(a.elementAt(j),i);
                a.setElementAt(t,j);

            ++i;
            --j;
        }
        
    } 
  
    //теперь также сортируем левую и правую часть  
    if (low < j)
        quickSort(a,low, j);
    if (i < high)
        quickSort(a,i, high);
}


}

