import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class ServerApp {

	
	public static void main(String[] args) {
		
		LinkedList<ServerAppThread> controlSockets = new LinkedList<ServerAppThread>();
		
		try {
			
			ServerSocket controlSocketListener = new  ServerSocket(12000);
			ServerSocket dataSocketListener = new ServerSocket(13000);
			
			while (true) {
				
				ControlSocket controlSocket = (ControlSocket) controlSocketListener.accept();
				controlSockets.addFirst(new ServerAppThread(controlSocket, dataSocketListener));
				controlSockets.getFirst().start();
			}
			
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
}
