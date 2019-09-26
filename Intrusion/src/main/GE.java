package main;


import UI.*;
import ship.Ship;
import cosmos.*;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import ship.GameInfo;



public class GE extends GameCanvas  {


   
   private static int GRID_SIZE = 42;//размер ячейки должен быть не менее большего из объектов


   public static int width;
   public static int height;
   public static Graphics g;
   public static Image buffer;

   public static boolean right,left,up,down,fire,star,zero;
   public static  fntFont font;

   public static Random random;
   public static  Menu menu;
   public static CollisionGrid Grid;

   public static final int STAR = -2;
   public static final int INTRO = -1;
   public static final int MENU = 0;
   public static final int GAME_PROCESS = 1;
   public static final int GAME_PAUSE = 2;
   public static final int GAME_OVER = 3;
   public static final int ENEMY = 4;
   public static final int INFA = 5;
   public static int PROCESS = MENU;
   public static String _8bit = "";//

    public GE()  {
        super(false);
        //0x003c9c - синий
        //ffb600 - жёлтый
        font = new fntFont("/menu/13.fnt", 0xffb600, 0);
        setFullScreenMode(true);
        width = getWidth();
        height = getHeight();
//        width = 176;
//        height = 220;
//       width = 128;
//        height = 160;
       // buffer = Image.createImage(width, height);
       // g = buffer.getGraphics();
        g = getGraphics();
        random = new Random();

        
        new Intro();

        Grid = new CollisionGrid(width,height, GRID_SIZE);
        menu = new Menu();
        Stars stars = new Stars();
        new Explosion();
        new Asteroids();
        new GameInfo();
        new Ship();
        new Enemy_group();

    }
    
    
    private long last_time;
    public static long current_time;
    public static float delta;
    final float MAXFPS = 1000/60;//это максимальный fps


    public void process()
    {
        deltaUpdate();
        update(delta);
        render();

    }

    private final void deltaUpdate()
    {
        last_time = current_time;
        current_time = System.currentTimeMillis();
        delta = ((float) (current_time - last_time))/MAXFPS ;

    }
    
    
    int count = 0;
    
    private final void update(float delta) {
         switch (PROCESS) {
                       
           case STAR:
                Stars.update(delta);
               break;
           case MENU:
               Stars.update(delta);
               break; 
           case GAME_PROCESS:    
              Stars.update(delta);
              Ship.update(delta);
              Asteroids.update(delta); 
              Enemy_group.update(delta);
              Explosion.update(delta);
               break;
            case  ENEMY:
              Stars.update(delta);
              Ship.update(delta);
             // Asteroids.update(delta);
              Enemy_group.update(delta);
              Explosion.update(delta);
//              count++;
//              if(count == 1000)
//                  Intrusion.runnable = false;
              
               break;    

         }
         
         Grid.update();
        
    }
    


   private final void render() {


   // Очистка содержимого дисплея (чёрный цвет)
   g.setColor(0);
   g.fillRect(0, 0, width, height);
   
       switch (PROCESS) {
           
           case STAR:             
              Stars.draw(g);
              break;

           case INTRO:
               Intro.draw();
               break;

           case MENU:
               menu.draw();
               break;
           case GAME_PROCESS:
               Stars.draw(g);
               Ship.draw();
               //Enemy.draw();
               Asteroids.draw();
               Enemy_group.draw(g);
               Explosion.draw(g);
               if (star) {
                   PROCESS = GAME_PAUSE;
                   star = false;
               }
               GameInfo.Draw(g);

               break;
           case GAME_PAUSE:
               font.drawString(g, "ПАУЗА", width / 2, height / 2, Graphics.VCENTER);
               if (star) {
                   PROCESS = GAME_PROCESS;
                   star = false;
               }
               break;

           case GAME_OVER:
               font.drawString(g, "ИГРА ОКОНЧЕНА", width / 2, height / 2, Graphics.VCENTER);
               font.drawString(g, "жми *", width / 2, height - font.getHeight(), Graphics.VCENTER);

               if (star) {
                   PROCESS = MENU;
                   Ship.init();
                   Asteroids.init();
               }
               break;

               case  ENEMY:

               Stars.draw(g);
               Ship.draw();
               //Asteroids.draw();
               Enemy_group.draw(g);
               Explosion.draw(g);
             
               GameInfo.Draw(g);
               //Asteroids.draw();
//               if (star) {
//                   PROCESS = GAME_PAUSE;
//                   star = false;
//               }
               //GameInfo.Draw(g);

               break;
               case  INFA:
               TextView.draw();
               break;    

       }
       
       g.setColor(0xFF0000);
       g.drawString(getFPS(), 0, 0, Graphics.TOP | Graphics.LEFT);
       //g.drawString(getHEAP(), 0, 20, Graphics.TOP | Graphics.LEFT);
       
      // repaint();
      // serviceRepaints();
       flushGraphics();

    }






//    public void paint(Graphics g) {
//
//        g.drawImage(buffer, 0, 0, 20);
//        
//
//    }

   

    

  public void keyPressed(int kc) {


        switch (kc) {
            case KEY_STAR:star = true; break;
            case KEY_NUM1:left = true;up = true;break;
            case KEY_NUM3:right = true;up = true;break;
            case KEY_NUM7:left = true;down = true;break;
            case KEY_NUM9:right = true;down = true;break;
            case KEY_NUM0:zero = true; break;
        }
        switch (getGameAction(kc)) {
            case RIGHT:right = true; break;
            case LEFT:left = true;break;
            case UP:up = true; break;
            case DOWN: down = true;break;
            case FIRE:fire = true;break;
        }
    }

    public void keyRepeated(int kc) {     
    }

    public void keyReleased(int kc) {
        switch (kc) {          
            case KEY_STAR:star = false;break;
            case KEY_NUM1:left = false;up = false; break;
            case KEY_NUM3:right = false;up = false;break;
            case KEY_NUM7:left = false; down = false;break;
            case KEY_NUM9:right = false;down = false; break;
            case KEY_NUM0:zero = false; break;  
        }
        switch (getGameAction(kc)) {

           case RIGHT:  right = false;   break;
           case LEFT:   left = false;    break;
           case UP:     up  = false;     break;
           case DOWN:   down = false;    break;
           case FIRE:   fire = false;    break;
        }

    }
    

    

   public static long FPS, FPS_Count, FPS_Start;

    static String getFPS() {
        FPS_Count++;
        if (System.currentTimeMillis() - FPS_Start >= 1000) {
            FPS = FPS_Count;
            FPS_Count = 0;
            FPS_Start = System.currentTimeMillis();
        }
        return "fps: " + Long.toString(FPS);
    }
    
    
    
    static String getHEAP() {
         
        return "Занято: " + Long.toString(
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024
                )+" Kb";
    }
    
}
