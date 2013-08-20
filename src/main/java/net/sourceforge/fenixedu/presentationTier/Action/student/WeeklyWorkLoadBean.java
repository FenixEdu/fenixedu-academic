package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.io.Serializable;

public class WeeklyWorkLoadBean implements Serializable {

    String attendsID;

    Integer contact;
    Integer autonomousStudy;
    Integer other;

    public Integer getAutonomousStudy() {
        return autonomousStudy;
    }

    public void setAutonomousStudy(Integer autonomousStudy) {
        this.autonomousStudy = autonomousStudy;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public String getAttendsID() {
        return attendsID;
    }

    public void setAttendsID(String attendsID) {
        this.attendsID = attendsID;
    }
}
