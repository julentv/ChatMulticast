package es.deusto.ingenieria.ssd.chat.multicast.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import es.deusto.ingenieria.ssd.chat.multicast.client.MulticastClient;
import es.deusto.ingenieria.ssd.chat.multicast.data.Message;
import es.deusto.ingenieria.ssd.chat.multicast.data.User;
import es.deusto.ingenieria.ssd.chat.multicast.data.UserList;
import es.deusto.ingenieria.ssd.chat.multicast.exceptions.IncorrectMessageException;
import es.deusto.ingenieria.ssd.chat.multicast.gui.JFrameMainWindow;


public class Controller {
	public  String ip;
	//private static final String DEFAULT_IP = "228.5.6.7";
	private int port ;
	private InetAddress group;
	private JFrameMainWindow window;
	public MulticastSocket multicastSocket;
	private User connectedUser;
	private User chatReceiver;
	private Message message;
	private UserList userList;
	
	//tiene a la ventana y el hilo
	public Controller(JFrameMainWindow jFrameMainWindow){
		window=jFrameMainWindow;
	}
	
	private void generateMessage() throws IncorrectMessageException{
		
		
	}
	
	
	public void proccesInputMessage (String receivedMessage) throws IncorrectMessageException{
		//el switch case con todos los mensajes aqui.
		
		generateMessage();
		if (this.message.getTo()==null || this.message.getTo().getNick().equals(connectedUser.getNick())){
			switch (message.getMessageType()){
			case Message.CLIENT_MESSAGE_LOGIN:
				if (userList.getUserByNick(this.message.getFrom().getNick())==null){
					//si no exist el ultimo de la lista envia la lista de usuarios
				}
				else{
					//si hay nick ese usuario envia el sms de error 301
				}
				
					
					
				break;
			case Message.CLIENT_MESSAGE_ESTABLISH_CONNECTION:
				//
				break;
			case Message.CLIENT_MESSAGE_ACCEPT_INVITATION:
				//
				break;
			case Message.CLIENT_MESSAGE_REJECT_INVITATION:
				//
				break;
			case Message.CLIENT_MESSAGE_CLOSE_CONVERSATION:
				//
				break;
			case Message.CLIENT_MESSAGE_CLOSE_CONNECTION:
				//
				break;
			case Message.CLIENT_MESSAGE:
				//
				break;
			case Message.CLIENT_MESSAGE_USER_LIST:
				//lista de usuarios
				break;
			default:
				throw new IncorrectMessageException("The message type code does not exist");
			}
		}
		
		
	}
	
	private void sendDatagramPacket(String message){
		try  {
											
			DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.length(), group, port);
			multicastSocket.send(messageOut);
			System.out.println(" - Sent a message to '" + messageOut.getAddress().getHostAddress() + ":" + messageOut.getPort() + 
			                   "' -> " + new String(messageOut.getData()));
			
		} catch (SocketException e) {
			System.err.println("# Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# IO Error: " + e.getMessage());
		}
	}
	
	
	public boolean connect(String ip, int port, String nick) throws IOException{
		this.multicastSocket= new MulticastSocket(port);
		InetAddress group = InetAddress.getByName(ip);
		multicastSocket.joinGroup(group);
		this.connectedUser= new User(nick);
		String message= "101&"+this.connectedUser.getNick();
		sendDatagramPacket(message);
		MulticastClient multicastClient = new MulticastClient(this);
		multicastClient.start();
		return true;
	}
}
