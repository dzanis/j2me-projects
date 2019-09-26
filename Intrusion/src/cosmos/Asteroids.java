package cosmos;

import main.Coordinat;
import java.util.Vector;
import ship.Lazer;
import ship.Ship;
import main.GE;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import main.CollisionGrid;
import ship.GameInfo;
/**
 *
 * @author dzanis
 */
public class Asteroids {


    public static Sprite aster[]= new Sprite[2];
    static Graphics g = GE.g;
    static float angle [] = {0F,0.1F,-0.1F};
    static float speed [] = {1F,1.5F,3F,3.5F};
    static float rotatespeed [] = {0.25F,0.5F,0.75F,1F};
    static int width = GE.width;// Ширина
    static int height = GE.height;// и высота дисплея
    public static Coordinat coordinat[] = new Coordinat[10];
    private static  int frame_lenght[] = new int[2];

    

    public Asteroids() {



        try {
            aster[0] = new Sprite(Image.createImage("/aster_big.png"), 32, 32);
            aster[1] = new Sprite(Image.createImage("/aster_anim.png"), 32, 32);
        } catch (Exception ex) {
        }
         frame_lenght[0] = aster[0].getFrameSequenceLength();
         frame_lenght[1] = aster[1].getFrameSequenceLength();
        init();
        
    }

    public static void init() {
        for (int i = 0; i < coordinat.length; i++) {
            coordinat[i] = new Coordinat();
            coordinat[i].ID = CollisionGrid.ASTER;
            genPos(i);

        }

        for (int i = 0; i < coordinat.length; i++) {
            //coordinat[i] = new asterCoordinat();
            sort(i);

        }
    }

    //генерация позиции астеройда с под номером i
    private static void genPos(int i) {

       // coordinat[i].x = random(width - 25);
       // coordinat[i].y = random(height*2)-height*2;
        coordinat[i].x = (random(width/32)*32);
        coordinat[i].y = ((random(height/32)*32)-height);
        //coordinat[i].angle = angle[random(3)];
        coordinat[i].angle = 0;
        int num = random(4);
        coordinat[i].speed = speed [num];
        //coordinat[i].speed = 0.05F;
        coordinat[i].frame_speed = rotatespeed [num];       
        coordinat[i].frame = 0F;
        if(num != 0)
            num = 1;
        coordinat[i].tactic = num;


    }


  

    private static void sort(int num) {

         for (int i = 0; i < coordinat.length; i++) {
            //Проверка растояния между астеройдами.
            if (coordinat[i].y <= 0 - 32) {
                if (i != num) {
//                    if (RoundInRound(
//                            coordinat[i].x, coordinat[i].y, 32,
//                            coordinat[num].x, coordinat[num].y, 32)) {
//                        coordinat[num].y = height;
//                    }
                    if(checkCollisions(coordinat[i].x, coordinat[i].y, coordinat[num].x, coordinat[num].y))
                        coordinat[num].y = height;
                }
            }

        }


        int count_Aster = 0;
        for (int i = 0; i < coordinat.length; i++) {
            //Проверка столкновение астеройдов.

            if (i != num) {
                if (coordinat[i].y < height) {
                    if (coordinat[num].x == coordinat[i].x) {//если находятся на одной оси X
                        count_Aster++;
                        if (coordinat[num].y < coordinat[i].y) {//если координата у [num] по Y меньше,т.е. астер выше
                           // System.out.print(num + " < " + i + "\n");
                            dont_Collize(num, i);

                        } else {//если координата у [num] по Y больше,т.е. астер ниже
                           // System.out.print(num + " > " + i + "\n");
                            dont_Collize(i, num);

                        }


                    }
                }


            }


        }

        //если на пути у астеройда ничего нет то ставлю максимальную скорость
        if (count_Aster == 0) {
            coordinat[num].speed = speed[3];
           // coordinat[num].speed = 0.05F;
            coordinat[num].frame_speed = rotatespeed[3];
        } else if (count_Aster >= 3)//если на одной линии уже три астеройда,то ставлю его за экран
        {
            coordinat[num].y = height;
        }

    }


   //если один догонит другого то астеройды столкнутся
   // метод dont_Collize недаёт астеройдам так вылетать
   // up_Aster - астеройд идуший сверху
   // down_Aster - астеройд  ниже
   //*****************************
   //*****up_Aster****************
   //********|********************
   //********|********************
   //********|********************
   //*****down_Aster**************
   private static void dont_Collize(int up_Aster,int down_Aster){

       //если скорость у верхнего меньше или равна скорости нижнего
       //то верхний никак недогонит нижний астеройд
       if(coordinat[up_Aster].speed <= coordinat[down_Aster].speed)
           return ;

       float distance = coordinat[down_Aster].y - coordinat[up_Aster].y;//расстояние между астеройдами

       //    http://animbook.mirmap.com/content/view/31/43/
       //время=расстояние/скорость t=s/v
       // время через которое первый астеройд догонит второй,вычсляется так t= s/(v1-v2)
       float T = distance/((coordinat[up_Aster].speed )-(coordinat[down_Aster].speed ));
       //расстояние=скорость*время s=v*t
       //узнаём положение нижнего астеройда когда его догонит верхний
       float S = coordinat[down_Aster].y +((coordinat[down_Aster].speed ) * T);

      //System.out.print(S + " S\n");
           if (S < height ) {//если верхний догонит нижнего на дисплее,то уменьшаем скорость верхнего
               coordinat[up_Aster].speed = coordinat[down_Aster].speed ;
               coordinat[up_Aster].frame_speed = coordinat[down_Aster].frame_speed;

       }




   
   }



   



    public static boolean RoundInRound(float x1, float y1, float r1, float x2, float y2, float r2) {
        
//        r1 /=2;
//        x1 += r1;
//        y1 += r1;
//        r2/=2;
//        x2 += r2;
//        y2 += r2;
        
         
        return (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) <= r1 + r2);
    }
    
    public static boolean checkCollisions(float x1,float y1,float x2,float y2) {
        return (x1+32>x2&&x1<x2+32&&y1+32>y2&&y1<y2+32); }
    

 public static void update(float delta) {

        for (int i = 0; i < coordinat.length; i++) {

            //coordinat[i].x += coordinat[i].angle;
            coordinat[i].y += coordinat[i].speed * delta;
                coordinat[i].frame += coordinat[i].frame_speed * delta;
                if(coordinat[i].frame >= frame_lenght[coordinat[i].tactic])
                    coordinat[i].frame = 0F;

          if (coordinat[i].y > height) {
                genPos(i);
                sort(i);
               } 
//              else if (aster.collidesWith(Ship.ship, true)) {
//                aster_explosion.CrExplosion(coordinat[i].speed,(int)coordinat[i].x,(int) coordinat[i].y);
//                Ship.shipCollize();//если астеройд столкнулся с кораблём
//               genPos(i);
//                sort(i);
//                
//          }
            else{


                if (coordinat[i].y > 0 && coordinat[i].y < height)
                GE.Grid.add(coordinat[i]);
          }
                
           //g.setColor(0xFF0000);
           //g.drawString(""+i, (int) coordinat[i].x, (int) coordinat[i].y, Graphics.TOP | Graphics.LEFT);
            


        }

            
        
    }

    public static void draw() {

        for (int i = 0; i < coordinat.length; i++) {
            int num = coordinat[i].tactic;
           aster[num].setPosition((int) coordinat[i].x, (int) coordinat[i].y );                
           aster[num].setFrame((int) coordinat[i].frame);
           aster[num].paint(g);
        }

        
    }

 

   private static int random(int i){
        return (GE.random.nextInt() >>> 1) % i;
    }




}

