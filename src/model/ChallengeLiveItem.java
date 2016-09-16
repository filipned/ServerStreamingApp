package model;
/**
 * Created by FILIP on 31-Jul-16.
 */

public class ChallengeLiveItem extends Challenge {

    private User user;
    private int numOfViewers;
    private int ID;

    public ChallengeLiveItem(User user, String challengeName) {
        super(challengeName);
        this.user = user;
        this.numOfViewers = 0;
    }

//    GET, SET
    public int getID() { return ID; }

//    setID podesavati na serveru kako bi se mogla obezzbijediti unikatnost, id ovog objekta treba biti postavljen i za
//    id buffera-a u koji se salju video podaci

    public void setID(int ID) { this.ID = ID; }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumOfViewers() {
        return numOfViewers;
    }

    public void setNumOfViewers(int numOfViewers) {
        this.numOfViewers = numOfViewers;
    }

//    Povecava broj gledalaca za 1
    public void increaseNumOfViewers() {
        this.numOfViewers++;
    }




}
