package es.deusto.ingenieria.ssd.chat.multicast.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import es.deusto.ingenieria.ssd.chat.multicast.client.MulticastClient;
import es.deusto.ingenieria.ssd.chat.multicast.data.User;
import es.deusto.ingenieria.ssd.chat.multicast.gui.JFrameMainWindow;


public class Controller {
	private  String ip;
	//private static final String DEFAULT_IP = "228.5.6.7";
	private int port ;
	private InetAddress group;
	private JFrameMainWindow window;
	public MulticastSocket multicastSocket;
	private User connectedUser;
	private User chatReceiver;
	//private static final String DEFAULT_MESSAGE = "Hello World!";
	
	//tiene a la ventana y el hilo
	public Controller(JFrameMainWindow jFrameMainWindow){
		window=jFrameMainWindow;
	}
	public void proccesInputMessage (String message){
		//el switch case con todos los mensajes aqui.
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
	public void receiveDatagramPacket(){
		try  {
			group = InetAddress.getByName(ip);
			
			byte[] buffer = new byte[1024];			
			DatagramPacket messageIn = null;
			
			
				messageIn = new DatagramPacket(buffer, buffer.length);
				multicastSocket.receive(messageIn);

				System.out.println(" - Received a message from '" + messageIn.getAddress().getHostAddress() + ":" + messageIn.getPort() + 
		                   		   "' -> " + new String(messageIn.getData()));
						
			
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
