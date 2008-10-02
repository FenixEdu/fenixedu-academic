package net.sourceforge.fenixedu.dataTransferObject.photographs;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Pedro Santos (pmrsa)
 */
public class PhotographFilterBean implements Serializable {
    private static final long serialVersionUID = -6023622162590978369L;

    private PhotoState state;

    private PhotoType type;

    private RoleType personType;

    private String name;

    public PhotoState getState() {
	return state;
    }

    public void setState(PhotoState state) {
	this.state = state;
    }

    public PhotoType getType() {
	return type;
    }

    public void setType(PhotoType type) {
	this.type = type;
    }

    public RoleType getPersonType() {
	return personType;
    }

    public void setPersonType(RoleType personType) {
	this.personType = personType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public boolean accepts(Photograph photograph) {
	if (getState() != null && photograph.getState() != getState())
	    return false;
	if (getType() != null && photograph.getPhotoType() != getType())
	    return false;
	Person person = photograph.getPerson();
	if (person != null) {
	    if (getPersonType() != null && !person.hasRole(getPersonType()))
		return false;
	    if (getName() != null && !person.getPersonName().match(name.toLowerCase().split(" ")))
		return false;
	} else {
	    return false;
	}
	return true;
    }

}
