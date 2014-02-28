package simpleftp.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JTextArea;

public final class GUICommandListener<T> implements ActionListener {

	private BlockingQueue<T> cmdQueue;
	private JTextArea console;
	private T cmd;

	public GUICommandListener(BlockingQueue<T> cmdQueue, JTextArea console, T t) {
		this.cmdQueue = cmdQueue;
		this.console = console;
		this.cmd = t;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			cmdQueue.put(cmd);
		} catch (InterruptedException e) {
			console.append("\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}
