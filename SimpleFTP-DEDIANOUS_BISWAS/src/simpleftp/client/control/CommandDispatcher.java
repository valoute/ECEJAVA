package simpleftp.client.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import simpleftp.client.gui.FTPClientWindow;

public class CommandDispatcher implements Runnable {
	public static final String NL = "\n";
	private static final String USER = "USER";
	private static final String PASS = "PASS";
	private static final String SERVER = "SERVER";
	private static final String PORT = "PORT";
	private static final String SETTINGS_ROOT = "simpleftpsettings";
	private static final String DEFAULT_SETTINGS = SETTINGS_ROOT + "/default";
	private static final String LAST_SETTINGS = SETTINGS_ROOT + "/last";
	private static final String DEF_USER = "paris";
	private static final String DEF_PASS = "paris";
	private static final String DEF_SERVER = "127.0.0.1";
	private static final String DEF_PORT = "7802";
	private final ExecutorService executorPool;
	private final CompletionService<Boolean> completionPool;
	private final FTPClientWindow window;
	private final int numberOfCores;
	private final double blockingCoefficient = 0.9;
	private final int poolSize;
	private BlockingQueue<Command> commands;
	private SimpleFTP client;
	private Preferences userPrefs;
	private String server;
	private String port;
	private String user;
	private String pass;
	private boolean alreadyConnected;

	public CommandDispatcher(BlockingQueue<Command> cmd, FTPClientWindow wind) {
		this.commands = cmd;
		this.window = wind;
		alreadyConnected = false;
		numberOfCores = Runtime.getRuntime().availableProcessors();
		poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
		executorPool = Executors.newFixedThreadPool(poolSize);
		completionPool = new ExecutorCompletionService<Boolean>(executorPool);
		client = new SimpleFTP();
	}

	@Override
	public void run() {
		try {
			loadStartupPreferences();
			Command cmd = this.commands.take();
			while (!cmd.equals(Command.QUIT)) {
				switch (cmd) {
				case CONNECT:
					connect();
					break;
				case DISCONNECT:
					disconnect();
					break;
				case STOR:
					upload();
					break;
				case LIST:
					listDir();
					break;
				case RETR:
					download();
					break;
				case PWD:
					pwd();
					break;
				case CWD:
					cwd();
					break;	
				case CLEAR:
					clearConsole();
					break;
				case LOADPREFS:
					loadPreferences();
					break;
				case SAVEPREFS:
					savePreferences();
					break;
				case DELPREF:
					deletePreference();
					break;
				default:
					break;
				}
				cmd = this.commands.take();
			}
			saveLastPreferences();
			disconnect();
			executorPool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void clearConsole() {
		window.console.setText("");
	}

	private void saveLastPreferences() {
		userPrefs = Preferences.userRoot().node(LAST_SETTINGS);
		userPrefs.put(USER, window.tfusername.getText());
		userPrefs.put(PASS, new String(window.pfuserpass.getPassword()));
		userPrefs.put(SERVER, window.tfservername.getText());
		userPrefs.put(PORT, window.tfport.getText());
		try {
			userPrefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	private void loadStartupPreferences() {
		try {
			userPrefs = Preferences.userRoot();
			if (!userPrefs.nodeExists(DEFAULT_SETTINGS)) {
				userPrefs = Preferences.userRoot().node(DEFAULT_SETTINGS);
				userPrefs.put(USER, DEF_USER);
				userPrefs.put(PASS, DEF_PASS);
				userPrefs.put(SERVER, DEF_SERVER);
				userPrefs.put(PORT, DEF_PORT);
				userPrefs.flush();
			}
			if (userPrefs.nodeExists(LAST_SETTINGS)) {
				userPrefs = Preferences.userRoot().node(LAST_SETTINGS);
			} 
			window.tfusername.setText(userPrefs.get(USER, DEF_USER));
			window.pfuserpass.setText(userPrefs.get(PASS, DEF_PASS));
			window.tfservername.setText(userPrefs.get(SERVER, DEF_SERVER));
			window.tfport.setText(userPrefs.get(PORT, DEF_PORT));
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPreferences() {
		try {
			userPrefs = Preferences.userRoot().node(SETTINGS_ROOT);
			String[] userSavedPrefs = userPrefs.childrenNames();
			String s = (String) JOptionPane.showInputDialog(window,
					"Choose preference to load:", "Load Preferences",
					JOptionPane.QUESTION_MESSAGE, null, userSavedPrefs, userSavedPrefs[0]);
			if ((s != null) && (s.length() > 0)) {
				userPrefs = Preferences.userRoot().node(SETTINGS_ROOT + "/" + s);
				window.tfusername.setText(userPrefs.get(USER, DEF_USER));
				window.pfuserpass.setText(userPrefs.get(PASS, DEF_PASS));
				window.tfservername.setText(userPrefs.get(SERVER, DEF_SERVER));
				window.tfport.setText(userPrefs.get(PORT, DEF_PORT));
				
				window.console.append(NL + "User preferences " + s + " loaded.");
			} else {
				window.console.append(NL + "Preferences loading operation cancelled.");
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	private void savePreferences() {
		String s = (String) JOptionPane.showInputDialog(window,
				"Save the current connection info as:", "Save Preferences",
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if ((s != null) && (s.length() > 0)) {
			userPrefs = Preferences.userRoot().node(SETTINGS_ROOT + "/" + s);
			userPrefs.put(USER, window.tfusername.getText());
			userPrefs.put(PASS, new String(window.pfuserpass.getPassword()));
			userPrefs.put(SERVER, window.tfservername.getText());
			userPrefs.put(PORT, window.tfport.getText());
			try {
				userPrefs.flush();
			} catch (BackingStoreException e) {
				window.console.append(NL + "Could not save preferences: "
						+ e.getMessage());
				e.printStackTrace();
			}
			window.console.append(NL + "Preferences saved as " + s);
		} else {
			window.console.append(NL + "Preferences saving operation cancelled.");
		}
	}
	
	private void deletePreference() {
		try {
			userPrefs = Preferences.userRoot().node(SETTINGS_ROOT);
			String[] userSavedPrefs = userPrefs.childrenNames();
			String s = (String) JOptionPane.showInputDialog(window,
					"Choose preference to delete:", "Delete Preference",
					JOptionPane.QUESTION_MESSAGE, null, userSavedPrefs, userSavedPrefs[0]);
			if ((s != null) && (s.length() > 0)) {
				userPrefs = Preferences.userRoot().node(SETTINGS_ROOT + "/" + s);
				userPrefs.removeNode();
				userPrefs.flush();
				window.console.append(NL + "User preferences " + s + " deleted.");
			} else {
				window.console.append(NL + "Preferences deletion operation cancelled.");
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	private void download() {
		if (!alreadyConnected) {
			window.console.append(NL + "You are not connected to any server.");
			return;
		}
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		fc.setDragEnabled(true);
		int returnVal = fc.showDialog(window, "Download");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			List<Future<Boolean>> downloaders = new ArrayList<Future<Boolean>>(
					files.length);
			for (File f : files) {
				Downloader downloader = new Downloader(f, window, server, port, user,pass);
				downloaders.add(completionPool.submit(downloader));
			}
		} else {
			window.console.append(NL + "Download action cancelled.");
		}
	}


	private void upload() {
		if (!alreadyConnected) {
			window.console.append(NL + "You are not connected to any server.");
			return;
		}
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		fc.setDragEnabled(true);
		int returnVal = fc.showDialog(window, "Upload");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			List<Future<Boolean>> uploaders = new ArrayList<Future<Boolean>>(files.length);
			for (File f : files) {
				Uploader uploader = new Uploader(f, window, server, port, user,
						pass);
				uploaders.add(completionPool.submit(uploader));
			}
		} else {
			window.console.append(NL + "Upload action cancelled.");
		}
	}

	private void listDir() {
		if (!alreadyConnected) {
			window.console.append(NL + "You are not connected to any server.");
			return;
		}
		try {
			String dirContent = client.ls();
			window.console.append(NL + dirContent);
			File f = client.getFileTree();
			window.root.removeAllChildren();
			DefaultMutableTreeNode userRoot = new DefaultMutableTreeNode(f.getName());
			populateTree(userRoot, f);
			window.root.add(userRoot);
		} catch (IOException e) {
			window.console.append(NL + e.getMessage());
			e.printStackTrace();
		}

	}
	
	private void populateTree(DefaultMutableTreeNode userRoot, File root) {
		
		// TO BE COMPLETED - WRITE THIS RECURSIVE METHOD - 7 LINES
		File[] files = root.listFiles();
		for(File f : files){
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(f.getName());
			userRoot.add(child);
			if(f.isDirectory()){
				populateTree(child,f);
			}
		}
			
	}

	private void pwd() {
		try {
			if (!alreadyConnected) {
				window.console.append(NL + "You are not connected to any server.");
				return;
			}
			String curDir = client.pwd();
			window.console.append(NL + "Current directory on FTP server: " + curDir);
		} catch (IOException e) {
			window.console.append(NL + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void cwd(){
		try{
			if (!alreadyConnected) {
				window.console.append(NL + "You are not connected to any server.");
				return;
			}
			
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(true);
			fc.setDragEnabled(true);
			int returnVal = fc.showDialog(window, "CWD");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				 String path = new String();
				File[] files = fc.getSelectedFiles();
				//List<Future<Boolean>> cwd= new ArrayList<Future<Boolean>>(files.length);
				for (File f : files) {
					 path = f.getAbsolutePath();
					
				}
				client.cwd(path);
				
			} 
			else {
				window.console.append(NL + "Upload action cancelled.");
			}
		}
		catch (IOException e) {
			window.console.append(NL + e.getMessage());
			e.printStackTrace();
		}
		
	}

	private void disconnect() {
		try {
			if (alreadyConnected) {
				client.disconnect();
				alreadyConnected = false;
				window.console.append(NL + "Now disconnected");
			} else {
				window.console.append(NL + "Already disconnected");
			}
		} catch (IOException e) {
			window.console.append(NL + e.getMessage());
			e.printStackTrace();
		}

	}

	private void connect() {
		if (alreadyConnected) {
			window.console.append(NL
					+ "You are already connected to this server.");
			return;
		}
		server = window.tfservername.getText();
		if (server == null || "".equals(server)) {
			window.console.append(NL
					+ "You must specify the server IP address.");
			return;
		}
		port = window.tfport.getText();
		if (port == null || "".equals(port)) {
			window.console.append(NL
					+ "You must specify the server port I must connect to.");
			return;
		}
		user = window.tfusername.getText();
		if (user == null || "".equals(user))
			user = "anonymous";
		pass = new String(window.pfuserpass.getPassword());
		if (pass == null || "".equals(pass))
			pass = "anonymous";
		try {
			client.connect(server, Integer.parseInt(port), user, pass);
			alreadyConnected = true;
			window.console.append(NL + "Now connected");
			pwd();
			listDir();
		} catch (IOException e) {
			window.console.append(NL + e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException nfe) {
			window.console.append(NL + nfe.getMessage());
			nfe.printStackTrace();
		}
	}
}
