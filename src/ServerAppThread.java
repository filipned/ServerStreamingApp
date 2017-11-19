import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.Delayed;

import sockets.ControlSocket;
import sockets.DataSocket;
import sockets.ServerDataSocket;
import model.*;

public class ServerAppThread extends Thread {


	private ControlSocket controlSocket;
	private DataSocket dataSocket;
	private ServerDataSocket dataSocketListener;
	private String request = "/";
	private int threadID;
	private Buffer buffer;
	private byte[] content;
	private int bytesRead;
	private InetAddress group;
	private DatagramSocket datagramSocket;

	private ChallengeListItem tmpChallengeList;
	private ByteArrayOutputStream baos;
	private BufferedInputStream in;
	private ChallengeLiveItem tmpChallengeLive;

	public ServerAppThread(ControlSocket controlSocket,
			ServerDataSocket dataSocketListener) throws UnknownHostException {

		this.controlSocket = controlSocket;
		this.dataSocketListener = dataSocketListener;
		group = InetAddress.getByName("192.168.43.1");

	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("waiting for request");
				request = controlSocket.recieveRequest();

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
					sendLiveChallenges();
					break;

				case ControlSocket.REMOVE_LIVE_CHALLENGE_REQUEST:
					ServerApp.liveChallenges.remove(tmpChallengeLive);
					break;

				case ControlSocket.START_STREAMING:
					recieveStream();
					break;

				default:
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO: handle exception
				break;
			}
		}

	}

	public void addChallenge() throws IOException {

		controlSocket.sendAnswer("good");

		System.out.println("Establishing data stream...");
		dataSocket = dataSocketListener.accept();
		System.out.println("Data stream established");
		try {
			tmpChallengeList = dataSocket.recieveChallenge();
			if (tmpChallengeList.equals(null))
				System.out.println("Null objekat");
			System.out.println("list challenge recieved");
			System.out.println(tmpChallengeList.toString());
			for (int i = 0; i < ServerApp.listChallenges.size(); i++) {

				if (ServerApp.listChallenges.get(i).getChallengeName()
						.equals(tmpChallengeList.getChallengeName())) {

					controlSocket.sendAnswer("bad");
					return;
				}
			}
			controlSocket.sendAnswer("good");
			System.out
					.println("duzina liste" + ServerApp.listChallenges.size());
			ServerApp.listChallenges.add(tmpChallengeList);
			System.out
					.println("duzina liste" + ServerApp.listChallenges.size());
			dataSocket.closeObjectInputStream();
			dataSocket.close();
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

			if (startSending) {
				dataSocket.sendChallenges(ServerApp.listChallenges);
			}

			dataSocket.closeObjectInputStream();
			dataSocket.closeObjectOutputStream();
			dataSocket.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addLiveChallenge() throws IOException {

		controlSocket.sendAnswer("good");

		dataSocket = dataSocketListener.accept();

		try {
			tmpChallengeLive = dataSocket.recieveLiveChallenge();

			threadID = ServerApp.userID;
			buffer = new Buffer(threadID);

			datagramSocket = new DatagramSocket(threadID + 4400);

			tmpChallengeLive.setID(ServerApp.userID++);

			if (tmpChallengeLive instanceof ChallengeLiveItem) {
				controlSocket.sendAnswer("good");
				ServerApp.liveChallenges.add(tmpChallengeLive);
			}
			dataSocket.closeObjectInputStream();
			dataSocket.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendLiveChallenges() throws IOException {
		Boolean startSending = false;
		controlSocket.sendAnswer("good");

		dataSocket = (DataSocket) dataSocketListener.accept();
		try {
			startSending = dataSocket.getSignal();

			if (startSending) {
				dataSocket.sendLiveChallenges(ServerApp.liveChallenges);
			}
			dataSocket.closeObjectInputStream();
			dataSocket.closeObjectOutputStream();
			dataSocket.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void recieveStream() throws IOException {
			
		try {
				
			controlSocket.sendAnswer("good");

			dataSocket = dataSocketListener.accept();

			in = new BufferedInputStream(new DataInputStream(
					dataSocket.getInputStream()));

			baos = new ByteArrayOutputStream();
			
			while (in.read() != -1) {
				dataSocket.setSoTimeout(5000);

				content = new byte[4096];
				bytesRead = -1;

				while ((bytesRead = in.read(content)) != -1) {

					baos.write(content, 0, bytesRead);
					System.out.println("normalno slanje" +content.length);
					buffer.getVideoContent().add(content);
					if (buffer.getVideoContent().size() > 3000) {
						 DatagramPacket packet = 
								 new DatagramPacket(buffer.getVideoContent().get(0), content.length, group, threadID + 4400);
						 
						 datagramSocket.send(packet);
						 buffer.getVideoContent().removeFirst();
					}
				}

			}
			in.close();
			baos.close();
			dataSocket.close();
			datagramSocket.close();

		} catch (SocketTimeoutException e) {
			// TODO: handle exception
			in.close();
			baos.close();
			dataSocket.close();
			datagramSocket.close();
			return;
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}
}

//listen = true;
//
//new Thread(new Runnable() {
//	
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//			
//			while(listen) {
//				
//					try {
//						
//						if(in.read() == -1 && buffer.getVideoContent().size() > 0) {
//							 System.out.println("Slanje baferovanih podataka");
//							 DatagramPacket packet = 
//									 new DatagramPacket(buffer.getVideoContent().get(0), 4096, group, threadID + 4400);
//							 
//							 datagramSocket.send(packet);
//							 buffer.getVideoContent().removeFirst();
//							 
//							 System.out.println(buffer.getVideoContent().get(0).length);
//							 System.out.println(buffer.getVideoContent().size());
//							 System.out.println("Velicina bafera " + buffer.getVideoContent().size());
//
//						} 
//						if(buffer.getVideoContent().size() == 0) {
//							System.out.println("Bafer ispraznjen");
//						} 
//						
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						break;
//					}	
//				
//			}
//	}
//}).start();
