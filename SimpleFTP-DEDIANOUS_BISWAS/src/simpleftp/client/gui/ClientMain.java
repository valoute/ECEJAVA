package simpleftp.client.gui;

import java.awt.EventQueue;

public class ClientMain {

	private ClientMain() {
		super();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FTPClientWindow frame = new FTPClientWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
