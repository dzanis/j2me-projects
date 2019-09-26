/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import cosmos.Asteroids;
import cosmos.Enemy;
import cosmos.Enemy_group;
import cosmos.Explosion;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import ship.GameInfo;


/**
 *
 * @author dzanis
 * Сетка столкновений
 * сделана по описанию с сайта
 * http://coolisee.com/2010/04/27/opredelenie-stolknovenij-mnozhestva-obektov/
 * http://coolisee.com/2010/04/27/testirovanie-i-tyuning-setki/
 */
public class CollisionGrid {

 //ID обьекта (предмета или персонажа),для определения   
public final static float LAZER = 0,SHIP = 1,ASTER = 2,RACKET = 3,ENEMY = 4,ENEMY_LAZER = 5; 
private static int R[] = {1,21,16,1,13,1};// их половинный радиус(если обьект размером 42x42 то половина радиуса 21)


public Vector _checks;
private  Vector _grid[];
private int _gridSize;
private int _height;
private int _numCells;
private int _numCols;
private int _numRows;
private int _width;


/*
 * Конструктор сетки столкновения
 * width ширина дисплея
 * height высота дисплея
 * gridSize размер ячейки должен быть не менее большего из объектов
 * (например самый большой обьект 50x50,значит и gridSize должен быть 50)
*/
public  CollisionGrid(int width, int height, int gridSize)
		{
			_width = width + gridSize;
			_height = height + gridSize;
			_gridSize = gridSize;
			_numCols = (int) Math.ceil(_width / _gridSize);
			_numRows = (int) Math.ceil(_height / _gridSize);
			_numCells = _numCols * _numRows;
                        //System.out.print(_numCells+" _numCells\n");
                        _grid = new Vector[_numCells];
                        _checks = new Vector();
// System.out.print("CollisionGrid 1\n");
		}


/*
 * отрисовка сетки для наглядности
 */
public void drawGrid(Graphics g)
		{

                            g.setColor(0xFF0000);
			for(int i= 0; i <= _width; i += _gridSize)
			{
				g.drawLine(i, 0,i, _height);

			}
			for(int i = 0; i <= _height; i += _gridSize)
			{


                                g.drawLine(0, i,_width, i);
			}
		}






 /**
 * @author dzanis
 * добавляю в сетку обьект с координатами Coordinat - который хранит 
 * в себе коортинаты обьекта (персонажа,врага и т.д.  )
 * добавлять желательно перед или после отрисовки обьекта
 * 
 */
    public void add(Coordinat cor) {

 
            //System.out.print(y+" y\n");
            //System.out.print(x+" x\n");

            int index = (int) (Math.floor(cor.y / _gridSize) * _numCols + Math.floor(cor.x / _gridSize));
            //System.out.print(index+" \n");
            if (_grid[index] == null) {
                _grid[index] = new Vector();
            }
            _grid[index].addElement(cor);



        //checkGrid();
    }



 
  

    private  void checkGrid()
	        {
	            // с помощью цикла обходим каждую ячейку сетки
	            for(int i = 0; i < _numCols; i++)
	            {
	                for(int j = 0; j < _numRows; j++)
	                {
	                    // проверяем каждый объект в текущей ячейке друг с другом
	                    checkOneCell(i, j);

	                    // и с каждым объектом в других ячейках
	                    checkTwoCells(i, j, i + 1, j);     // проверка с объектами ячейки справа
	                    checkTwoCells(i, j, i - 1, j + 1); // проверка с объектами ячейки снизу слева
	                    checkTwoCells(i, j, i, j + 1);     // проверка с объектами ячейки снизу
	                    checkTwoCells(i, j, i + 1, j + 1); // проверка с объектами ячейки снизу справа
	                }
	            }
        
                    
	        }
    

   
    
    
    

     private  void checkOneCell(int x, int y) {
        // проверяем каждый объект в текущей ячейке друг с другом
        Vector cell = _grid[y * _numCols + x];
        if(cell == null)
            return;

	            int cellLength = cell.size();

	            for(int i = 0; i < cellLength - 1; i++)
	            {
                       _checks.addElement(cell.elementAt(i));
	                for(int j = i + 1; j < cellLength; j++)
	                {
	                    _checks.addElement(cell.elementAt(j));	                }
	            }
    }

    private void checkTwoCells(int x1, int y1, int x2, int y2)
		{
                        // убеждаемся в наличии второй ячейки
			if(x2 >= _numCols || x2 < 0 || y2 >= _numRows) return;

			Vector cellA = _grid[y1 * _numCols + x1];
			Vector cellB = _grid[y2 * _numCols + x2];
			if(cellA == null || cellB == null) return;

			int cellALength = cellA.size();
			int cellBLength = cellB.size();

			for(int i = 0; i < cellALength; i++)
			{
				
                                 for(int j = 0; j < cellBLength; j++)
				{
                                        _checks.addElement(cellA.elementAt(i));
					_checks.addElement(cellB.elementAt(j));
				}
				
			}
                        
                       
		}

    
    
   public void update() {
        //drawGrid(GE.g);
        checkGrid();
        sortID();
        int numChecks = _checks.size() - 1;
        //System.out.println("numChecks "+numChecks);
        for (int j = 0; j < numChecks; j += 2) {
            checkCollision((Coordinat) _checks.elementAt(j), (Coordinat) _checks.elementAt(j + 1));
        }
        clear();
    }
   
   
   /*
 * Очишает сетку от обьектов
 */
private void clear() {

    for (int i = 0; i < _grid.length; i++) {
        if (_grid[i] != null)
       _grid[i].removeAllElements();
    }

    _checks.removeAllElements();


}
   
   
 /**
 * @author dzanis
 * сортирую,чтоб ID с большим значением был вторым
 * например чтоб лазер был первым
 */
    protected void sortID() {
        int numChecks = _checks.size() - 1;
        Coordinat t,t2;
         for (int j = 0; j < numChecks; j += 2) {
             
             t = (Coordinat)_checks.elementAt(j);
             t2 = (Coordinat)_checks.elementAt(j + 1);
              if ( t.ID > t2.ID )
         {
                 //Меняем местами               
                 _checks.setElementAt(t2,j);
                 _checks.setElementAt(t,j + 1);

         }
         
         }
    }


private  void checkCollision(Coordinat ballA,Coordinat ballB) {

        // если расстояние между центрами двух обьектов
        // меньше суммы их радиусов, то они столкнулись
    if(ballA.ID == ASTER &&  ballB.ID == ASTER  )
        return;
    if(ballA.ID == SHIP &&  ballB.ID == SHIP  )
        return;

        float AR = R[(int)ballA.ID];
        float BR = R[(int)ballB.ID];
        float dx = ((ballB.x + BR)- (ballA.x +AR));
        float dy = ((ballB.y +BR) - (ballA.y + AR));
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        
//        GE.g.setColor(0x00ff00);
//        GE.g.fillRect((int)(ballA.x+AR/2), (int)(ballA.y+AR/2), (int)AR,(int) AR);
//        GE.g.fillRect((int)(ballB.x+BR/2), (int)(ballB.y+BR/2),(int) BR, (int)BR);
        
        if (dist < AR+BR) {
            //ballA.y = scr_H;
           // ballB.y = scr_H;
            //System.out.print("checkCollision\n");

//            GE.g.setColor(0xff0000);
//            GE.g.fillRect((int)(ballA.x+AR/2), (int)(ballA.y+AR/2), (int)AR,(int) AR);
//            GE.g.fillRect((int)(ballB.x+BR/2), (int)(ballB.y+BR/2),(int) BR, (int)BR);


 
            if(ballA.ID == SHIP ){
  
                if(ballB.ID == ASTER){
                    Explosion.add(ballB.speed,ballB.x,ballB.y);
                ballB.y = GE.height;
                ship.Ship.shipCollize();
                }
                
                if(ballB.ID == ENEMY_LAZER)
                    ship.Ship.shipCollize();
                
                if(ballB.ID == ENEMY){
               Explosion.add(1F,ballB.x - 3,ballB.y - 9);
               ballB.y = - 360;
               Enemy_group.enemyKillCount++;
                    ship.Ship.shipCollize();
                }

            }
           else
            if(ballA.ID == LAZER  ){

                if(ballB.ID == ENEMY){
                    ballA.y = -30;
               Explosion.add(1F,ballB.x - 3,ballB.y - 9);
               GameInfo.asterKill +=2;
               ballB.y = - 360;
               Enemy_group.enemyKillCount++;
                }
                
                if(ballB.ID == ASTER  ){
                    ballA.y = -30; 
                 Explosion.add(ballB.speed,ballB.x, ballB.y);
                ballB.y = GE.height;
                GameInfo.asterKill ++;
               
            }
                

                
            }
            else
            if(ballA.ID == ENEMY && ballB.ID == ENEMY ){
                
//               ballA.x -= 1 * Intrusion.delta;
//               ballA.y -= 1 * Intrusion.delta;
//               ballB.x += 1 * Intrusion.delta;
//               ballB.y += 1 * Intrusion.delta;
                dx += ((float) GE.random.nextFloat() - 0.5f) * 0.001f;
                dy += ((float) GE.random.nextFloat() - 0.5f) * 0.001f;

                                // simulate the spring
                                final float d = (float) Math.sqrt(dx * dx + dy * dy);
                                final float c = (0.5f * ((AR+BR) - d)) / d;

                                ballA.x -= dx * c;
                                ballA.y -= dy * c;
                                ballB.x += dx * c;
                                ballB.y += dy * c;
      
            }
            else
            if(ballA.ID == ASTER && ballB.ID == ENEMY ){

                if (dist < (AR+BR)/2){
                    Explosion.add(1F,ballB.x,ballB.y);
               ballB.y = - 100;
                }
                    
                if(dx < 0)
                         ballB.x -= ballA.speed * GE.delta;
                     else
                         ballB.x += ballA.speed  * GE.delta;
                
            }
            
    

    }


    }





    

}
