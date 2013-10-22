package es.deusto.ingenieria.ssd.chat.multicast.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class Controller {
	private  String ip;
	//private static final String DEFAULT_IP = "228.5.6.7";
	private int port ;
	private InetAddress group;
	public MulticastSocket multicastSocket;
	//private static final String DEFAULT_MESSAGE = "Hello World!";
	
	//tiene a la ventana y el hilo
	
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
			
			for (int i = 0; i < 3; i++) { // get messages from other 2 peers in the same group
				messageIn = new DatagramPacket(buffer, buffer.length);
				multicastSocket.receive(messageIn);

				System.out.println(" - Received a message from '" + messageIn.getAddress().getHostAddress() + ":" + messageIn.getPort() + 
		                   		   "' -> " + new String(messageIn.getData()));
			}
			
			
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
		return true;
	}
}
