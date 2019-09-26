/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UI;

import javax.microedition.lcdui.Graphics;
import main.GE;

/**
 *
 * @author dzanis
 * 24.01.2011
 */

public final class TextView
{




    static Graphics g = GE.g;
    private static int windowsPosX;
    private static int windowsPosY;
    private static int windowsWidth;
    private static int windowsHeight;
    private static int stringHeight;

     /**массив текста*/
    static private String[] TextList;
    /**текуший путь в файловой*/
    static private String Title = "";//заглавие
    
    
    /**количество символов по высоте*/
    static int scrHeight;

    static int scrStart;
    static int scrSel;
    static int p;
    static int i;
    static boolean cursor = false;//если поставить true - то будет виден курсор.
    //Пригодится если нужно будет выбрать какую нибуть опцию


    public TextView()
    {
    }


    /**
     Создание окна
     * X и Y позиции окна
     * width и height ширина и высота окна
     * text текст который мы будем показывать в окне
     * title заглавие нашего окна
     * anchor положение текста в окне
     */
    public static void windowsInit(
            int X,
            int Y,
            int width,
            int height,
            String text,
            String title,
            int anchor) {

        windowsPosX = X;
        windowsPosY = Y;
        windowsWidth = width;
        windowsHeight = height;
        stringHeight = GE.font.getHeight();
        TextList = getLineArray(text, windowsWidth);
        Title = title;


        //вычисляем сколько влезет символов по высоте дисплея (делим высоту дисплея на высоту символа)
        scrHeight = (windowsHeight / stringHeight);
        scrStart = scrSel = 0;

        

    }


    public static void draw(){
    
     filesList( TextList, Title);
     
    }


	int scroll;// длЯ плавной прокрутки
    final int SCROLL_SPEED = 1;// скорость прокруки

   /**
 * Метот прорисовывает текстовый массив строк с прокруткой
 * возврашает выбранную строку ( если cursor = true)
 */
   private static String filesList( String[] text, String title) {

        p = 1;
        
        if(text.length <= 0)
            return "";

        //заглавие
        g.setColor(0x989898);
        g.fillRect(windowsPosX , windowsPosY, windowsWidth, stringHeight);
        GE.font.drawString(g, title,windowsPosX + windowsWidth / 2,windowsPosY + 0, Graphics.VCENTER);
        // рамка
        g.drawRect(windowsPosX , windowsPosY, windowsWidth, windowsHeight);
        


          if (GE.up) {//курсор вверх
                if (cursor) {
                    scrSel--;
                    if (scrSel < 0) //scrSel = files.length - 1;
                    {
                        scrSel = 0;
                    }
                } else {
                	scroll += SCROLL_SPEED;
                	if(scroll >= stringHeight || scrStart == 0){
                		scroll = 0;
                    scrStart--;
                	}
                }
            
                
        } else if (GE.down ) {//курсор вниз
                if (cursor) {
                    scrSel++;
                    if (scrSel >= text.length) //scrSel = 0;
                    {
                        scrSel = text.length;
                    }
                } else {
                	scroll -= SCROLL_SPEED;
                	if(scroll <= -stringHeight || (scrStart + scrHeight >= text.length)){
                		scroll = 0;
                    scrStart++;
                	}
                }                

        }
        

        



        if (scrSel >= text.length) {
            scrSel = text.length - 1;
        }
        if(cursor)
        scrStart = scrSel - scrHeight / 2;
        if (scrStart + scrHeight >= text.length) {
            scrStart = text.length - scrHeight;
        }
        if (scrStart < 0) {
            scrStart = 0;
        }

        if (GE.fire) //выбор
            return text[scrSel];


        

        for (i = scrStart    , p = 1; i < text.length && i < scrStart + scrHeight; i++, p++) {

            
            if (cursor && scrSel == i)//Прорисовка курсора
            {
                g.setColor(0x989898);
                g.fillRect(windowsPosX,windowsPosY + p * stringHeight, windowsWidth, stringHeight);
                
            }

            //Прорисовка текстового массива
            GE.font.drawString(g, text[i],windowsPosX + windowsWidth / 2, windowsPosY + p * stringHeight+ scroll, Graphics.VCENTER);
        }


        

        skroll_bar(scrStart,text.length,scrHeight);

        return "";

    }

 /**
 * просто skroll_bar с правой стороны окна
 */
  private static  void skroll_bar(int scrStart,int fileslength,int scrHeight) {

         g.setColor(0xff0000);
        int sbstart = windowsHeight * scrStart / fileslength, sbsize = windowsHeight * scrHeight / fileslength;
        if (sbsize > windowsHeight) {
            sbsize = windowsHeight;
        }
        g.fillRect(windowsPosX + windowsWidth - 2,windowsPosY + sbstart, 2, sbsize <= 2 ? 2 : sbsize);
        //System.out.println("sbstart "+(sbstart)+"sbsize "+(sbsize)+"\n");
    }

/**
 * Этот метод (сообша с getLine(str,w)) форматирует текстовую строку в зависимости от ширины окна
 * и возврашеет её в виде массива строк
 * ширина каждой строки в массиве не превышает ширину окна
 */
  private  static String[] getLineArray(String text, int w)
    {
        int lineArraySize = 0;
        int charIndex = 0;
        String tempString = "";
        for(text = text + "\n"; charIndex < text.length(); charIndex += tempString.length())
        {
            tempString = getLine(text.substring(charIndex, text.length()), w);
            lineArraySize++;
        }

        String lineArray[] = new String[lineArraySize];
        charIndex = 0;
        for(int lineIndex = 0; lineIndex < lineArraySize; lineIndex++)
        {
            lineArray[lineIndex] = getLine(text.substring(charIndex, text.length()), w);
            charIndex += lineArray[lineIndex].length();
            lineArray[lineIndex].trim();
        }

        return lineArray;
    }


    private static String getLine(String str, int w)
    {
        int last_space = 0;
        int c_start = 0;
        int c_pixel_width = 0;
 
        for(int i = 0; i < str.length(); i++)
        {
            if(str.charAt(i) == ' ' || str.charAt(i) == '\n' || str.charAt(i) == '\r')
                last_space = i;
            if((c_pixel_width += GE.font.stringWidth(str.substring( i, i + 1))) >= w)
            {
          
                if(c_start < last_space)
                    return str.substring(c_start, last_space + 1);
                else
                    return str.substring(c_start, i + 1);
            }
            if(str.charAt(i) == '\n' || str.charAt(i) == '\r')
                if(str.charAt(i) == '\n')
                    return str.substring(c_start, i + 1).replace('\n', ' ');
                else
                    return str.substring(c_start, i + 1).replace('\r', ' ');
        }

        if(c_start < str.length() - 1)
        {
            return str.substring(c_start, str.length());
        } else
        {
            return "";
        }
    }

 
}

