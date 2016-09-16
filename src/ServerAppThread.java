import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import model.*;




public class ServerAppThread extends Thread{

	private ControlSocket controlSocket;
	private DataSocket dataSocket;
	private ServerDataSocket dataSocketListener;
	private String request = "/";
	
	private LinkedList<ChallengeListItem> listChallenges = new LinkedList<ChallengeListItem>();
	private LinkedList<ChallengeLiveItem> liveChallenges = new LinkedList<ChallengeLiveItem>();
	
	
	public ServerAppThread(ControlSocket controlSocket, ServerDataSocket dataSocketListener) {
		this.controlSocket = controlSocket;		
		this.dataSocketListener = dataSocketListener;
	}
	
	
	@Override
	public void run() {
		while(true) {
			try {
				System.out.println("waiting for request");
				request = controlSocket.recieveRequest();
				System.out.println("Request is " + request);
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
			} catch (NullPointerException e) {
				// TODO: handle exception
				
			}
		}
		
	}
	
	public void addChallenge() throws IOException {
		ChallengeListItem tmpChallengeList;
		controlSocket.sendAnswer("good");
		
		System.out.println("Establishing data stream...");
		dataSocket = dataSocketListener.accept();
		System.out.println("Data stream established");
		try {
			tmpChallengeList = dataSocket.recieveChallenge();
			if(tmpChallengeList.equals(null)) System.out.println("Null objekat");
			System.out.println("list challenge recieved");
			System.out.println(tmpChallengeList.toString());
			controlSocket.sendAnswer("good");
			System.out.println("duzina liste" + listChallenges.size());
			listChallenges.add(tmpChallengeList);
			System.out.println("duzina liste" + listChallenges.size());
		} catch (ClassNotFoundException e) {
			controlSocket.sendAnswer("bad");
			e.printStackTrace();
		}
	}
	
	public void sendListChallenges() throws IOException {
		Boolean startSending = false;
		controlSocket.sendAnswer("good");
		
		dataSocket = dataSocketListener.accept();
		startSending = dataSocket.getSignal();
		if(startSending) {
			dataSocket.sendChallenges(listChallenges);
		}
	}
}
