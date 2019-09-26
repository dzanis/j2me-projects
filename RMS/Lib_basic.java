
import com.siemens.mp.io.file.*;
import javax.microedition.io.*;                        
import java.io.*;
import java.util.Enumeration;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.rms.*;

        public class Lib_basic {


   

        public static String list(int idx) {
   StringBuffer s = new StringBuffer();
   try{
         
    String as[] = RecordStore.listRecordStores();
    if(as != null)
            {
                for(int l = 0; l < as.length; l++)
                 
                        s.append(as[l]).append('|');

            }
   
   }catch(Exception e) {return "";}
   return s.toString();
}         


       
 public static void read_rs(String name, String info){
try{

RecordStore myStore = RecordStore.openRecordStore(name, true);
StringBuffer f = new StringBuffer();
RecordEnumeration rc = myStore.enumerateRecords(null, null, true);
rc.rebuild();
byte[] data = null;
byte[] d =  new byte [(rc.numRecords())*256];
int x = 0;
for(int i = 1; i <= rc.numRecords(); i++){
data = rc.nextRecord();
for(int c = 0; c <= 255; c++){ 
d[x] = data[c];
x =x+1; }
}
myStore.closeRecordStore();

            int sa = 256;
            x = d.length;
            int t = 8;
			int p = 0;
            int r1 = d[x-250];
			if(r1 < 0)
            r1 =r1+256;
            int r2 = d[x-249];
            if(r2 < 0)
            r2 =r2+256;
            byte[] d2 =  new byte [x-(256-r2)-8];
            
            if(r1 != 0)
            for(int k = 0; k < r1; k++){
            for(int i =(x-sa)+t; i < ((x-sa)+t)+(256-t); i++){
                d2[p] = d[i];
                p =p+1;		}
            sa=sa+256;
            t=0;      }            
            for(int y = (x-sa)+t; y < ((x-sa)+t)+(r2-t); y++){
                d2[p] = d[y];
                p =p+1; }   

FileConnection fs = (FileConnection)Connector.open("file://" + info);
if(!fs.exists())
fs.create();
DataOutputStream os = fs.openDataOutputStream();
os.write(d2, 0, d2.length);
os.flush();
os.close();

} catch(Exception e) { }
        
      
       
  }     


public static void write_rs(String name,String list,int r1,int r2) { 
    try {
      RecordStore myStore = RecordStore.openRecordStore(name,true);
      RecordEnumeration rc;
      byte[] data = new byte[256];
       int sa = 0;
       int t = 8;
       int l = 0;
        data[0]=(byte)82;
    data[1]=(byte)65;
     data[2]=(byte)0;
      data[3]=(byte)1;
       data[4]=(byte)0;
        data[5]=(byte)0; 
         data[6]=(byte)r1;
          data[7]=(byte)r2;
     if(r1 != 0)  
          for (int i=0;i < r1;i++)//>
            {
             for(int c = t; c < 256; c++)
            {
                char b = list.charAt(l);
                data[c] = (byte)b;
                 l=l+1;
            }
             myStore.addRecord(data,0,data.length);
            t=0;
            }
            if(r1 != 0)
            for(int p = 0; p < 256; p++)
            {data[p] = 0;}
            for(int k = t; k < r2; k++)
            {
                char b = list.charAt(l);
                data[k] = (byte)b;
                l=l+1;
            }
            myStore.addRecord(data,0,data.length);
  
            
            myStore.closeRecordStore();
        } 
      catch(Exception e) { }
        
      
       
  }     


 public static String get_files(String path)  {
        StringBuffer s = new StringBuffer();
         try        {
         FileConnection fs = (FileConnection)Connector.open("file://" + path);
            Enumeration r = fs.list();
            fs.close();
            do
            {
                if(!r.hasMoreElements())
                    break;
                String t = (String)r.nextElement();
                if(!t.endsWith("/"))
                    s.append(t).append('|');
            } while(true);
        }
        catch(Exception e)
        {
            return "";
        }
        return s.toString();
    }




public static String read_file_a(String name) {
        StringBuffer s = new StringBuffer();
        try        {
        FileConnection fs =(FileConnection)Connector.open("file://" + name);
            int sz = (int)fs.fileSize();
            byte d[] = new byte[sz];
            DataInputStream is = fs.openDataInputStream();
            is.read(d, 0, sz);
            for(int i = 0; i < sz; i++)
            {
                byte b = d[i];
                s.append((char)b);
            }

        }
        catch(Exception e)
        {
            return "";
        }
        return s.toString();
    }

public static int file_exists(String name) {
   try{
      FileConnection fs = (FileConnection)Connector.open("file://"+name);
      if(fs.exists()) return 1;
   }catch(Exception e) {return 0;}
   return 0;
}


 public static void delete_file(String name)    {
 try        {            
 FileConnection fs =(FileConnection)Connector.open("file://" + name);
            fs.delete();
        }
        catch(Exception e)
        {
            return;
        }
    }

private static FileConnection fc = null;
private static DataInputStream dis = null;
private static DataOutputStream dos = null;  
}
