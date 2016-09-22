import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import model.*;

public class ServerAppThread extends Thread {

	private ControlSocket controlSocket;
	private DataSocket dataSocket;
	private ServerDataSocket dataSocketListener;
	private String request = "/";
	private int threadID;
	private Buffer buffer;

	public ServerAppThread(ControlSocket controlSocket, ServerDataSocket dataSocketListener) {
		this.controlSocket = controlSocket;
		this.dataSocketListener = dataSocketListener;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("waiting for request");
				request = controlSocket.recieveRequest();
				if (request.equals(null)) {
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
					sendLiveChallenges();
					break;

				case ControlSocket.REMOVE_LIVE_CHALLENGE_REQUEST:

					break;

				case ControlSocket.START_STREAMING:
					recieveStream();
					break;

				case ControlSocket.WATCH_CHALLENGE_REQUEST:
					// sendStream(id);
					break;

				// dodati sve potrebene requestove
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
			if (tmpChallengeList.equals(null))
				System.out.println("Null objekat");
			System.out.println("list challenge recieved");
			System.out.println(tmpChallengeList.toString());
			for (int i = 0; i < ServerApp.listChallenges.size(); i++) {
				if (ServerApp.listChallenges.get(i).getChallengeName().equals(tmpChallengeList.getChallengeName())) {
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

			if (startSending) {
				dataSocket.sendChallenges(ServerApp.listChallenges);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addLiveChallenge() throws IOException {
		ChallengeLiveItem tmpChallengeLive;
		controlSocket.sendAnswer("good");

		dataSocket = dataSocketListener.accept();

		try {
			tmpChallengeLive = dataSocket.recieveLiveChallenge();

			int id = ServerApp.userID;
			buffer = new Buffer(id);
			ServerApp.bufferList.add(buffer);
			threadID = id;
			tmpChallengeLive.setID(ServerApp.userID++);

			if (tmpChallengeLive instanceof ChallengeLiveItem) {
				controlSocket.sendAnswer("good");
				ServerApp.liveChallenges.add(tmpChallengeLive);
			}

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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void recieveStream() throws IOException {
		controlSocket.sendAnswer("good");

		System.out.println("Establishing data stream...");
		dataSocket = dataSocketListener.accept();
		System.out.println("Data stream established");

		InputStream in = dataSocket.getInputStream();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		while (true) {
			byte[] content = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = in.read(content)) != -1) {
				baos.write(content, 0, bytesRead);
				buffer.getVideoContent().add(content);

				if (buffer.getVideoContent().size() > 3000)
					buffer.getVideoContent().removeFirst();
			}

		}

	}

	public void sendChallenge(int id) throws IOException {

		
		System.out.println("Establishing data stream...");
		dataSocket = (DataSocket) dataSocketListener.accept();

		for (int i = 0; i < ServerApp.bufferList.size(); i++) {
			Buffer buff = ServerApp.bufferList.get(i);

			if (id == buff.getId()) {
				controlSocket.sendAnswer("good");
				System.out.println("Establishing data stream...");
				dataSocket = (DataSocket) dataSocketListener.accept();

				LinkedList<byte[]> bytes = buff.getVideoContent();
				for (int j = 0; j < bytes.size(); j++) {
					OutputStream out = dataSocket.getOutputStream();
					out.write(bytes.get(j));
				}
			}

		}
		controlSocket.sendAnswer("bad");

	}
	

}
