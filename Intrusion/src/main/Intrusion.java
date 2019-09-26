package main;


import UI.TextView;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class Intrusion extends MIDlet implements Runnable{


    GE gr;
    public static Thread thread;
    boolean firstStart  = false;
    public static boolean runnable  = true;



public Intrusion() {
        gr = new GE();
    }

    public void  startApp() {

        Display.getDisplay(this).setCurrent(gr);
        //firstStart нужен для того,чтоб после сворачивания или звонка (suspend)
        //игра не зависала
        //(зависала потому что дублировался поток,ведь после suspend'a вызывается startApp )
        if (!firstStart) {
            (thread = new Thread(this)).start();          
            firstStart = true;
        }
        
        


    }

    public void pauseApp() {
        System.gc();
    }

    public void destroyApp(boolean paramBoolean) {
        runnable = false;
        System.gc();
        notifyDestroyed();
    }


    
    //  цикл

    public void run() {

        try{


        while ((!runnable) || (Display.getDisplay(this).getCurrent() == null))
        Thread.yield();
      Thread.sleep(50L);




        while (runnable) {
            gr.process();
            try {
                    Thread.sleep(2L);
            } catch (InterruptedException e) {}
           
        }

        } catch (Throwable error){
            viewError(error);
        }
        
        thread = null;
        destroyApp(false);

 }

 
    
    

   protected void viewError(Throwable error) {
        error.printStackTrace();
        TextView.windowsInit(11,11,GE.width - 11*2,GE.height - 11 * 2,error.toString(),
                "Error!",Graphics.VCENTER);
        gr.PROCESS = gr.INFA;
        gr.process();
        try {
          Thread.sleep(5000L);
            } catch (InterruptedException e) {}

    }
   
   
   
   
   
   
    
}


