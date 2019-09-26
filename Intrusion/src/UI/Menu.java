package UI;


import cosmos.Stars;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import main.Intrusion;
import main.GE;


/**
 *
 * @author dzanis
 */
public class Menu {
    
 static Graphics g = GE.g;
 static int width = GE.width;// Ширина
 static int height = GE.height;// и высота дисплея

static Image fon,cursor,big_ship,highlight;
static int shipPosX;//позиции для большого корабля
static int shipPosY;
static int cursorPOS;//позиция курсора
static  int TEXT_START ;//начальная позиция  строк
static  int CURSOR_START ;//начальная позиция курсора
static int posY ;//позиция курсора
static int hposX ;//позиция блика
private static long begin;//для засекания времени появления блика
static int moveSPEED;//скорость передвижения курсора
static int skip;//
static String textMenu[] = {"НОВАЯ ИГРА","ПОМОШЬ","АВТОРЫ","Тест (враг)","ВЫХОД"};
static final int DEFAULT = -1;
static final int NEW_GAME = 0;
//static final int CONTINUE = 1;
static final int HELP = 1;
static final int ABOUT = 2;
static final int TEST_ENEMY = 3;
static final int EXIT = 4;
static final int  ramkaH = 11;//толшина рамки
static int PROCESS = DEFAULT;


    




   public Menu() {
     
        try {

        fon = Image.createImage(GE._8bit+"/menu/border2.png");
        cursor = Image.createImage(GE._8bit+"/menu/cursor2.png");
        big_ship = Image.createImage(GE._8bit+"/menu/f1big.png");

        highlight = Image.createImage(GE._8bit+"/menu/highlight2.png");
        
         } catch (Exception ex) {}
        
        
       
       fonInit();

       new TextView();
       String text = "ВТОРЖЕНИЕ\n v 1.5 \n Игра разрабатываемая интузиастами команды GAMASTIC."
               + "На данный момент ешё незакончен сценарий игры.Потому в игре ешё нет ничего кроме геймплея в астеройдном поле."
               + "Игру разрабатывают новички в геймдейве,потому не судите строго."
               + "Свои пожелания вы можете оставить на нашем сайте gamastic.ru"
               + "\n\n\nСЦЕНАРИСТ\nRooMen\n\n\nПРОГРАМИСТЫ\ndzanis\nDimon638\n\n\nХУДОЖНИКИ\nКирпи4\nKillerbl\n Холлин\nlimil\n\n\nМУЗЫКА\nишем вакансии";

       TextView.windowsInit(ramkaH,ramkaH,width - ramkaH*2,height - ramkaH * 2,text,textMenu[ABOUT],Graphics.VCENTER);

    }




    public static void draw() {

        switch(PROCESS){
            
            
        case DEFAULT:
        drawMenu();
        break;

        case HELP:

         break;
        case ABOUT:       
          Stars.draw(g);
         TextView.draw();
          GE.font.drawString(g, "назад *", width / 2, height - GE.font.getHeight(), Graphics.VCENTER);
         if(GE.star)
            PROCESS = DEFAULT;
        break;
  
        }

    }


     private static void drawMenu() {
        if (GE.up) {
            //двигаем вверх
            cursorPOS -= 1; //уменьшаем позицию курсора
            moveSPEED = -2; //присваеваем скорости отрицательное значение
            GE.up = false; //сброс флага,чтоб не бегало непрерывно
        }
        if (GE.down) {
            //двигаем вниз
            cursorPOS += 1;
            moveSPEED = 2;
            GE.down = false;
        }
        if (cursorPOS < 0) {
            //если курсор меньше нуля,то быстро перемешаем его вниз
            cursorPOS = textMenu.length - 1;
            moveSPEED = 10;
        }
        if (cursorPOS > textMenu.length - 1) {
            //быстро вверх
            cursorPOS = 0;
            moveSPEED = -10;
        }
        posY += moveSPEED;
        int texPos = CURSOR_START + (skip * cursorPOS); //расчёт позиции текста
        if (moveSPEED > 0 && posY >= texPos || moveSPEED < 0 && posY <= texPos) {
            posY = texPos;
        }
        if (GE.fire && posY == texPos) {
            //выбор (только когда курсор неподвижен)
            GE.fire = false;
            switch (cursorPOS) {
                case NEW_GAME:
                    GE.PROCESS = GE.GAME_PROCESS;
                    break;
                case ABOUT:
                    PROCESS = ABOUT;
                    break;
                case TEST_ENEMY:
                    GE.PROCESS = GE.ENEMY;
                    break;    
                case EXIT:
                    Intrusion.runnable = false;
                    break;
            }
        }
        Stars.draw(g);
        g.drawImage(fon, 0, 0, 20);
        if (height >= 220) {
            g.drawImage(big_ship, shipPosX, shipPosY, 20);
        }
        
        
 
        
        cursorPaint();
        
    }






 /**
 * мультиэкранная прорисовка курсора,максимальный экран 240x320
 */
private static void cursorPaint() {
        
        int imgW = cursor.getWidth();
        int imgH = cursor.getHeight();
        // расчитываем сколько резать
        int cut = width/2 - imgW/2 ;
        // скользяший блик
        if (System.currentTimeMillis() - begin > 4000) //появление блика через каждые 4 секунды
        highlight(cut); 
       // рамка курсора 
       g.drawImage(cursor, cut, posY, 20);
       //левый ползунок
       g.drawRegion(cursor, 0,0,12, imgH, Sprite.TRANS_NONE, 0, posY, 20);
       //правый ползунок
       g.drawRegion(cursor, imgW-12,0,12, imgH, Sprite.TRANS_NONE, width-12, posY, 20);
       
       
       

}

/**
 * скользяший блик в рамке
 */
private static void highlight(int cut) {
       
        
        g.drawImage(highlight, (cut + 55)+ (hposX +=3), posY + 7, 20);
        if(hposX >= 104){
            hposX = 0;
            begin = System.currentTimeMillis();
        }
    }

 /**
 * мультиэкранная прорисовка бордюра,максимальный экран 240x320
 */
public static void borderPaint(Graphics g) {
        int pusW = width / 2;
        int pusH = height / 2;
        int imgW = fon.getWidth();
        int imgH = fon.getHeight();
        //левый верхний бордюр
        g.drawRegion(fon, 0, 0, pusW, pusH, Sprite.TRANS_NONE, 0, 0, 20);
        //правый верхний бордюр
        g.drawRegion(fon, imgW - pusW, 0, imgW - (imgW - pusW), pusH, Sprite.TRANS_NONE, pusW, 0, 20);
        //левый нижний бордюр
        g.drawRegion(fon, 0, imgH - pusH, pusW, imgH - (imgH - pusH), Sprite.TRANS_NONE, 0, pusH, 20);
        //правый нижний бордюр
        g.drawRegion(fon, imgW - pusW, imgH - pusH, imgW - (imgW - pusW), imgH - (imgH - pusH), Sprite.TRANS_NONE, pusW, pusH, 20);
    }

/**
 * создаём фон,на котором бордюр,кораблик,и текст
 */
private   void fonInit()
    {

        Image image = Image.createImage(width, height);
        Graphics g = image.getGraphics();
        
        
        int center = cursor.getHeight()/2-GE.font.getHeight()/2;//вычесляем чтоб позиция текста была по центру курсора
        skip = GE.font.getHeight() + center;//пропуск пикселей
        int textcolumn = textMenu.length * (GE.font.getHeight()+ center);//высота колонки текста
       TEXT_START = height/2 - textcolumn/2;//вычесляем чтоб колонка текста была по центру дисплея
              

       g.setColor(0x007dff);//фон
       g.fillRect(0, 0, width, height);
       

       if(height >= 220)//рисуем корабль если дисплей больше 220 пикселей
         {
             int imgH = big_ship.getHeight();
             
             if(ramkaH + imgH >= TEXT_START - center){//если корабль рисуется на тексте то пересчитываем координаты
                TEXT_START  = ((height+(ramkaH + imgH))/2 - textcolumn/2);
            }            
             //g.drawImage(big_ship, width/2-big_ship.getWidth()/2, (ramkaH + TEXT_START -center)/2-imgH/2, 20);
             shipPosX = width/2-big_ship.getWidth()/2;
             shipPosY = (ramkaH + TEXT_START -center)/2-imgH/2;
        } 
        
        posY =CURSOR_START = TEXT_START-center;//основные значения курсора

        borderPaint(g);//рисую бордюр

        

        for (int i = 0; i < textMenu.length; i++) {// прорисовка текста
            GE.font.drawString(g, textMenu[i], width/2,
                    TEXT_START +(skip*i) ,
                    Graphics.VCENTER);
        }


        //делаю фон прозрачным,цвет 0x007dff заменяется на прозрачность
        fon = GE.font.ChangeColor(0, 0x007dff, image);
        image = null;
        g = null;
        System.gc();

    }



}
