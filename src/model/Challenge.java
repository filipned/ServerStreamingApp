package model;


import java.io.Serializable;

/**
 * Created by FILIP on 31-Jul-16.
 */

public abstract class Challenge implements Serializable {

    private String challengeName;



    public Challenge(String challengeName) {
        this.challengeName = challengeName;

    }

//    GET, SET

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }
}
