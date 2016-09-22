import java.io.IOException;
import java.net.DatagramSocket;
import java.util.LinkedList;

import model.ChallengeListItem;
import model.ChallengeLiveItem;


public class ServerApp {

	public static LinkedList<ChallengeListItem> listChallenges = new LinkedList<ChallengeListItem>();
	public static LinkedList<ChallengeLiveItem> liveChallenges = new LinkedList<ChallengeLiveItem>();
	public static int userID = 0;

	
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
