package simpleftp.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple serveur TCP creant des thread pour gerer chaque connexion client.
 *
 */
public class FTPServerMain {

	/**
	 * Listen port.
	 */
	public static final int PORT = 7802;
	/**
	 * Socket server.
	 */
	private static ServerSocket socketaccueil;

	public static void main(String argv[]) throws Exception {
		// TO BE COMPLETED - MAKE IT MULTITHREAD - 4  LINES
		 final int coresNb = Runtime.getRuntime().availableProcessors();
		 final double coeff = 0.9;
		 final int poolSize = (int)(coresNb / (1 - coeff));
		 final ExecutorService executorPool = Executors.newFixedThreadPool(poolSize);
		try {
			socketaccueil = new ServerSocket(PORT);
			System.out.println("Server up and running.");
			FTPServer.setDebug(true);
			while (true) {
				FTPServer server = new FTPServer(socketaccueil.accept());
				// TO BE COMPLETED - 1 LINE
			executorPool.submit(server);
			}
			
		} finally {
			if (socketaccueil != null) {
				socketaccueil.close();
			}
		}
	}
}
