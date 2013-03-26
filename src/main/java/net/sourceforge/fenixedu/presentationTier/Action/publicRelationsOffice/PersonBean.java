package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.io.Serializable;

/**
 * @author jaime
 * 
 */
public class PersonBean implements Serializable {

    private String username;

    public PersonBean() {
        setUsername(null);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
