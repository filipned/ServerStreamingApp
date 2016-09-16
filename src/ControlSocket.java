import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ControlSocket extends Socket {

	public final static String ADD_CHALLENGE_REQUEST = "add_challenge_request";
	public final static String LIST_CHALLENGES_REQUEST = "list_challenges_request";
	public final static String ADD_LIVE_CHALLENGE_REQUEST = "add_live_challenge_request";
	public final static String LIVE_CHALLENGES_REQUEST = "live_challenges_request";
	public final static String REMOVE_LIVE_CHALLENGE_REQUEST = "remove_live_challenge";
	public final static String WATCH_CHALLENGE_REQUEST = "watch_challenge";

	private PrintStream outputStream;
	private BufferedReader inputStream;

	public PrintStream openOutputStream() throws IOException {
		outputStream = new PrintStream(this.getOutputStream());
		return outputStream;
	}

	public BufferedReader openInputStream() throws IOException {
		inputStream = new BufferedReader(new InputStreamReader(this.getInputStream()));
		return inputStream;
	}

	public void closeOutputStream() throws IOException {
		outputStream.close();
	}

	public void closeInputStream() throws IOException {
		inputStream.close();
	}
	
	public void sendAnswer(String request) throws IOException {
        this.openOutputStream();
        outputStream.println(request);
    }

    public String recieveRequest() throws IOException {
    	this.openInputStream();
        return inputStream.readLine();
    }
}
