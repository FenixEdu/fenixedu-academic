package net.sourceforge.fenixedu.presentationTier.Action.manager.renderers;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.fenixedu.domain.person.Gender;

public class SearchBean implements Serializable {
    private int minAge;
    private int maxAge;
    private Date date;
    private Gender gender;

    public int getMinAge() {
	return this.minAge;
    }

    public void setMinAge(int minAge) {
	this.minAge = minAge;
    }

    public int getMaxAge() {
	return this.maxAge;
    }

    public void setMaxAge(int maxAge) {
	this.maxAge = maxAge;
    }

    public Date getDate() {
	return this.date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Gender getGender() {
	return this.gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }
}
