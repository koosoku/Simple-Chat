
import java.io.*;

import common.*;

public class ServerConsole implements ChatIF 
{

	EchoServer server;
	final public static int DEFAULT_PORT = 5555;
	
	public ServerConsole(int port){
		server = new EchoServer(port,this);
	}

 
	public void accept(){
		try{
			BufferedReader fromConsole = 
			new BufferedReader(new InputStreamReader(System.in));
			String message;

			while (true){
				message = fromConsole.readLine();
				server.handleMessageFromServerUI(message);
			}
		} 
		catch (Exception ex) 
		{
			System.out.println
			("Unexpected error while reading from console!");
		}
	}
	public void display(String message){
		System.out.println(">" + message);
    }
  

	 public static void main(String[] args) 
	 {
		 int port = 0; //Port to listen on
	
		 try
		 {
			 port = Integer.parseInt(args[0]); //Get port from command line
		 }
		 catch(Throwable t)
		 {
			 port = DEFAULT_PORT; //Set port to 5555
		 }
		
		 ServerConsole sc = new ServerConsole(port);
	    
		 try 
		 {
			 sc.server.listen(); //Start listening for connections
		 } 
		 catch (Exception ex) 
		 {
			 System.out.println("ERROR - Could not listen for clients!");
		 }
		 sc.accept();
	  }
}

