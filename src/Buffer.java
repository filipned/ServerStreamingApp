import java.util.Collections;
import java.util.LinkedList;

public class Buffer {

	
	private int id;
	private LinkedList<byte[]> videoContent;
	
	
	public Buffer(int id) {
		videoContent = new LinkedList<byte[]>();
		this.id = id;
		videoContent =  (LinkedList<byte[]>) Collections.synchronizedList(new LinkedList<byte[]>());
		
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
