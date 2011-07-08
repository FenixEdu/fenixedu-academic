package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;

public class SpaceAttendances extends SpaceAttendances_Base {

    public SpaceAttendances(String personIstUsername, String responsibleIstUsername, DateTime entranceTime) {
	this.setPersonIstUsername(personIstUsername);
	this.setResponsibleForEntranceIstUsername(responsibleIstUsername);
	this.setEntranceTime(entranceTime);
    }

    public Person getPerson() {
	return Person.readPersonByIstUsername(getPersonIstUsername());
    }

    public void exit(String responsibleUsername) {
	if (hasOccupiedLibraryPlace()) {
	    setResponsibleForExitIstUsername(responsibleUsername);
	    setExitTime(new DateTime());
	    removeOccupiedLibraryPlace();
	}
    }
}
