import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketImpl;


public class ServerDataSocket extends ServerSocket{

	public ServerDataSocket(int port) throws IOException {
		super(port);
	}


	@Override
	public DataSocket accept() throws IOException {
		if (isClosed())
		    throw new SocketException("Socket is closed");
		  if (!isBound())
		    throw new SocketException("Socket is not bound yet");
		  DataSocket dataSocket = new DataSocket((SocketImpl) null);
		  implAccept(dataSocket);
		  return dataSocket;
		
	}
	
}
