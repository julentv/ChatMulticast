package es.deusto.ingenieria.ssd.chat.multicast.client;

import es.deusto.ingenieria.ssd.chat.multicast.controller.Controller;



public class MulticastClient extends Thread {

private Controller controller;
	
	public MulticastClient(Controller controller){
		this.controller=controller;
	}
	
	

}
