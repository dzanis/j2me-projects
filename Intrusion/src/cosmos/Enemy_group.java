/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package cosmos;
import javax.microedition.lcdui.*;
import java.util.Vector;
import main.CollisionGrid;
import main.GE;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import main.Coordinat;
import ship.GameInfo;
import ship.Lazer;
import ship.Ship;

/**
 *
 * @author dzanis
 * это класс врага
 */
public class Enemy_group {


   


   static int width = GE.width;
   static int height = GE.height;


   public static final int LEFT = 1,RIGHT = 2,UP = 3,DOWN = 4;
    
    static public Vector enemy_counts = new Vector();
    static Sprite enemyShip;
    private static  int frame_lenght;
//    так,значит ешё один вид выстрела будет.
//    Отлично,простым лазером неубить врага и он неможет.
//    Надо набитрать энергию в пушку зажатием кнопки.
//    Враг тоже так делает.это позваляет сделать уклонение,даёт время на расчёт
 

   public Enemy_group(){

        try {

            enemyShip= new Sprite(Image.createImage("/ship3b.png"), 26, 18);

        } catch (Exception e) {}
        
        frame_lenght= enemyShip.getFrameSequenceLength();

         for (int i = 0; i < enemyCount; i++) {
        Coordinat enemy = new Coordinat();
        enemy.ID = CollisionGrid.ENEMY;
        enemy_counts.addElement(enemy);
             
         }
         
        init();



    }
   
   static int enemyCount = 16;
   public static int enemyKillCount = 0;
   
     public static void init() {
        begin = System.currentTimeMillis();
        

        if(random(2) == 1)
           setGroupPos(1,-360,7,5,posKlin[1]) ;
        else
        setGroupPos(1,-360,7,4,posKlin[0]) ;
        
       //addEnemy((GE.width / 2 - 22), 85,2,BIG_SHIP);
        //GameInfo.init();
    }
   
    static int posKlin[][] = {
        {
            1, 1, 1, 1, 1, 1, 1,
            0, 1, 1, 1, 1, 1, 0,
            0, 0, 1, 1, 1, 0, 0,
            0, 0, 0, 1, 0, 0, 0,},
        {
            1,0,1,0,1,0,1,
            0,1,0,1,0,1,0,
            1,0,1,0,1,0,1,
            0,1,0,1,0,1,0,
            0,0,1,0,1,0,0,
        }
    };
   
   static  void setGroupPos(int Xpos, int Ypos,int _numWidth, int _numHeight,int pos[]) {

          int groupPos = 0;//начинаю прорисовывать с 0-й позиции массива
          int index = 0;
          int frameAll = random(frame_lenght);
          int  shuter = 0;
          int shuterframe = random(frame_lenght);
        for (int y = 0; y < _numHeight ; y ++) {
            for (int x = 0; x < _numWidth ; x ++) {
                int frame = frameAll;
                if(pos[groupPos] == 1){
                    int tactic = random(3)+1;
                    if(tactic == 3 && shuter < 2){//делаю только 2-х стреляюших и меняю их фрейм
                        shuter ++;
                        frame = shuterframe;
                    }
                    else
                        tactic = 2;
                    
                setEnemy(index,Xpos + x * 30,Ypos + y * 30,tactic,frame);
                index++;
                }

                groupPos++;           
            }
        }
   }
   
   
   
   
   public static void setEnemy(int index,float x, float y,int tactic,float frame) {

        Coordinat enemy = (Coordinat) enemy_counts.elementAt(index);
        enemy.x = x;
        enemy.y = y;
        enemy.tactic = tactic;
        enemy.frame = frame;
        enemy.direction = 0;
        //System.out.print("Lazer.add\n");
            
    }
   
   
   public static void update(float delta) {

       if(enemyKillCount >= enemyCount){
           if(random(10)<= 5)
           init();
          enemyKillCount = 0; 
       }
        if (enemy_counts.size() > 0) {
            for (int i = 0; i < enemy_counts.size(); i++) {
                update((Coordinat) enemy_counts.elementAt(i),delta);
                
                
            }

        }
        
        updateLazer(delta);

}
   
   
   public static void draw(Graphics g) {

       

        if (enemy_counts.size() > 0) {
            for (int i = 0; i < enemy_counts.size(); i++) {
                drawEnemy(g,(Coordinat) enemy_counts.elementAt(i));
                
                
            }

        }
        
        drawLazer(g);

}

  


    /**
     * Искуственный интелект врага
     */   
     static void AI(Coordinat enemy) {
        
//         if (System.currentTimeMillis() - begin > 1000) //если уже было 
//             return;
          
         
        if(Ship.t == null)
            return;

        
 int distanceX = (int) Math.abs(Ship.t.x - enemy.x);//расстояние по оси X  между героем и врагом
 int distanceY = (int) Math.abs(Ship.t.y - enemy.y);//расстояние по оси Y  между героем и врагом

// http://www.youtube.com/watch?v=zjQ2XWAAwyU  Adobe flash cs4 Game tutorial: Enemy AI
//             float run = ship.x - enemy.x ;
//             float dis = (float) Math.sqrt(run );
           // System.out.println("distanceX "+distanceX+"\r");
            
        
           

         switch(enemy.tactic){
             
              case 1:  /// тактика уклонения врага от лазера
                  enemy.direction = -1; 
                
            for (int i = 0; i < Lazer.coordinat.length; i++) {
                Coordinat lazer = Lazer.coordinat[i];
                 float dx = lazer.x - (enemy.x+13) ;
                 float dy = lazer.y - (enemy.y+9) ;
                 float dis = (float) Math.sqrt(dx * dx + dy * dy);
                 
                 if(dis < 100){
                     if(dx < 0)
                         enemy.direction = RIGHT;
                     else
                         enemy.direction = LEFT;
                     
                    
                 }
                
                  if(enemy.direction == RIGHT && enemy.x >= width - 27)
                     enemy.direction = LEFT;
                 else
                    if(enemy.direction == LEFT && enemy.x <= 1)
                     enemy.direction = RIGHT;  
                     
                
            }

         
          // else
        //enemy.direction = -1;
                     
         
               break;   
             
             
             
             
             
             case 2:  /// тактика уклонения врага от выстрелов         
         if(GE.fire ){//если герой стреляет то уклоняемся в сторону 
               
             if (distanceX <= 16 && enemy.direction == -1) //если расстояние по X меньше 21 и корабль стоит неподвижно
             if (enemy.x + 16 >= width/2)//если корабль справа от центра,то двигаемся влево
                 enemy.direction = LEFT;
              else // или наоборот вправо
                 enemy.direction = RIGHT;
            //begin = System.currentTimeMillis();
        }
         else
             if (distanceX >= 26)//если нестреляет и расстояние от героя больше 42,то останавливаемся
                 enemy.direction = -1;
         break;
         
         case 3: /// тактика нападения
       
             if (Ship.t.x > enemy.x) {//если корабль героя справа,то двигаемся вправо
                 if (distanceX >= 13) 
                     enemy.direction = RIGHT;
                  else 
                     enemy.direction = -1;               
             }
             if (Ship.t.x < enemy.x) {//если корабль героя слева ,то двигаемся влево
                 if (distanceX >= 13) 
                     enemy.direction = LEFT;
                   else 
                     enemy.direction = -1;
             }   
         
         if (distanceX <= 21 && enemy.direction == -1) //если расстояние по X меньше 21 и корабль стоит неподвижно
             if (System.currentTimeMillis() - enemy.fire_begin > 300){
             addLazer(enemy.x + 12F, enemy.y + 16F);
             enemy.fire_begin = System.currentTimeMillis();
             }
             
             
}
             
           
          
         
                      
        // g.setColor(0xFFFF00);
       //g.drawString("right "+right, 0, 20, Graphics.TOP | Graphics.LEFT);
       //g.drawString("left "+left, 0, 30, Graphics.TOP | Graphics.LEFT);   
        


    }
     


static long begin;
static boolean flashing = true;







static void update(Coordinat enemy,float delta ) {

     AI(enemy);
     
        float x = enemy.x ;
        float y = enemy.y ;

        switch(enemy.direction){
            
            case RIGHT:
             x += 1 * delta;//вправо скорость (+5)
         break;
                
         case LEFT:
              x -=  1 * delta;//влево скорость (+5)
         break;

         case UP:
              y -= 1 * delta;
         break;
         case DOWN:
        // if (y < height - 55) 
             y += 1 * delta;
         
         break;

         
}


       if (x < 0) {
            x = 0;
            enemy.direction = -1;
        }
        if (x > width - 29) {
            x = width - 29;
             enemy.direction = -1;
        }
//        if (y < 0) {
//            y = 0;
//        }
        if (y > height) {
            y = -360;
            enemyKillCount++;
        }
        

       y += 1 * delta;
        
        enemy.x = x ;
        enemy.y = y ;
        //enemy.frame += 0.1 * delta ;
        if(enemy.frame >= frame_lenght)
                    enemy.frame = 0F;
        //System.out.println("y "+ y);
        if (enemy.y >= 0 && enemy.y < height)
        GE.Grid.add(enemy);
        
    
    
}
     
     static void drawEnemy(Graphics g,Coordinat enemy ) {
        int x = (int) enemy.x ;
        int y = (int) enemy.y ;
        enemyShip.setFrame((int)enemy.frame);
        enemyShip.setPosition(x,y);
        enemyShip.paint(g);
        
     }
     
     
     

      public static void shipCollize(Coordinat enemy) {
         
          

      }


 private static int random(int i){
        return (GE.random.nextInt() >>> 1) % i;
    }
 
 
 static public Vector bullet_counts = new Vector();
 
 //static long fire_begin;
 
 
 
  static void addLazer(float x, float y) {


        //delay = 150;

        Coordinat t = new Coordinat();
        t.ID = CollisionGrid.ENEMY_LAZER;
        t.x = x ;
        t.y = y ;
        t.speed = 4F;
        bullet_counts.addElement(t);

        //System.out.print("Lazer.add\n");
        
        

    }
  
  static void updateLazer(float delta) {

       

        if (bullet_counts.size() > 0) {
            for (int i = 0; i < bullet_counts.size(); i++) {
                Coordinat t = (Coordinat) bullet_counts.elementAt(i);
                t.y += t.speed * delta;

                
                if (t.y < 0 || t.y >= height) {
                    bullet_counts.removeElementAt(i);
                } else {
                    GE.Grid.add(t);
                }

                
            }

        }

}
  
  static void drawLazer(Graphics g) {

        if (bullet_counts.size() > 0) {
            for (int i = 0; i < bullet_counts.size(); i++) {
                Coordinat t = (Coordinat) bullet_counts.elementAt(i);
                //lazer.setPosition((int)t.x,(int) t.y );
               // lazer.paint(g);
                g.setColor(0xff0000);
                g.fillRect((int)t.x,(int) t.y, 3, 3);
               // System.out.print(t.y+"Lazer\n");
                
            }

        }

}
  


}
