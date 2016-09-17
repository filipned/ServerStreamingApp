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
				if(request.equals(null)) {
					return;
				}
				System.out.println("Request is " + request);
				switch (request) {
				case ControlSocket.ADD_CHALLENGE_REQUEST:
					addChallenge();
					break;
	
				case ControlSocket.ADD_LIVE_CHALLENGE_REQUEST:
					addLiveChallenge();
					break;
				
				case ControlSocket.LIST_CHALLENGES_REQUEST:
					sendListChallenges();
					break;
					
				case ControlSocket.LIVE_CHALLENGES_REQUEST:
					sendLiveChallenge();
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
			for (int i = 0; i < ServerApp.listChallenges.size(); i++) {
				if(ServerApp.listChallenges.get(i).getChallengeName().equals(tmpChallengeList.getChallengeName())) {
					controlSocket.sendAnswer("bad");
					return;
				}
			}
			controlSocket.sendAnswer("good");
			System.out.println("duzina liste" + ServerApp.listChallenges.size());
			ServerApp.listChallenges.add(tmpChallengeList);
			System.out.println("duzina liste" + ServerApp.listChallenges.size());
		} catch (ClassNotFoundException e) {
			controlSocket.sendAnswer("bad");
			e.printStackTrace();
		}
	}
	
	public void sendListChallenges() throws IOException {
		Boolean startSending = false;
		controlSocket.sendAnswer("good");
		
        dataSocket = dataSocketListener.accept();
		try {
			startSending = dataSocket.getSignal();
		
			if(startSending) {
				dataSocket.sendChallenges(ServerApp.listChallenges);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public void addLiveChallenge() throws IOException{
	        ChallengeLiveItem tmpChallengeLive;
	        controlSocket.sendAnswer("good");
	       
	        dataSocket = dataSocketListener.accept();
	       
	        try {
	            tmpChallengeLive = dataSocket.recieveLiveChallenge();
	           
	           
	            tmpChallengeLive.setID(ServerApp.userID++);
	            
	            if(tmpChallengeLive instanceof ChallengeLiveItem) {
	            	controlSocket.sendAnswer("good");
	            	ServerApp.liveChallenges.add(tmpChallengeLive);
	            }
	           
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	       
	    }
	   
	    public void sendLiveChallenge() throws IOException{
	        Boolean startSending = false;
	        controlSocket.sendAnswer("good");
	       
	        dataSocket = (DataSocket) dataSocketListener.accept();
	        try {
				startSending = dataSocket.getSignal();
			
		        if(startSending) {
		            dataSocket.sendLiveChallenges(ServerApp.liveChallenges);
		        }
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	    }
	
	
}
