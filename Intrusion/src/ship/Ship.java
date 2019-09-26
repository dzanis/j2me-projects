package ship;


import cosmos.Stars;
import main.CollisionGrid;
import main.GE;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import main.Coordinat;

public  class Ship {

   static Sprite turbo;
   static public Sprite ship;
   static Image shit;
   static int x;
   static int y;
   static int width = GE.width;
   static int height = GE.height;
   static int[] turb = {0, 0, 0, 0, 1, 1, 1, 1, 1};

   static Graphics g = GE.g;

    
    private static final float SHIP_TOP = 7F,SHIP_LEFT = 0F,SHIP_RIGHT = 14F;
    private static float shipFrame = SHIP_TOP;
    public static Coordinat t;

   public Ship(){



        try {
            ship = new Sprite(Image.createImage(GE._8bit+"/ship_anim.png"), 42, 42);
            turbo = new Sprite(Image.createImage(GE._8bit+"/turbo.png"), 32, 20);
            shit = Image.createImage("/shit.png");
        } catch (Exception e) {}

        t = new Coordinat();
        t.ID = CollisionGrid.SHIP;
        init();
        
        int x = (int) (t.x );
        int y = (int) (t.y );
        ship.setPosition(x, y);
        turbo.setFrameSequence(turb);
        turbo.setPosition(x, y + 32);


        new Lazer();
        new Racket();
        

    }

    public static void init() {
        t.x = (GE.width / 2 - 22);
        t.y = (GE.height - 55);
        GameInfo.init();
    }

   

static long begin;
static long fire_begin;
static boolean flashing;


 public static void update(float delta) {
         float x = t.x ;
         float y = t.y ;


         if (GE.right) {      
             x = shipFrame == SHIP_RIGHT ? x + (5 * delta)  : x + (3 * delta);//если корабль полностью повернулся вправо то добавляю скорость (+5)
            shipFrame += 1 * delta;
             if(shipFrame > SHIP_RIGHT)
             shipFrame = SHIP_RIGHT;
  
         }
        else // если не нажат поворот то постепенно возврашаю положение корабля в средний фрейм т.е. SHIP_TOP
            if(shipFrame > SHIP_TOP)
             shipFrame -= 0.5 * delta;

         if (GE.left) {
             x = shipFrame == SHIP_LEFT ? x - (5 * delta) : x - (3 * delta);//добавляю скорость если shipFrame == SHIP_LEFT
            shipFrame -= 1 * delta;
             if(shipFrame < SHIP_LEFT)
             shipFrame = SHIP_LEFT;
  
         }
        else //возврашаю положение корабля в средний фрейм т.е. SHIP_TOP
           if(shipFrame < SHIP_TOP)
             shipFrame += 0.5 * delta;

         
         if (GE.up) {
             y -= 3 * delta;
         }
         if (GE.down && y < height - 55) {
             y += 3 * delta;
             turbo.setFrame(0);
         }
         
         
         if (x < 0) {
            x = 0;
        }
        if (x > width - 42) {
            x = width - 42;
        }
        if (y < 0) {
            y = 0;
        }
         
         if (GE.fire) {
             if (GE.current_time - fire_begin > 150) {
            Lazer.add(x + 16, y );
            
           //Lazer.add(x + 5F, y + 10F);
           //Lazer.add(x + 35F, y + 10F);
            
            GameInfo.lazerFire();
            fire_begin = GE.current_time; 
            }
        
             
         }

         if (GE.zero) {
             Racket.add(x, y);
         }


       if (GE.current_time - begin < 1000) //маргание брони корабля 1 секунду
                flashing = !flashing;
        else
        {
            if(GameInfo.shipLive <= 0){
              GE.PROCESS = GE.GAME_OVER;
              return;
            }
            flashing = true;

         }

        t.x = x ;
        t.y = y ;
        GE.Grid.add(t);
        Lazer.update(delta);
     }


     public static void draw() {

        
        int x = (int) (t.x );
        int y = (int) (t.y );
        
        
        Lazer.draw();
        Racket.draw();
        ship.setFrame((int)shipFrame);
        ship.setPosition(x,y);
        turbo.setPosition(x + 6, y + 35);
        turbo.nextFrame();
        turbo.paint(g);
        ship.paint(g);
        
        if(!flashing){       
        g.drawImage(shit, x - 5, y - 2, 20);
         }

        
        

        
        
    }
     
    
     
     
     
     

      public static void shipCollize() {
          if (GE.current_time - begin > 1000){//если уже было столкновение больше секунды назад
          GameInfo.shipLive -= 0.1;//то отнимаю жизнь
          }
        
     
          begin = GE.current_time;
          

      }


}
