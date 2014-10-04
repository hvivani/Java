import java.io.*;  
import java.net.*;  
import java.util.*;  
import java.util.TimerTask;  
//import org.apache.commons.*;
//import org.apache.commons.net.telnet.TelnetClient;  
class Connectivity extends TimerTask  
{  
    public static void main(String args[])  
    {  
        try  
        {  
            System.out.println("Please enter ip address");  
            Scanner sc=new Scanner(System.in);  
            String ip=sc.nextLine().trim();  
            System.out.println("Please enter port number");  
            TimerTask con  = new Connectivity();  
            Scanner sc1=new Scanner(System.in);  
            int port=sc1.nextInt();  
            Timer timer = new Timer();  
            timer.scheduleAtFixedRate(con,1,1000);  
            Socket s1=new Socket(ip,port);  
            InputStream is=s1.getInputStream();  
            DataInputStream dis=new DataInputStream(is);  
            if(dis!=null)  
            {  
                System.out.println("Connected with ip "+ip+" and port "+port);  
            }  
            else  
            {  
                System.out.println("Connection invalid");  
            }  
              
            dis.close();  
            s1.close();  
              
        }  
        catch(Exception e)  
        {  
            System.out.println("Not Connected,Please enter proper input");  
              
        }  
          
    }  
  
    @Override  
    public void run() {  
        // TODO Auto-generated method stub  
          
    }  
}
