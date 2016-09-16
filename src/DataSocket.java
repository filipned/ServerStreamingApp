import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import model.ChallengeListItem;


public class DataSocket extends Socket {

	
	ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    
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
    	ChallengeListItem cli = (ChallengeListItem) objectInputStream.readObject();
    	
    	return cli;
    }
    
    public Boolean getSignal() throws IOException {
    	this.openObjectInputStream();
    	return objectInputStream.readBoolean();
    }
    
    public void sendChallenges(LinkedList<ChallengeListItem> listChallenges) throws IOException {
    	this.openObjectOutputStream();
    	
    	objectOutputStream.writeObject(listChallenges);
    }
}
