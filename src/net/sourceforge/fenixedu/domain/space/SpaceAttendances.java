package net.sourceforge.fenixedu.domain.space;

import org.joda.time.DateTime;

public class SpaceAttendances extends SpaceAttendances_Base {
    
    public SpaceAttendances(String personIstUsername, String responsibleIstUsername, DateTime entranceTime) {
	this.setPersonIstUsername(personIstUsername);
	this.setResponsibleForEntranceIstUsername(responsibleIstUsername);
	this.setEntranceTime(entranceTime);
    }
    
    public SpaceAttendances() {
    }

}
