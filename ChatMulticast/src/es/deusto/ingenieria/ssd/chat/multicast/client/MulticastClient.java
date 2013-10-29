package es.deusto.ingenieria.ssd.chat.multicast.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import es.deusto.ingenieria.ssd.chat.multicast.controller.Controller;
import es.deusto.ingenieria.ssd.chat.multicast.data.Message;



public class MulticastClient extends Thread {
	
	private Controller controller;
	private InetAddress group;
	
	public MulticastClient(Controller controller){
		this.controller=controller;
	}
	
	public DatagramPacket receiveDatagramPacket(){
		try  {
			
			byte[] buffer = new byte[1024];			
			DatagramPacket messageIn = null;
			
			
				messageIn = new DatagramPacket(buffer, buffer.length);
				System.out.println(messageIn.getAddress().getHostAddress());
				controller.multicastSocket.receive(messageIn);
				
				System.out.println(" - Received a message from '" + messageIn.getAddress().getHostAddress() + ":" + messageIn.getPort() + 
		                   		   "' -> " + new String(messageIn.getData()));
						
			return messageIn;
		} catch (SocketException e) {
			System.err.println("# Socket Error: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.err.println("# IO Error: " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public void run(){
		DatagramPacket receivedPacket= receiveDatagramPacket();
		System.out.println("aaaaaa");
		String receivedMessage= new String(receivedPacket.getData());
		receivedMessage= receivedMessage.trim();
				
		
			
		}
			
		
	}


