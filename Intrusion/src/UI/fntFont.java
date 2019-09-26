package UI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
/**
 *
 * @author sinaz
 */
public class  fntFont
{

    static  Image fontImg = null;
    static  int posX[] = null;
    static  int posY[] = null;
    static  int textWidth[] = null;
    private InputStream is;
    static  int fontHeigth;
    static  int textColor;
    static  int backgroundColor;

    

    public fntFont(String res,int fontColor,int backColor)
    {

        try
        {
            posX = new int[256];
            posY = new int[256];
            textWidth = new int[256];
            is = getClass().getResourceAsStream(res);

            if(ReadWord() != 13313)//сигнатура шрифта
            {
                is.close();
                throw new Exception();
            }
            //int NegativePos =
            ReadInt();
            /*NegativePos Указывает на позицию негативного шрифта,который добавляется в конец файла.
            Это использывается в MIDP-1.0 так-как не поддерживает изменение RGB в картинках.*/

            fontHeigth = (int) ReadByte();

            //int fontType =
            ReadByte();
            /*Указывает возможность негатива,но это только для MIDP-1.0 !!!*/
            
            textColor = ReadInt();
            backgroundColor = ReadInt();
            

            for(int l = 0; l < 32; l++)
            {
                posX[l] = 0;
                posY[l] = 0;
                textWidth[l] = 0;
            }

            short word0 = 0;
            short word1 = 0;
            for(int i1 = 32; i1 < 256; i1++)
            {
                byte byte2 = ReadByte();
                if(word0 + byte2 > 200)
                {
                    word0 = 0;
                    word1 += ((short) fontHeigth);
                }
                posX[i1] = word0;
                posY[i1] = word1;
                textWidth[i1] = byte2;
                word0 += ((short) (byte2));
            }

            int imgsize = ReadInt();
            byte imgbyte[] = new byte[imgsize];
            is.read(imgbyte, 0, imgsize);
            is.close();
            fontImg = Image.createImage(imgbyte, 0, imgsize);


           // устанавливаю цвет фона шрифта
            fontImg = ChangeColor(backColor,backgroundColor,fontImg);
            backgroundColor = backgroundColor - 0xff000000;
            //устанавливаю цвет шрифта
            setColor(fontColor);


        }
        catch(Exception exception)
        {
            //throw new Exception();
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
           // throw new Exception();
        }
        System.gc();
    }


    public  void drawString(Graphics g, String s, int x, int y,int anhor)
    {
        if(anhor == (Graphics.TOP | Graphics.RIGHT) )
            x = x-stringWidth(s);
        if(anhor == (Graphics.VCENTER ) )
            x = x - stringWidth(s)/2;

        for(int k = 0; k < s.length(); k++)
        {

            int ch = s.charAt(k) ;
            ch = ch == 0x401 ? 0xa8 : ch == 0x451 ? 0xb8 : ch;
                    ch = ch > 0x400 ? ch - 0x350 : ch;

            if(textWidth[ch] == 0)
                continue;

            if(x + textWidth[ch] >= 0)
            {
               // g.setClip(x, y, textWidth[ch], fontHeigth);
               // g.drawImage(fontImg, x - posX[ch], y - posY[ch], 20);
                g.drawRegion(fontImg, posX[ch], posY[ch],textWidth[ch], fontHeigth,0, x, y,20 );
            }
            x += textWidth[ch];


        }




    }


    public static void setColor(int RGB) {
        try {
           fontImg = ChangeColor(RGB |= 0xff000000, textColor,fontImg);
            textColor = RGB;
        } catch (Exception ex) {}
        System.gc();
    }


    public static int stringWidth(String s)
    {

        int j = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int ch = s.charAt(i);
            ch = ch == 0x401 ? 0xa8 : ch == 0x451 ? 0xb8 : ch;
                    ch = ch > 0x400 ? ch - 0x350 : ch;

            j += textWidth[ch];

        }

        return j;
    }

    public static int getHeight()
    {
        return fontHeigth;
    }


    /**
     * изменяет цвет thisColor в картинке img на новый цвет newColor
     * если newColor равен 0 то цвет делается прозрачным
     */
    public static Image ChangeColor(int newColor, int thisColor, Image img){


        thisColor |= 0xff000000;

        int width = img.getWidth();
        int height = img.getHeight();
        int lenght = width * height;

        try {
            int pixelARGB[] = new int[lenght];
            img.getRGB(pixelARGB, 0, width, 0, 0, width, height);
            for (int pos = 0; pos < lenght; pos++) {
                if (pixelARGB[pos] == thisColor) {
                    pixelARGB[pos] = newColor;
                }

            }

            img = Image.createRGBImage(pixelARGB, width, height, true);

        } catch (OutOfMemoryError outofmemoryerror) {
            //throw new Exception();
        }

        return img;
    }

private  int ReadWord() throws IOException
    {
      return (is.read() & 0xff) | (is.read() & 0xff) << 8;
    }

    private  int ReadInt() throws IOException
    {

      return ((is.read() & 0xff) | (is.read() & 0xff) << 8)
            | ((is.read() & 0xff) | (is.read() & 0xff) << 8) << 16;
    }

    private  byte ReadByte() throws IOException
    {
      return (byte) is.read();
    }







}
