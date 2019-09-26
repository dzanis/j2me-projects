import java.util.Random;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

/**
 * Tetris 1.2
 * @author dzanis
 * 5.09.2009
 */
public class Tetris extends MIDlet {


public void startApp() {
	Display.getDisplay(this).setCurrent(new TetrisGame());
}

public void pauseApp() {
}

public void destroyApp(boolean unconditional) {
}
}

class TetrisGame extends GameCanvas implements Runnable {
    
private Graphics graphics;
private int width;
private int height;
private boolean game;
private int box; //размер квадрата
private int[] matrix; //это матрица стакана 10x20,
private int figurePosX;//позиция фигуры в стакане
private int figurePosY;//позиция фигуры в стакане
private int figureNum;//номер фигуры
private int figureNextNum = 1;//номер следуюшей фигуры
private int figureRotateNumber;//номер положения фигуры

private int[] data; //массив с данными [очки],[счётчик линий],[уровень игры]
private final int SCORE = 0;//очки
private final int LINES = 1;//счётчик линий
private final int LEVEL = 2;//уровень игры   
private final int AVTOR = 3;
private final int GAME_OVER = 4;
private final int PRESS_FIRE_TO_START = 5;
private final String[] text= {"SCORE","LINES","LEVEL","© dzanis 5.09.2009","**** GAME OVER ****   ","PRESS FIRE TO START",};
private int[] skoreForLineCount= {0,40,100,300,1200};//очки за количество удалённых колонок,за одну 40,за две 100 и т.д
int heightFont = Font.getDefaultFont().getHeight();
    
//координаты квадратиков фигур по осям (x,y)
int[][][] figure= {
	{},
	{{0,0,1,0,0,1,1,1}},//фигура квадрат
	{{0,1,1,1,2,1,1,2},{1,0,0,1,1,1,1,2},{1,0,0,1,1,1,2,1},{1,0,1,1,2,1,1,2}},//фигура по форме буквы "Т"
	{{1,0,1,1,1,2,2,2},{0,1,1,1,2,1,0,2},{0,0,1,0,1,1,1,2},{2,0,0,1,1,1,2,1}},//фигура по форме буквы "L"
	{{1,0,1,1,1,2,0,2},{0,0,0,1,1,1,2,1},{1,0,2,0,1,1,1,2},{0,1,1,1,2,1,2,2}},//фигура по форме буквы "L" зеркально
	{{0,1,1,1,1,2,2,2},{1,0,0,1,1,1,0,2}},//фигура по форме буквы "Z"
	{{1,1,2,1,0,2,1,2},{0,0,0,1,1,1,1,2}},//фигура по форме буквы "Z" зеркально
	{{1,0,1,1,1,2,1,3},{0,2,1,2,2,2,3,2}}//фигура по форме буквы "I"
};
final int[] color = {//цвета фигур
	0,
	0xFF0000,//фигура квадрат
	0xFF00FF,//фигура по форме буквы "Т"
	0x004F00,//фигура по форме буквы "L"
	0x5D0078,//фигура по форме буквы "L" зеркально
	0x9B7400,//фигура по форме буквы "Z"
	0x707F85,//фигура по форме буквы "Z" зеркально
	0x0024B8 //фигура по форме буквы "I"
};
// конструктор класса   
public TetrisGame() {
	super(true);
	setFullScreenMode(true);
	graphics = getGraphics();
	width = getWidth();
	height = getHeight();
	box = (height-heightFont)/18;//расчёт размера квадрата по высоте
	dialog = text[PRESS_FIRE_TO_START];
	dialogWidth = Font.getDefaultFont().stringWidth(dialog);
	newGame();
	new Thread(this).start();
}
            
// Игровой цикл.
public void run() 
{        
	while (true) 
	{           
		if(game){
			move();
		} else {
			dialog();
		}
		try {
			Thread.sleep(1);
			
		} catch (InterruptedException e) {
		}
		flushGraphics();
	}        
}
    
String dialog;
int dialogWidth;
int dialogNum;
    
void dialog(){	
	graphics.setColor(0);
	graphics.fillRect(0,height - heightFont, width, heightFont);
	graphics.setColor(0xFFFFFF);
	graphics.drawString(dialog, width - dialogNum, height - heightFont, 20);
	dialogNum ++;
	if((width - dialogNum)+ dialogWidth < 0)
		dialogNum = 0;
	int state = getKeyStates();
	
	if ((state & FIRE_PRESSED) != 0) {
		newGame();
		game = true;
		dialog = text[GAME_OVER]+text[PRESS_FIRE_TO_START];
		dialogWidth = Font.getDefaultFont().stringWidth(dialog);
		dialogNum = 0;
	}	
}
        
long timeGame,timeKeyPress;
boolean pressOne = true;

void move() {        
	long t = System.currentTimeMillis();       
	//временно сохраняю текущую позицию фигуры
	int previosfigureRotateNumber = figureRotateNumber;
	int previosfigurePosX = figurePosX;
	int previosfigurePosY = figurePosY;        
	int state = getKeyStates();        
	//пробуем двигать-крутить фигуру
	if(pressOne || t-timeKeyPress > 400L){
		//pressOne позволяет реагировать на быстрое нажатие клавиши
		// на долгое нажатие >400 ms срабатывает быстрое передвижение фигуры           
		if ((state & FIRE_PRESSED) != 0) {//поворот фигуры
			figureRotateNumber = figureRotateNumber < figure[figureNum].length - 1 ? figureRotateNumber + 1 : 0;
			//figureRotateNumber = figureRotateNumber < figPosCorLenght[figureNum] - 1 ? figureRotateNumber + 1 : 0;
		} else
			if ((state & LEFT_PRESSED) != 0){//движение фигуры влево
			figurePosX -=1;
			} else
				if ((state & RIGHT_PRESSED) != 0) {//движение фигуры вправо
			figurePosX +=1;
				} else
					if ((state & DOWN_PRESSED) != 0) {//движение фигуры вниз
			figurePosY+=10;
					}	
	}        
	if (state != 0 ){
		pressOne = false;
	} else {
		timeKeyPress = t;
		pressOne = true;
		
	}       
	if(!canMove()){//если двигать крутить нельзя то возврашяю прежнию позицию фигуры
		figureRotateNumber = previosfigureRotateNumber ;
		figurePosX = previosfigurePosX;
		figurePosY = previosfigurePosY;
	}       
	//здесь задаётся скорость падения фигур в зависимости от уровня
	long speed = (1000L - (data[LEVEL]*100));
	if(t - timeGame > (speed < 200L ? 200L :speed) ){//не допускаю скорость быстрее 200 ms
		figurePosY+=10;
		timeGame = t;
	}
	insertFigureToMatrix();   
}
   
//даём добро на передвижение-поворот фигуры если:
//1.фигура не выходит за пределы стакана
//2.фигуре не мешают кубики
//так же используется для проверки на упор фигуры в дно стакана или в замороженные кубики
private boolean canMove() {	
	for (int i = 0; i < 8; i += 2) {
		int x = figurePosX + figure[figureNum][figureRotateNumber][i];
		int y = figurePosY + (figure[figureNum][figureRotateNumber][i+1] * 10);
		if(x < 0 | x > 9 | x+y > 199 | x+y < 0){//не выходит ли фигура за пределы стакана
			return false;
		}
		if(matrix[x+y] >= 1)//не мешают ли фигуре кубики
			return false;
	}
	return true;	
}
    
//записываю фигуру в массив поля
//(>=1)- замороженная фигура(достигшая дна), (<=-1) - падающая фигура
// цвету кубика соответствуют  color[figureNum]
void insertFigureToMatrix() 
{       
	int kube = -1;       
	if(!canMove()){//если фигура уперается то замораживаю её
		figurePosY-=10;//позицию возврашяю для исключения выхода за пределы массива или наложения фигуры на замороженные кубики
		kube = 1;
	}
	//записываю фигуру в массив поля
	for (int i = 0; i < 8; i += 2) 
	{
		matrix[ figurePosX + figure[figureNum][figureRotateNumber][i]
				+ figurePosY + (figure[figureNum][figureRotateNumber][i+1] * 10)] = kube * figureNum;		
	}		   
	if(kube == 1)//если фигура зоморожена создаю новую
	{
		newFigure();
	}       
	RepaintStakan();       
}
   
//прорисовка стакана с кубиками
void RepaintStakan() {
	int posMatrix = 20;//начинаю прорисовывать с 20-го байта(эфект вылезания фигур из потолка)        
	graphics.setColor(0xffffff);
	graphics.fillRect(1, 1, box * 10 + 2, box * 18 + 2);//очистка стакана       
	int linesCount = 0;
	for (int posY = 1; posY < box * 18; posY += box) {
		
		int count = 0;//счётчик замороженных квадратиков
		
		for (int posX = 1; posX < box * 10; posX += box) {
			graphics.setColor(0xc0c0c0);//сетка на стакане
			graphics.drawRect(posX, posY, box, box);
			if (matrix[posMatrix] != 0) {//если не пусто то рисуем кубики
				graphics.setColor( color[ Math.abs(matrix[posMatrix]) ] );// цвет фигуры
				box(posX, posY);//рисуем кубик
				if(matrix[posMatrix] >= 1)//если квадратик заморожен то увеличиваем счётчик
					count++;
				else//удаление падаюшей фигуры из массива
					matrix[posMatrix] = 0;
			}               
			if(posY == 1 && count > 0){//если есть замороженый квадратик в самой верхней линии то игра с начала
				game = false;                   
			}               
			if(count == 10){//если линия заполнена
				linesCount++;//увеличиваем счётчик линий
				System.arraycopy(matrix, 0, matrix, 10, posMatrix-10);//удаление линии из стакана
			}
			posMatrix++;
		}
	}        
	data[LINES] += linesCount;
	data[LEVEL] = data[LINES] / 10;//уровень игры зависит от количества стёртых линий
	//присуждаем очки за количество удалённых линий,и уровня
	data[SCORE] +=  (skoreForLineCount[linesCount] * data[LEVEL]) + skoreForLineCount[linesCount];        
}
   
private void newFigure() {
	figureNum = figureNextNum;
	figureNextNum = Math.abs(new Random().nextInt() % 7)+1;//случайный выбор фигуры
	figureRotateNumber = 0;
	figurePosY = 0;
	figurePosX=4;
	//очистка дисплея
	graphics.setColor(0);
	graphics.fillRect(0, 0, width, height);
	//прорисовка инфомации очков,линий и уровня
	int x1 = box * 10 + 5;
	int y1 = 0;
	for (int i = 0; i < 3; i++) {
		graphics.setColor(0xFF0000 >> i * 5);
		graphics.drawString(text[i], x1, y1, 20);
		graphics.drawString(Integer.toString(data[i]), x1, y1 + heightFont, 20);
		y1 += heightFont * 2;
	}
	graphics.drawString(text[3], 0, height - heightFont, 20);
	//прорисовка следуюшей фигуры
	y1 += heightFont;
	graphics.setColor(color[figureNextNum]);
	for (int i = 0; i < 8; i += 2) {
		int x = figurePosX + (figure[figureNextNum][0][i] * box);
		int y = figurePosY + (figure[figureNextNum][0][i + 1] * box);
		box(x1 + x, y1 + y);
	}
}

private void box(int posX, int posY) {
	graphics.drawRect(posX + 1, posY + 1, box - 2, box - 2);
	graphics.fillRect(posX + 3, posY + 3, box - 4, box - 4);
}
private void newGame() {
	matrix = new int[200]; //очистка массива стакана
	data = new int[3]; //очистка данных
	newFigure();	
}
   
}




