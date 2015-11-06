// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  String loginID;
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
	  super(host, port); //Call the superclass constructor
	  this.clientUI = clientUI;
	  this.loginID = loginID;
	  openConnection();
	  sendToServer("#login "+loginID);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	if(message.charAt(0) == '#'){
		try{
		handleClientCommands(message);
		}
		catch(IOException e){
			System.out.println(e);
		}
	}	
	else{
	    try
	    {
	      sendToServer(message);
	    }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
    }
  }
  private void handleClientCommands(String message) throws IOException{
	  //create string array to handle setHost and setPort
	  String[] splittedMessage = message.split(" ", 2);
	  switch (splittedMessage[0]){ 
		  case "#quit": quit();
		  	break;
		  case "#logoff" : closeConnection();
		  	break;
		  	//case to setHost, checks if connected, then removes < and > before setting.
		  case "#sethost" :
			if(!isConnected()) {
				setHost(splittedMessage[1].replace("<", "").replace(">", ""));
		  	}
			else{
				throw new IOException("Please logoff before setting host");
			}
		  	break;
		  case "#setport":
		  	if(!isConnected()) {
		  		setPort(Integer.parseInt(splittedMessage[1].replace("<", "").replace(">", "")));
		  	}
			else{
				throw new IOException("Please logoff before setting port");
			}
		  	break;
		  case "#login":
			if(!isConnected()) {
				openConnection();
		  	}
			else{
				throw new IOException("Please logoff before attempting to login");
			}
			break;
		  case "#gethost":
			  clientUI.display("Host: "+ getHost());
			break;
		  case "#getport":
			  clientUI.display("Port: "+ getPort());
			  break;
		  default:
			  throw new IOException("Invalid Command"); 
		  	
		  	
	  }
		  	
  }
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  protected void connectionException(Exception exception) {
	  System.out.println("The client will be closed");
	  connectionClosed(false);
  }
  protected void connectionClosed(boolean keepalive) {
	 if (!keepalive)
		 System.exit(0);
  }
}
//End of ChatClient class
