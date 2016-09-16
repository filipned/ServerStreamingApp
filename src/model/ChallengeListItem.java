package model;
/**
 * Created by FILIP on 31-Jul-16.
 */

public class ChallengeListItem extends  Challenge {

    private int numOfSubscribers;

    public ChallengeListItem(String challengeName) {
        super(challengeName);
        this.numOfSubscribers = 0;
    }

//    GET, SET
    public int getNumOfSubscribers() {
        return numOfSubscribers;
    }

    public void setNumOfSubscribers(int numOfSubscribers) {
        this.numOfSubscribers = numOfSubscribers;
    }

//    povecava borj subskrajera za 1
    public void increaseNumOfSubscribers() {
        this.numOfSubscribers++;
    }
}
