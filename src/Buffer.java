import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

public class Buffer {

	
	private int id;
	private LinkedList<byte[]> videoContent;
	private DatagramSocket udpSocket; 
	
	public Buffer(int id) throws SocketException {
		videoContent = new LinkedList<byte[]>();
		this.id = id;
		udpSocket = new DatagramSocket();
		
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public synchronized LinkedList<byte[]> getVideoContent() {
		return (videoContent);
	}
	public  synchronized void setVideoContent(LinkedList<byte[]> videoContent) {
		this.videoContent = videoContent;
	}
	
	
}
