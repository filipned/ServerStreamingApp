package sockets;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.LinkedList;

import model.*;


public class DataSocket extends Socket {

	
	ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    FileInputStream fileInputStream;
    
    public DataSocket(SocketImpl socketImpl) throws SocketException {
    	
    	super((SocketImpl) null);
    }

	public ObjectOutputStream openObjectOutputStream() throws IOException {
        objectOutputStream = new ObjectOutputStream(this.getOutputStream());
        return objectOutputStream;
    }

    public ObjectInputStream openObjectInputStream() throws IOException {
        objectInputStream = new ObjectInputStream(this.getInputStream());
        return objectInputStream;
    }

    public void closeObjectOutputStream() throws IOException {
        objectOutputStream.close();
    }

    public void closeObjectInputStream() throws IOException {
        objectInputStream.close();
    }
    
    public ChallengeListItem recieveChallenge() throws IOException, ClassNotFoundException {
    	this.openObjectInputStream();
    	ChallengeListItem cli = null;
    	Object o = objectInputStream.readObject();
		if(o instanceof ChallengeListItem) 
    		cli = (ChallengeListItem) o;
    	
    	return cli;
    }
    
    public ChallengeLiveItem recieveLiveChallenge() throws IOException, ClassNotFoundException {
        this.openObjectInputStream();
        ChallengeLiveItem cli = null;
    	Object o = objectInputStream.readObject();
		if(o instanceof ChallengeLiveItem) 
    		cli = (ChallengeLiveItem) o;
    	
    	return cli;
    }
    
    public Boolean getSignal() throws IOException, ClassNotFoundException {
    	this.openObjectInputStream();
    	Boolean b = false;
    	Object o  = objectInputStream.readObject();
    	if(o instanceof Boolean) {
    		b = (Boolean) o;
    	}
    	return b;
    }
    
    public void sendChallenges(LinkedList<ChallengeListItem> listChallenges) throws IOException {
    	this.openObjectOutputStream();
    	
    	objectOutputStream.writeObject(listChallenges);
    }
    
    public void sendLiveChallenges(LinkedList<ChallengeLiveItem> liveChallenges) throws IOException {
        this.openObjectOutputStream();
       
        objectOutputStream.writeObject(liveChallenges);
       
    }
   
}
