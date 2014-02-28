/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of SimpleFTP.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: SimpleFTP.java,v 1.2 2004/05/29 19:27:37 pjm2 Exp $

*/

package simpleftp.client.control;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JFileChooser;


/**
 * SimpleFTP is a simple package that implements a Java FTP client.
 * With SimpleFTP, you can connect to an FTP server and upload multiple files.
 *  <p>
 * Copyright Paul Mutton,
 *           <a href="http://www.jibble.org/">http://www.jibble.org/</a>
 * 
 */
public class SimpleFTP {

	/**
     * Create an instance of SimpleFTP.
     */
    public SimpleFTP() {
        
    }
    
    
    /**
     * Connects to the default port of an FTP server and logs in as
     * anonymous/anonymous.
     */
    public void connect(String host) throws IOException {
        connect(host, 21);
    }
    
    
    /**
     * Connects to an FTP server and logs in as anonymous/anonymous.
     */
    public void connect(String host, int port) throws IOException {
        connect(host, port, "anonymous", "anonymous");
    }
    
    
    /**
     * Connects to an FTP server and logs in with the supplied username
     * and password.
     */
    public void connect(String host, int port, String user, String pass) throws IOException {
        if (socket != null) {
            throw new IOException("SimpleFTP is already connected. Disconnect first.");
        }
        
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
       // TO BE COMPLETED - 1 LINE
        input = new ObjectInputStream(socket.getInputStream());
        
        String response = readLine();
        if (!response.startsWith("220 ")) {
            throw new IOException("SimpleFTP received an unknown response when connecting to the FTP server: " + response);
        }
        
        sendLine("USER " + user);
        
        response = readLine();
        
        if (!response.startsWith("331 ")) {
        	System.out.println("2");
            throw new IOException("SimpleFTP received an unknown response after sending the user: " + response);
        }
        sendLine("PASS " + pass);
        
        response = readLine();
        if (!response.startsWith("230 ")) {
            throw new IOException("SimpleFTP was unable to log in with the supplied password: " + response);
        }
        // Now logged in.
    }
    
    
    /**
     * Disconnects from the FTP server.
     */
    public  void disconnect() throws IOException {
        try {
            sendLine("QUIT");
        }
        finally {
            socket = null;
        }
    }
    
    
    /**
     * Returns the working directory of the FTP server it is connected to.
     */
    public String pwd() throws IOException {
        sendLine("PWD");
        String dir = null;
        String response = readLine();
        if (response.startsWith("257 ")) {
        	System.out.println("PWD "+ response);
            int fsLastIndex = response.lastIndexOf(System.getProperty("file.separator"));
            if (fsLastIndex > 0) 
                dir = response.substring(fsLastIndex + 1);
             else 
            	 dir = response.substring(4);
        }
        return dir;
    }
    
    
    /**
     * Returns a list of files from a directory specified.
     */
    public String ls() throws IOException {
        sendLine("LIST");
        String list = null;
        String response = readLine();
        if (response.startsWith("200 ")) 
                   list = response.split(" ")[1];
        // TO BE COMPLETED - READ THE SERIALIZED OBJECT FROM THE NETWORK - 5 LINES
        
        try{
        	fileTree = (File)input.readObject();
        }
        catch(ClassNotFoundException e){
        	e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Changes the working directory (like cd). Returns true if successful.
     */   
    public boolean cwd(String dir) throws IOException {
        sendLine("CWD " + dir);
        String response = readLine();
        return (response.startsWith("250 "));
    }
    
    
    /**
     * Sends a file to be stored on the FTP server.
     * Returns true if the file transfer was successful.
     * The file is sent in passive mode to avoid NAT or firewall problems
     * at the client end.
     */
//    public synchronized boolean stor(File file) throws IOException {
//    	
//    	
//    	sendLine("PASV");
//        String response = readLine();
//        if (!response.startsWith("227 ")) {
//            throw new IOException("SimpleFTP could not request passive mode: " + response);
//        }
//        
//        String ip = null;
//        int port = -1;
////        int opening = response.indexOf('(');
////        int closing = response.indexOf(')', opening + 1);
////        if (closing > 0) {
////            String dataLink = response.substring(opening + 1, closing);
////            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
//            try {
//                ip = response.split(" ")[1];
//                //port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
//                port=Integer.parseInt(response.split(" ")[2]);
//            }
//            catch (Exception e) {
//                throw new IOException("SimpleFTP received bad data link information: " + response);
//            }
//        
//
//        Socket dataSocket = new Socket(ip, port);
//        
//        response = readLine();
//        if (!response.startsWith("150 ")) {
//            throw new IOException("SimpleFTP was not allowed to send the file: " + response);
//        }
//    	    	
//        InputStream is=new BufferedInputStream(new FileInputStream(file));
//        OutputStream os=dataSocket.getOutputStream();
//        int nbBytes=0;
//    	
//    	if (file.isDirectory()) {
//            throw new IOException("SimpleFTP cannot upload a directory.");
//        }
//        
//    	byte [] b=new byte[(int)(file.length())];
//        is.read(b);
//        os.write(b);
//        sendLine("STOR "+file.getName()+" "+file.length());
//        is.close();
//        os.close();
//        dataSocket.close();
//        System.out.println(file.getName());
//        response = readLine();
//        return (response.startsWith("226 "));
//    }
    
    
    public boolean stor(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("SimpleFTP cannot upload a directory.");
        }
        
        String filename = file.getName();

        return stor(new FileInputStream(file), filename);
    }
    
    public boolean storDownload(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("SimpleFTP cannot download a directory.");
        }
        
        String filename = file.getName();

        return storDownload(new FileOutputStream(file), filename);
    }
    
    
    
    /**
     * Sends a file to be stored on the FTP server.
     * Returns true if the file transfer was successful.
     * The file is sent in passive mode to avoid NAT or firewall problems
     * at the client end.
     */
    public  boolean stor(InputStream inputStream, String filename) throws IOException {

        BufferedInputStream input = new BufferedInputStream(inputStream);
        
        sendLine("PASV");
        String response = readLine();
        if (!response.startsWith("227 ")) {
            throw new IOException("SimpleFTP could not request passive mode: " + response);
        }
        
        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
            }
            catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: " + response);
            }
        }
        
        sendLine("STOR " + filename);
        
        Socket dataSocket = new Socket(ip, port);
        
        response = readLine();
        if (!response.startsWith("150 ")) {
            throw new IOException("SimpleFTP was not allowed to send the file: " + response);
        }
        
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();
        
        response = readLine();
        return response.startsWith("226 ");
    }
    
    public  boolean storDownload(OutputStream OutputStream, String filename) throws IOException {
    	 BufferedOutputStream output = new BufferedOutputStream(OutputStream);
    	    sendLine("PASV");
    	    String response = readLine();
    	    if (!response.startsWith("227 ")) {
    	        throw new IOException("SimpleFTP could not request passive mode: " + response);
    	    }
    	    String ip = null;
    	    int port = -1;
    	    int opening = response.indexOf('(');
    	    int closing = response.indexOf(')', opening + 1);
    	    if (closing > 0) {
    	        String dataLink = response.substring(opening + 1, closing);
    	        StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
    	        try {
    	            ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + 
    	        tokenizer.nextToken() + "." + tokenizer.nextToken();
    	            port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
    	        }
    	        catch (Exception e) {
    	            throw new IOException("SimpleFTP received bad data link information: " + response);
    	        }
    	    }    	    
    	    sendLine("RETR " + filename);
    	    Socket dataSocket = new Socket(ip, port);
    	    response = readLine();
    	    if (!response.startsWith("150 ")) {
    	        throw new IOException("SimpleFTP was not allowed to receiv the file: " + response);
    	    }
    	    BufferedInputStream input = new BufferedInputStream(dataSocket.getInputStream());
    	    byte[] buffer = new byte[4096];
    	    int bytesRead = 0;
    	    while ((bytesRead = input.read(buffer)) != -1) {
    	        output.write(buffer, 0, bytesRead);
    	    }
    	    output.flush();
    	    output.close();
    	    input.close();
    	    
    	    response = readLine();
    	    return response.startsWith("226 ");
    }
    
    
        
    



    /**
     * Enter binary mode for sending binary files.
     */
    public boolean bin() throws IOException {
        sendLine("TYPE I");
        String response = readLine();
        return (response.startsWith("201 "));
    }
    
    
    /**
     * Enter ASCII mode for sending text files. This is usually the default
     * mode. Make sure you use binary mode if you are sending images or
     * other binary data, as ASCII mode is likely to corrupt them.
     */
    public boolean ascii() throws IOException {
        sendLine("TYPE A");
        String response = readLine();
        return (response.startsWith("202 "));
    }
    
    
    /**
     * Sends a raw command to the FTP server.
     */
    private void sendLine(String line) throws IOException {
        if (socket == null) {
            throw new IOException("SimpleFTP is not connected.");
        }
        try {
            writer.write(line + "\r\n");
            writer.flush();
            if (DEBUG) {
                System.out.println("> " + line);
            }
        }
        catch (IOException e) {
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
    
    public File getFileTree() {
    	return this.fileTree;
    }
    
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    // TO BE COMPLETED - 1 LINE
    private ObjectInputStream input = null;
    private File fileTree;
    
    private static boolean DEBUG = false;
    

}