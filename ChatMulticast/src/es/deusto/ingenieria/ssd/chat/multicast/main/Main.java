package es.deusto.ingenieria.ssd.chat.multicast.main;

import java.awt.EventQueue;

import es.deusto.ingenieria.ssd.chat.multicast.gui.JFrameMainWindow;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					JFrameMainWindow frame = new JFrameMainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
