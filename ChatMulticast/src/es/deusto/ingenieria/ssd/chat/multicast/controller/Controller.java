package es.deusto.ingenieria.ssd.chat.multicast.controller;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	public User chatReceiver;
	private Message message;
	private UserList userList;
	private SimpleDateFormat textFormatter = new SimpleDateFormat("HH:mm:ss");
	
	//tiene a la ventana y el hilo
	public Controller(JFrameMainWindow jFrameMainWindow){
		window=jFrameMainWindow;
	}
	
	private void generateMessage() throws IncorrectMessageException{
		
		
	}
	
	public void sendMessage(String message){
		sendDatagramPacket(message);
	}
	public void proccesInputMessage (String receivedMessage) throws IncorrectMessageException{
		//el switch case con todos los mensajes aqui.
		String messageToSend;
		String warningMessage;
		String time;
		generateMessage();
		
		//Si el que envia el sms no soy yo mirar si el sms es para mi
		if (!this.message.getFrom().getNick().equals(connectedUser.getNick())){
			//si el sms es para mi procesar
			if (this.message.getTo()==null || this.message.getTo().getNick().equals(connectedUser.getNick())){
				
				switch (message.getMessageType()){
				
				case Message.CLIENT_MESSAGE_LOGIN:
					if (userList.getUserByNick(this.message.getFrom().getNick())==null){
						//si no exist el ultimo de la lista envia la lista de usuarios
						if (userList.getLastUser().getNick().equals(connectedUser.getNick())){
							messageToSend="108"+userList.toString();
							sendDatagramPacket(messageToSend);
						}
					}
					else{
						//si hay nick ese usuario envia el sms de error 301
						if (this.message.getFrom().getNick().equals(connectedUser.getNick())){
							messageToSend="301&"+this.connectedUser.getNick();
							sendDatagramPacket(messageToSend);
						}
					}
										
						
					break;
				case Message.CLIENT_MESSAGE_ESTABLISH_CONNECTION:
					//si ya estoy hablando mandar already chatting
					if (this.chatReceiver!=null){
						
						boolean acceptInvitation= this.window.acceptChatInvitation(message.getFrom().getNick());
						if (acceptInvitation){
							this.chatReceiver= new User(message.getFrom().getNick());
							messageToSend="103&"+this.connectedUser.getNick()+"&"+chatReceiver.getNick();
							sendDatagramPacket(messageToSend);
						}
						else{
							messageToSend="104&"+this.connectedUser.getNick()+"&"+chatReceiver.getNick();
							sendDatagramPacket(messageToSend);
						}
					}
					else{
						messageToSend= "303&"+this.connectedUser.getNick()+"&"+message.getFrom().getNick();
						sendDatagramPacket(messageToSend);
											 
					}
					
					break;
				case Message.CLIENT_MESSAGE_ACCEPT_INVITATION:
					time = textFormatter.format(new Date());		
					warningMessage = " " + time + ": BEGINING OF THE CONVERSATION WITH ["+this.chatReceiver.getNick()+"]\n";
					this.window.appendMessageToHistory(warningMessage, Color.GREEN);
					break;
				case Message.CLIENT_MESSAGE_REJECT_INVITATION:
					warningMessage= message.getFrom().getNick()+" has rejected your invitation";
					this.window.showMessage(warningMessage);
					break;
				case Message.CLIENT_MESSAGE_CLOSE_CONVERSATION:
					warningMessage= message.getFrom().getNick()+" has closed the conversation";
					this.window.showMessage(warningMessage);
					break;
				case Message.CLIENT_MESSAGE_CLOSE_CONNECTION:
					this.userList.deleteByNick(this.message.getFrom().getNick());
					this.window.refreshUserList();
					break;
				case Message.CLIENT_MESSAGE:
					 time = textFormatter.format(new Date());		
					  warningMessage = " " + time + " - [" + this.message.getFrom() + "]: " + message.getText().trim() + "\n";
					this.window.appendMessageToHistory(warningMessage, Color.MAGENTA);
					break;
				case Message.CLIENT_MESSAGE_USER_LIST:
					//lista de usuarios
					break;
				default:
					throw new IncorrectMessageException("The message type code does not exist");
				}
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
