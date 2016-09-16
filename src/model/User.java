package model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by FILIP on 31-Jul-16.
 */

public class User implements Serializable {

    private String firstName;
    private String lastName;
//    private ImageView asterix;
    private LinkedList<Challenge> subscribedChallenges;
    private LinkedList<Challenge> acomplishedChallenges;


    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
//        this.asterix = asterix;
        subscribedChallenges = new LinkedList<Challenge>();
        acomplishedChallenges = new LinkedList<Challenge>();
    }

//    GET,SET
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public ImageView getAsterix() {
//        return asterix;
//    }
//
//    public void setAsterix(ImageView asterix) {
//        this.asterix = asterix;
//    }

    public LinkedList<Challenge> getSubscribedChallenges() {
        return subscribedChallenges;
    }

    public void setSubscribedChallenges(LinkedList<Challenge> subscribedChallenges) {
        this.subscribedChallenges = subscribedChallenges;
    }

    public LinkedList<Challenge> getAcomplishedChallenges() {
        return subscribedChallenges;
    }

    public void setAcomplishedChallenges(LinkedList<Challenge> subscribedChallenges) {
        this.subscribedChallenges = subscribedChallenges;
    }

//    Metoda dodaje izazov u listu subskrajbovanih izazova
    public void addSubscription(Challenge challenge) {
        subscribedChallenges.add(challenge);
    }

//    metoda dodaje izazov u listu odradjenih izazova
    public void addAcomplishedChallenge(Challenge challenge) {
        acomplishedChallenges.add(challenge);
    }

}
