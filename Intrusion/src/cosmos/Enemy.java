/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package cosmos;
import javax.microedition.lcdui.*;
import cosmos.Stars;
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
public class Enemy {


   static Sprite lazer,turbo;
   

   static int x;
   static int y;
   static int width = GE.width;
   static int height = GE.height;
   static int[] turb = {0, 0, 0, 0, 1, 1, 1, 1, 1};

   static Graphics g = GE.g;

   public static final int LEFT = 1,RIGHT = 2,UP = 3,DOWN = 4;
    private static final float SHIP_TOP = 7F,SHIP_LEFT = 0F,SHIP_RIGHT = 14F;

    //private static Coordinat enemy;
    private static boolean right;
    private static boolean left;
    private static boolean up;
    private static boolean down;
    private static boolean fire;
    private static boolean zero;
    
    static public Vector enemy_counts = new Vector();
    private static final int BIG_SHIP = 0,SMALL_SHIP = 1,SMALL_SHIP_ALL = 2;
    static Sprite enemyShip[] = new Sprite[3];
    private static  int frame_lenght[] = new int[3];
//    так,значит ешё один вид выстрела будет.
//    Отлично,простым лазером неубить врага и он неможет.
//    Надо набитрать энергию в пушку зажатием кнопки.
//    Враг тоже так делает.это позваляет сделать уклонение,даёт время на расчёт
 

   public Enemy(){



        try {
            enemyShip[BIG_SHIP] = new Sprite(Image.createImage("/ship_anim2.png"), 42, 42);
            enemyShip[SMALL_SHIP] = new Sprite(Image.createImage("/enemy_small.png"), 20, 20);
            enemyShip[SMALL_SHIP_ALL] = new Sprite(Image.createImage("/ship3b.png"), 26, 18);
            turbo = new Sprite(Image.createImage("/turbo2.png"), 32, 22);
            lazer = new Sprite(Image.createImage("/lazer.png"));
        } catch (Exception e) {}
        
        frame_lenght[BIG_SHIP] = enemyShip[BIG_SHIP].getFrameSequenceLength();
        frame_lenght[SMALL_SHIP] = enemyShip[SMALL_SHIP].getFrameSequenceLength();
        frame_lenght[SMALL_SHIP_ALL] = enemyShip[SMALL_SHIP_ALL].getFrameSequenceLength();
       // enemy = new Coordinat();
       // enemy.ID = CollisionGrid.ENEMY;
        init();

       // int x = (int) (enemy.x );
       // int y = (int) (enemy.y );
      //  ship.setPosition(x, y);
        turbo.setFrameSequence(turb);
       // turbo.setPosition(x, y + 32);


       // new Lazer();
       // new Racket();


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

          int mapPos = 0;//начинаю прорисовывать с 0-й позиции массива
 
        for (int y = 0; y < _numHeight ; y ++) {
            for (int x = 0; x < _numWidth ; x ++) {
                
                if(pos[mapPos] == 1)
                addEnemy(Xpos + x * 30,Ypos + y * 20,random(3),SMALL_SHIP_ALL);

                mapPos++;           
            }
        }
   }
   
     public static void init() {
        begin = System.currentTimeMillis();
        
        setGroupPos(1,1,7,4,posKlin[0]) ;
        
       //addEnemy((GE.width / 2 - 22), 85,2,BIG_SHIP);
        //GameInfo.init();
    }
   
   
   public static void addEnemy(float x, float y,int tactic,int enemy_ID) {


        //delay = 150;

        Coordinat enemy = new Coordinat();
        enemy.ID = CollisionGrid.ENEMY;
        enemy.x = x;
        enemy.y = y;
        enemy.tactic = tactic;
        if(enemy_ID == BIG_SHIP)
            enemy.frame = SHIP_TOP;
        else
        enemy.frame = 0;
        enemy.enemy_ID = enemy_ID;
        enemy_counts.addElement(enemy);
        
        //System.out.print("Lazer.add\n");
        
        

    }
   
   
   public static void update(float delta) {

        if (enemy_counts.size() > 0) {
            for (int i = 0; i < enemy_counts.size(); i++) {
                updateEnemy((Coordinat) enemy_counts.elementAt(i),delta);
                
                
            }

        }
        
        updateLazer(delta);

}
   
   
   public static void draw() {

       

        if (enemy_counts.size() > 0) {
            for (int i = 0; i < enemy_counts.size(); i++) {
                drawEnemy((Coordinat) enemy_counts.elementAt(i));
                
                
            }

        }
        
        drawLazer();

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
                Coordinat lazer = (Coordinat) Lazer.coordinat[i];
                 float dx = lazer.x - (enemy.x+21) ;
                 float dy = lazer.y - (enemy.y+21) ;
                 float dis = (float) Math.sqrt(dx * dx + dy * dy);
                 
                 if(dis < 50){
                     if(dx < 0)
                         enemy.direction = RIGHT;
                     else
                         enemy.direction = LEFT;
                     
                    
                 }
                
                  if(enemy.direction == RIGHT && enemy.x >= width - 30)
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
               
             if (distanceX <= 21 && enemy.direction == -1) //если расстояние по X меньше 21 и корабль стоит неподвижно
             if (enemy.x + 21 >= width/2)//если корабль справа от центра,то двигаемся влево
                 enemy.direction = LEFT;
              else // или наоборот вправо
                 enemy.direction = RIGHT;
            //begin = System.currentTimeMillis();
        }
         else
             if (distanceX >= 42)//если нестреляет и расстояние от героя больше 42,то останавливаемся
                 enemy.direction = -1;
         break;
         
         case 3: /// тактика нападения
       
             if (Ship.t.x > enemy.x) {//если корабль героя справа,то двигаемся вправо
                 if (distanceX >= 21) 
                     enemy.direction = RIGHT;
                  else 
                     enemy.direction = -1;               
             }
             if (Ship.t.x < enemy.x) {//если корабль героя слева ,то двигаемся влево
                 if (distanceX >= 21) 
                     enemy.direction = LEFT;
                   else 
                     enemy.direction = -1;
             }   
         
         if (distanceX <= 21 && enemy.direction == -1) //если расстояние по X меньше 21 и корабль стоит неподвижно
             if (System.currentTimeMillis() - enemy.fire_begin > 300){
             addLazer(enemy.x + 20F, enemy.y + 42F);
             enemy.fire_begin = System.currentTimeMillis();
             }
             
             
}
             
           
          
         
                      
        // g.setColor(0xFFFF00);
       //g.drawString("right "+right, 0, 20, Graphics.TOP | Graphics.LEFT);
       //g.drawString("left "+left, 0, 30, Graphics.TOP | Graphics.LEFT);   
        


    }
     


static long begin;
static boolean flashing = true;

static void updateEnemy(Coordinat enemy,float delta ) {

    switch(enemy.enemy_ID){
        case BIG_SHIP:
            updateBigEnemy(enemy,delta );
            break;
            
       case SMALL_SHIP:
            //enemy.direction = DOWN;
            updateSmallEnemy(enemy,delta );
            break;  
           
       case SMALL_SHIP_ALL:
            //enemy.direction = DOWN;
            updateSmallEnemy(enemy,delta );
            break;    
    }
    
    
}





static void updateSmallEnemy(Coordinat enemy,float delta ) {

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
        if (y > height) 
            y = -100;
        

       y += 1 * delta;
        
        enemy.x = x ;
        enemy.y = y ;
        //enemy.frame += 0.1 * delta ;
        if(enemy.frame >= frame_lenght[SMALL_SHIP_ALL])
                    enemy.frame = 0F;
        //System.out.println("y "+ y);
        if (enemy.y >= 0 && enemy.y < height)
        GE.Grid.add(enemy);
        
    
    
}

     static void updateBigEnemy(Coordinat enemy,float delta ) {


       AI(enemy);
     
        float x = enemy.x ;
        float y = enemy.y ;
        float shipFrame = enemy.frame;

        switch(enemy.direction){
            
            case RIGHT:

              x = shipFrame == SHIP_RIGHT ? x + (5 * delta)  : x + (3 * delta);//если корабль полностью повернулся вправо то добавляю скорость (+5)
            shipFrame += 1 * delta;
             if(shipFrame > SHIP_RIGHT)
             shipFrame = SHIP_RIGHT;

         break;
                
         case LEFT:

              x = shipFrame == SHIP_LEFT ? x - (5 * delta) : x - (3 * delta);//добавляю скорость если shipFrame == SHIP_LEFT
            shipFrame -= 1 * delta;
             if(shipFrame < SHIP_LEFT)
             shipFrame = SHIP_LEFT;
         break;

         case UP:
              y -= 3 * delta;
         break;
         case DOWN:
         if (y < height - 55) {
             y += 3 * delta;
             turbo.setFrame(0);
         }
         break;
//         if (fire) {
//            // Lazer.add(x , y );
//         }
//
//         if (zero) {
//            // Racket.add(x, y);
//         }
         case -1:
               // если не нажат поворот то постепенно возврашаю положение корабля в средний фрейм т.е. SHIP_TOP
            if(shipFrame > SHIP_TOP)
             shipFrame -= 0.5 * delta;
                 
        //возврашаю положение корабля в средний фрейм т.е. SHIP_TOP
           if(shipFrame < SHIP_TOP)
              shipFrame += 0.5 * delta;

           
           break;
         
}


       if (x < 0) {
            x = 0;
             enemy.direction = -1;
        }
        if (x > width - 42) {
            x = width - 42;
             enemy.direction = -1;
        }
        if (y < 0) {
            y = 0;
        }


        enemy.x = x ;
        enemy.y = y ;
        enemy.frame = shipFrame ;
        GE.Grid.add(enemy);
        




    }
     
     static void drawEnemy(Coordinat enemy ) {
        int x = (int) enemy.x ;
        int y = (int) enemy.y ;
        int shipID = enemy.enemy_ID;
        enemyShip[shipID].setFrame((int)enemy.frame);
        enemyShip[shipID].setPosition(x,y);
        if(shipID == BIG_SHIP){
        turbo.setPosition(x + 5, y - 20);
        turbo.nextFrame();
        turbo.paint(g);
        }
        
        
        if(flashing){        
        enemyShip[shipID].paint(g);
         }
        
     }
     
     
     

      public static void shipCollize() {
          if (System.currentTimeMillis() - begin > 1000){//если уже было столкновение больше секунды назад
          GameInfo.еnemyLive --;//то отнимаю жизнь
          }


          begin = System.currentTimeMillis();


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

                
                if (t.y < 0 || t.y >= GE.height) {
                    bullet_counts.removeElementAt(i);
                } else {
                    GE.Grid.add(t);
                }

                
            }

        }

}
  
  static void drawLazer() {

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
