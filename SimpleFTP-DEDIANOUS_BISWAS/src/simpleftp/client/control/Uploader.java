package simpleftp.client.control;

import java.io.File;
import java.util.concurrent.Callable;

import simpleftp.client.gui.FTPClientWindow;

public final class Uploader implements Callable<Boolean> {

	private SimpleFTP client;
	private String server;
	private String port;
	private FTPClientWindow wind;
	private String user;
	private String pass;
	private File file;

	public Uploader(File f, FTPClientWindow window, String server, String port,
			String user, String pass) {
		this.file = f;
		this.server = server;
		this.port = port;
		this.wind = window;
		this.user = user;
		this.pass = pass;
		client = new SimpleFTP();
	}

	@Override
	public Boolean call() throws Exception {
		Boolean result = false;
		client.connect(server, Integer.parseInt(port), user, pass);
		result = client.stor(file);
		if (result) {
			wind.console.append("Successfully transfered file "+ file.getName());
		} else {
			wind.console.append("Failed transfering file " + file.getName());
		}
		return result;
	}
}
