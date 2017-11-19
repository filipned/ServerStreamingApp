package sockets;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;


public class ServerControlSocket extends ServerSocket {

	public ServerControlSocket(int port) throws IOException {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ControlSocket accept() throws IOException {
		if (isClosed())
		    throw new SocketException("Socket is closed");
		  if (!isBound())
		    throw new SocketException("Socket is not bound yet");
		  ControlSocket controlSocket = new ControlSocket((SocketImpl) null);
		  implAccept(controlSocket);
		  return controlSocket;
		
	}
}
