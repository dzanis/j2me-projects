/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;



import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author dzanis
 * 27.01.2011
 */

public class Intro {

 static Graphics g = GE.g;
 static int width = GE.width;// Ширина
 static int height = GE.height;// и высота дисплея

static Image golova;
static Image text;
static Image mask;


static int textYpos;
static int textXpos;
static int golovaYpos;
static int golovaXpos;
static int[] pixelARGB;
static int  pixelAlfa = 0xFF000000;
static int  slideY;
static int  sceneNum;
static final int scenaN1 = 0;
static final int scenaN2 = 1;
static final int scenaN3 = 2;
static final int scenaN4 = 3;

/**
 * интро заставка GAMASTIK'a
 */
public Intro() {


        try {

            golova = Image.createImage(Image.createImage("/logo.png"), 25, 0, 74,82, Sprite.TRANS_NONE);
            text = Image.createImage(Image.createImage("/logo.png"), 0, 82, 128 , 18, Sprite.TRANS_NONE);



        } catch (Exception ex) {}

        pixelARGB = new int[74 * 82];//создаю массив по размеру головы
        for (int pos = 0; pos < pixelARGB.length; pos++) {//заполняю его чёрным непрозрачным цветом
                    pixelARGB[pos] = 0xff000000;
            }

        mask = Image.createRGBImage(pixelARGB, 74,82, true);//содаю из массива чёрную картинку без прозрачности

        //оптимизированные позиции для головы и текста
        golovaXpos = (width / 2)- (74 / 2);
        golovaYpos = (height / 2)- (82 / 2);

       textXpos = (width / 2)- (128 / 2);
       textYpos = (height / 2)- (18 / 2);

}



     public static void draw(){

     g.setColor(0);
     g.fillRect(0, 0, width, height);

         logoPaint(g);

          switch (sceneNum) {

            case scenaN1://в первой сцене,голова двигается вверх и постепенно появляется,а текст двигается вниз
                mask = ChangeAlfa(mask, pixelAlfa);
             pixelAlfa -= 0x08000000;//постепенно делаю маску прозрачной
             slideY++;
             if(slideY == 32)
                 sceneNum++;
                break;
            case scenaN2://картинки стоят на месте,появляется надпись "жми огонь"
                 GE.font.drawString(g, "жми огонь", width / 2, height - GE.font.getHeight(), Graphics.VCENTER);
              if (GE.fire) {//если нажали огонь,то переход в следуюшую сцену
                  sceneNum++;
         }
             pixelAlfa = 0;//дальше я буду ипользовать его как ускорение для slideY
                break;
            case scenaN3://приближаю голову и текст
                slideY-=pixelAlfa++;
             if(slideY < 16)
                 sceneNum++;
                break;
            case scenaN4://голова и текст разлетается в разные стороны
                slideY+=pixelAlfa++;
             if(slideY > 200)//тут заставка заканчивается,перехожу в меню
                 GE.PROCESS = GE.MENU;
                break;
         }



    }


 /**
 * просто прорисовка головы с маской затемнения и текста GAMASTIK
 */
private static void logoPaint(Graphics g) {
        
   
        //голова
        g.drawImage(golova, golovaXpos, golovaYpos - slideY, 20);
        //маска затемнения
        g.drawImage(mask, golovaXpos, golovaYpos - slideY, 20);
        //текст
        g.drawImage(text, textXpos, textYpos + slideY, 20);
     }


/**
     * изменяет прозрачность в картинке img,и возврашает её
     * если pixelAlfa = 0x00000000 то  делается прозрачной
     * если pixelAlfa = 0xFF000000 то  делается не прозрачной

     */
    private static Image ChangeAlfa(Image img, int pixelAlfa) {


        int width = img.getWidth();
        int height = img.getHeight();
        img.getRGB(pixelARGB, 0, width, 0, 0, width, height);
        for (int pos = 0; pos < pixelARGB.length; pos++) {
            pixelARGB[pos] = pixelAlfa ;
        }
        img = Image.createRGBImage(pixelARGB, width, height, true);
        return img;
    }

}
