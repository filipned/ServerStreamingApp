import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

import model.*;

public class ServerAppThread extends Thread{

	private ControlSocket controlSocket;
	private DataSocket dataSocket;
	private ServerSocket dataSocketListener;
	private String request = "/";
	
	private LinkedList<ChallengeListItem> listChallenges = new LinkedList<ChallengeListItem>();
	private LinkedList<ChallengeLiveItem> liveChallenges = new LinkedList<ChallengeLiveItem>();
	
	
	public ServerAppThread(ControlSocket controlSocket, ServerSocket dataSocketListener) {
		this.controlSocket = controlSocket;
		this.dataSocketListener = dataSocketListener;
	}
	
	
	@Override
	public void run() {
	
		try {
			
			request = controlSocket.recieveRequest();
			
			switch (request) {
			case ControlSocket.ADD_CHALLENGE_REQUEST:
				addChallenge();
				break;

			case ControlSocket.ADD_LIVE_CHALLENGE_REQUEST:
				//id
				break;
			
			case ControlSocket.LIST_CHALLENGES_REQUEST:
				sendListChallenges();
				break;
				
			case ControlSocket.LIVE_CHALLENGES_REQUEST:
				
				break;
				
			case ControlSocket.REMOVE_LIVE_CHALLENGE_REQUEST:
				
				break;
				
//				dodati sve potrebene requestove
				
			default:
				break;
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public void addChallenge() throws IOException {
		ChallengeListItem tmpChallengeList;
		controlSocket.sendAnswer("good");
		
		dataSocket = (DataSocket) dataSocketListener.accept();
		
		try {
			tmpChallengeList = dataSocket.recieveChallenge();	
			controlSocket.sendAnswer("good");
			
			listChallenges.add(tmpChallengeList);		
		} catch (ClassNotFoundException e) {
			controlSocket.sendAnswer("bad");
		}
	}
	
	public void sendListChallenges() throws IOException {
		Boolean startSending = false;
		controlSocket.sendAnswer("good");
		
		dataSocket = (DataSocket) dataSocketListener.accept();
		startSending = dataSocket.getSignal();
		if(startSending) {
			dataSocket.sendChallenges(listChallenges);
		}
	}
}
