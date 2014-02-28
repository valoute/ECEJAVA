package simpleftp.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Void;
import java.util.concurrent.Callable;

import simpleftp.client.control.Command;

/**
 * ServeurFTP is a simple package that implements a Java FTP server. With
 * ServeurFTP, you can connect to an FTP client and download multiple files. 
 */
public class FTPServer implements Callable<Void> {
	private static boolean DEBUG = false;
	private String response, user;
	private int validations;
	private File f;
	private StringBuilder sb;
	private int portnum1, portnum2;
	private Properties p;
	private ServerSocket dl;
	private Socket dl2;
	private FileInputStream fis;
	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	// TO BE COMPLETED - 1 LINE
	private ObjectOutputStream output = null;
	
	public FTPServer(Socket s) {
		socket = s;
		validations = 0;
		try {
			p = new Properties();
			fis = new FileInputStream("users.properties");
			p.load(fis);
			try {
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				// TO BE COMPLETED - 1 LINE
				output = new ObjectOutputStream(socket.getOutputStream());
				
				sendLine("220 ");
				validations++;
				f = new File(System.getProperty("user.dir"));	
				System.out.println("FTP Server : new client from " + socket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				try {
					sendLine("error");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} catch (IOException ioex) {
			System.out.println("Error: Properties File didn't load");
			try {
				sendLine("error ");
			} catch (IOException e) {
				e.printStackTrace();
			}
			ioex.printStackTrace();
		}
	}

	
	public Void call() {
		try {
			response = readLine();
			if (response.startsWith("USER")) {
				if (p.containsKey(response.substring(5))) {
					user = response.substring(5);
					System.out.println("Server knows user " + response);
					sendLine("331 ");
					validations++;
				} else {
					System.out.println("Server does no know user " + response);
					sendLine("error ");
				}
			}
			response = readLine();
			if (response.startsWith("PASS")) {
				if (p.get((String) user).equals(response.substring(5))) {
					System.out.println("Server validates pass " + response);
					sendLine("230 ");
					validations++;
				} else {
					sendLine("error ");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				sendLine("error ");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		while (validations == 3) {
			try {
				response = readLine();

				switch (Command.valueOf(response.split(" ")[0])) {

				case PWD:
					sendLine("257 " + System.getProperty("user.dir"));
					break;

				case LIST:
					sb = new StringBuilder("200 ");
					String[] fileNames = f.list();
					for (String fileName : fileNames) {
						sb = sb.append(fileName + "|");
					}
					sendLine(sb.toString());
					// TO BE COMPLETED - 1 LINE
					sendObject(f);
					break;

				case CWD:
					System.out.println("CWD");
					System.setProperty("user.dir", response.substring(4));
					System.out.println("CWD new current directory:"
							+ System.getProperty("user.dir").toString());
					f = new File(System.getProperty("user.dir"));
					sendLine("250 ");
					break;

				case PASV:
					pasv();
					break;

				case STOR:
					stor();
					break;
					
				case RETR:
					storDownload();
					break;

				case QUIT:
					socket.close();
					System.out.println("QUIT :Goodbye");
					validations = 0;
					break;

				default:
					System.out.println("Unknown Command.");
					sendLine("error");
					break;

				}
			} catch (IOException e) {
				try {
					sendLine("error");
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		}
		Void Void = null;
		return Void;
	}
	
	// TO BE COMPLETED - SEND SERIALIZABLE OBJECT OVER THE NETWORK - 5 LINES 
private void sendObject(File f2) throws IOException{
	if(socket == null){
		throw new IOException("FTPServer is not connected");
	}
	populate(f2);
	output.writeObject(f2);
}
//Populate method - completed
private void populate(File root, File f){
	File fRoot = new File(root, f.getName());
	for(File it : f.listFiles()){
		if(it.isDirectory())
		populate(f,it);
		else {
			File child = new File(root,it.getName());
		
		}
	}
	
	
}
private void populate(File root){
	for(File it : root.listFiles()){
		File f = new File(root,it.getName());
		if(it.isDirectory()){
			populate(it);
		}
	}
}
	/**
     * Sends a raw command to the FTP server.
     */

	private void sendLine(String line) throws IOException {
		if (socket == null) {
			throw new IOException("FTPServer is not connected.");
		}
		try {
			writer.write(line + "\r\n");
			writer.flush();
			if (DEBUG) {
				System.out.println("> " + line);
			}
		} catch (IOException e) {
			socket = null;
			throw e;
		}
	}

	private String readLine() throws IOException {
		String line = reader.readLine();
		if (DEBUG) {
			System.out.println("< " + line);
		}
		return line;
	}

	private void stor() {
		try {
			dl2 = dl.accept();
			sendLine("150 ");
			OutputStream os = new FileOutputStream(new File(System.getProperty("user.dir")+ System.getProperty("file.separator")
							+ response.substring(5)));
			InputStream is = dl2.getInputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
			os.close();
			is.close();
			sendLine("226 ");
			dl.close();
			dl2.close();
			System.out.println("STOR " + response.split(" ")[1]
					+ " : File received");
		} catch (IOException e) {
			try {
				sendLine("error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private void storDownload() {
		try {
			dl2 = dl.accept();
			sendLine("150 ");
			InputStream is = new FileInputStream(new File(System.getProperty("user.dir")+ System.getProperty("file.separator")
							+ response.substring(5)));
			OutputStream os = dl2.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
			os.close();
			is.close();
			sendLine("226 ");
			dl.close();
			dl2.close();
			System.out.println("RETR " + response.split(" ")[1]
					+ " : File Downloaded");
		} catch (IOException e) {
			try {
				sendLine("error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	
	
	
	
	

	private void pasv() {
		try {
			dl = new ServerSocket(0);
			portnum1 = dl.getLocalPort() / 256;
			portnum2 = dl.getLocalPort() % 256;
			sendLine("227 ("
					+ InetAddress.getLocalHost().getHostAddress()
							.replace('.', ',') + "," + portnum1 + ","
					+ portnum2 + ")");
			System.out.println("PASV success");
		} catch (IOException e) {
			try {
				sendLine("error ");
				System.out.println("pasv error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public static void setDebug(boolean b) {
		DEBUG = b;
	}
}
