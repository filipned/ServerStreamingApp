import java.util.LinkedList;

public class Buffer {

	
	private int id;
	private LinkedList<byte[]> videoContent;
	
	
	public Buffer(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LinkedList<byte[]> getVideoContent() {
		return videoContent;
	}
	public void setVideoContent(LinkedList<byte[]> videoContent) {
		this.videoContent = videoContent;
	}
	
	
}
