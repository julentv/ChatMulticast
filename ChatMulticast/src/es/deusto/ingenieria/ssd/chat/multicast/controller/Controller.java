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
	
	
	public User getConnectedUser() {
		return connectedUser;
	}


	public void setConnectedUser(User connectedUser) {
		this.connectedUser = connectedUser;
	}

	
	public UserList getUserList() {
		return userList;
	}


	public void setUserList(UserList userList) {
		this.userList = userList;
	}


	private void generateMessage(String mess) throws IncorrectMessageException{
		String []arrMessage=mess.split("&");
		this.message= new Message();
		this.message.setFrom(this.userList.getUserByNick(arrMessage[1]));
		if(this.message.getFrom()==null)
			this.message.setFrom(new User(arrMessage[1]));
		this.message.setMessageType(Integer.valueOf(arrMessage[0]));
		if(this.message.hasUserTo()){
			this.message.setTo(this.userList.getUserByNick(arrMessage[2]));
			
			if(arrMessage.length>3)
				this.message.setText(mess.substring(arrMessage[0].length()+arrMessage[1].length()+arrMessage[2].length()+2));
		}else{
			if(arrMessage.length>2)
			this.message.setText(mess.substring(arrMessage[0].length()+arrMessage[1].length()+1));
		}
	}
	
	public void sendMessage(String message){
		sendDatagramPacket(message);
	}
	public void proccesInputMessage (String receivedMessage) throws IncorrectMessageException{
		//el switch case con todos los mensajes aqui.
		String messageToSend;
		String warningMessage;
		String time;
		generateMessage(receivedMessage);
		
		//Si el que envia el sms no soy yo mirar si el sms es para mi
		System.out.println("soy yo: "+!this.message.getFrom().getNick().equals(connectedUser.getNick()));
		if (this.message.isLogginMessage() ||(!this.message.isLogginMessage() && !this.message.getFrom().getNick().equals(connectedUser.getNick()))){
			
			//si el sms es para mi procesar
			if (this.message.getTo()==null || this.message.getTo().getNick().equals(connectedUser.getNick())){
				
				switch (message.getMessageType()){
				
				case Message.CLIENT_MESSAGE_LOGIN:
					if (userList.getUserByNick(this.message.getFrom().getNick())==null){
						//si no exist el ultimo de la lista envia la lista de usuarios
						
						if (userList.getLastUser().getNick().equals(connectedUser.getNick())){
							this.userList.add(this.message.getFrom());
							messageToSend="108&"+this.connectedUser.getNick()+userList.toString();
							sendDatagramPacket(messageToSend);
						}else{
							this.userList.add(this.message.getFrom());
						}
						this.window.refreshUserList();
						
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
					this.userList.fromString(this.message.getText());
					this.window.refreshUserList();
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
	
	public boolean isConnected() {
		return this.connectedUser != null;
	}
	
	public boolean isChatSessionOpened() {
		return this.chatReceiver != null;
	}
	
	public boolean connect(String ip, int port, String nick) throws IOException{
		this.port=port;
		this.multicastSocket= new MulticastSocket(port);
		this.group = InetAddress.getByName(ip);
		multicastSocket.joinGroup(group);
		userList= new UserList();
		this.connectedUser= new User(nick);
		userList.add(connectedUser);
		String message= "101&"+this.connectedUser.getNick();
		sendDatagramPacket(message);
		MulticastClient multicastClient = new MulticastClient(this);
		multicastClient.start();
		return true;
	}
	
public boolean disconnect() {
		
		String message;
//		//ENTER YOUR CODE TO DISCONNECT
		if (isChatSessionOpened()){
			sendChatClosure();
		}
		
		message= "106";
		sendDatagramPacket(message);
		
		this.connectedUser = null;
		this.chatReceiver = null;
		
		
		return true;
	}



/**
 * This method is used to send the message of closing the chat
 * @return true
 */
public boolean sendChatClosure() {
	
	//ENTER YOUR CODE TO SEND A CHAT CLOSURE
	String message="105&"+this.chatReceiver.getNick();
	sendDatagramPacket(message);
	this.chatReceiver = null;
	
	return true;
}
}
