package cosmos;


import main.GE;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;



/**
 *
 * @author dzanis
 */
public class Stars {


    static int width = GE.width;// Ширина
    static int height = GE.height;// и высота дисплея
    static final int X = 0,  Y = 1;
    final static int MAX_STARS = 100;// Кол-во звёздочек
    static float Stars[][] = new float [2][MAX_STARS];//координаты звёзд X,Y
    static int view[] = new int [MAX_STARS];//вид звёзд
    static float speed [] = {0.25F,0.5F,2F};

    static Image[] stars = new Image[3];


    public Stars() {


       try {

         Image img = Image.createImage("/stars.png");
           for (int i = 0; i < stars.length; i++) 
               stars[i] = Image.createImage(img, i * 5, 0, 5,5, Sprite.TRANS_NONE);
          

        } catch (Exception e) {}

        // случайным образом раскидаем звёздочки
        for (int i = 0; i < MAX_STARS; i++) {
            genPos(i);


        }

    }

    

    //генерация позиции звёздочки  под номером i
    private static void genPos(int i) {
        Stars[X][i] = random(width);
        Stars[Y][i] = random(height * 2) - height;
        view[i] = random(3); //генерация одного из трёх видов
    }


    
// Главный цикл логики
    public static void update(float delta) {
   
    for (int i = 0; i < MAX_STARS; i++) {
        
        //           switch(GE.PROCESS){
//               case GE.MENU:
//                //если мы в меню то звёздочки летят наискось,снизу вверх
//               Stars[Y][i] -= speed[view[i]];// Изменяем позицию Y (скорость зависит от вида)  
//               Stars[X][i] += speed[view[i]];
//                   break;
//               default:
//                Stars[Y][i] += speed[view[i]];   
//           }
      
           if (GE.PROCESS == GE.MENU){
               //если мы в меню то звёздочки летят наискось,снизу вверх
               
               Stars[Y][i] -= speed[view[i]] * delta;// Изменяем позицию Y (скорость зависит от вида)
                       
               Stars[X][i] += speed[view[i]] * delta;
           }
           else
           {
               Stars[Y][i] += speed[view[i]] * delta;
           }
               // Если звезда "улетела" за пределы дисплея
               if (Stars[Y][i] >= height || Stars[Y][i] <= 0 || Stars[X][i] >= width) {
                   genPos(i); //генерация новой позиции
               }

       }
    }

    // Главный цикл отрисовки
    public static void draw(Graphics g) {

       for (int i = 0; i < MAX_STARS; i++) { 
     // Рисуем звёздочку в новом положении (картинка зависит от вида)
        g.drawImage(stars[view[i]], (int)Stars[X][i], (int)Stars[Y][i], 20);
       }


    }

    private static int random(int i){
        return (GE.random.nextInt() >>> 1) % i;
    }




}
