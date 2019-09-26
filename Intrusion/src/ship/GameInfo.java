/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ship;

import UI.fntFont;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import main.GE;

/**
 *
 * @author Admin
 */
public class GameInfo {

   public static int asterKill;//количество уничтоженных астеройдов
   public static float shipLive;//жизни корабля
   public static int lazerCount;//количество лазеров
   public static int kvantCount;//количество лазеров в магазине

   public static int еnemyLive;//жизни корабля врага



   static Image kvant;
   static Image magazin;
   static Image ochki;
   static Image zashita;


   static int mXpos;//позиция магазина x
   static int mYpos;//позиция магазина y
   static int ochkiXpos;//позиция для рамки очков x
   static int ochkiYpos ;//позиция для рамки очков y
   static int aKXpos;//позиция для очков asterKill x
   static int aKYpos;//позиция для очков asterKill y
   static int zashitaXpos;//позиция для рамки зашиты x
   static int zashitaYpos;//позиция для рамки зашиты y
   static int z_kvantXpos;//позиция для кванта зашиты x
   static int z_kvantYpos;//позиция для кванта зашиты x
   static int kvantPos[][];//позиция квантов в магазине

    public GameInfo() {

        
        try {

        kvant = Image.createImage(GE._8bit+"/kvant.png");
        magazin = Image.createImage(GE._8bit+"/magazin.png");
        ochki = Image.createImage(GE._8bit+"/ochki.png");
        zashita = Image.createImage(GE._8bit+"/zashita.png");

         } catch (Exception ex) {ex.printStackTrace();}

        mYpos = GE.height - magazin.getHeight();
        kvantPos = new int[][]{
                    {mXpos + 41, mYpos + 31},
                    {mXpos + 39, mYpos + 16},
                    {mXpos + 34, mYpos + 5},
                    {mXpos + 21, mYpos + 3},
                    {mXpos + 8, mYpos + 7}
                };

       ochkiXpos = GE.width - 77;
       ochkiYpos = 2;
       aKXpos=GE.width - 6;
       aKYpos = 8;
       zashitaXpos=GE.width - 17;
       zashitaYpos=GE.height - 48;
       z_kvantXpos = GE.width - 11;//позиция для кванта зашиты x
       z_kvantYpos = GE.height -12;

        init();
    }

    public static void init() {
        asterKill = 0;//количество уничтоженных астеройдов
        shipLive = 5;//жизни корабля
        lazerCount = 100;//количество лазеров
        kvantCount = 5;//количество лазеров в магазине
    }

   // static int x = 14;
   // static int y = 23;

     public static void Draw(Graphics g){



//         if (GE.right) {
//             x += 1;
//
//         }
//        else
//
//         if (GE.left) {
//             x -= 1;
//
//         }
//        else
//         if (GE.up) {
//             y -= 1;;
//         }
//         else
//         if (GE.down ) {
//             y += 1;
//         }
//
//         if (GE.zero) {
//             System.out.print(x+" x\n");
//             System.out.print(y+" y\n");
//         }

        
//оптимизировать позициии !!!!!!!!!

         
        for (int i = 0; i < kvantCount; i++) {
             g.drawImage(kvant, kvantPos[i][0], kvantPos[i][1], 20);

         }
       g.drawImage(magazin, mXpos, mYpos, 20);
       GE.font.drawString(g, String.valueOf(lazerCount), mXpos + 23,mYpos + 25,Graphics.VCENTER);



       g.drawImage(ochki, ochkiXpos, ochkiYpos, 20);
       GE.font.drawString(g, String.valueOf(asterKill),aKXpos,aKYpos,Graphics.TOP | Graphics.RIGHT);


       g.drawImage(zashita, zashitaXpos, zashitaYpos, 20);
       for (int i = 0; i < shipLive; i++) {
             g.drawImage(kvant, z_kvantXpos, z_kvantYpos - (7*i), 20);

         }


    }

   public static void lazerFire(){
       if(GameInfo.kvantCount <= 0){

            GameInfo.lazerCount -=5;//
            GameInfo.kvantCount = 5;
            if(GameInfo.lazerCount <=0){//для лазеров отнимаю от количества сбитых астеров
                GameInfo.lazerCount =100;
                GameInfo.asterKill -=50;
            }

        }
       GameInfo.kvantCount--;
        
   } 



}
