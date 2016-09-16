import java.io.IOException;
import java.util.LinkedList;


public class ServerApp {

	
	public static void main(String[] args) {
		
		LinkedList<ServerAppThread> controlSockets = new LinkedList<ServerAppThread>();
		
		try {
			
			ServerControlSocket controlSocketListener = new  ServerControlSocket(45000);
			ServerDataSocket dataSocketListener = new ServerDataSocket(46000);
			
			System.out.println("waiting for user...");
			while (true) {
				
				ControlSocket controlSocket = controlSocketListener.accept();
				System.out.println("User connected...");
				controlSockets.addFirst(new ServerAppThread(controlSocket, dataSocketListener));
				controlSockets.getFirst().start();
			}
			
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
}
