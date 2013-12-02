package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;

public class SpaceAttendances extends SpaceAttendances_Base {

    public SpaceAttendances(String personIstUsername, String responsibleIstUsername, DateTime entranceTime) {
        this.setPersonIstUsername(personIstUsername);
        this.setResponsibleForEntranceIstUsername(responsibleIstUsername);
        this.setEntranceTime(entranceTime);
    }

    public String getOccupationDesctiption() {
        if (hasOccupiedLibraryPlace() && getOccupiedLibraryPlace() instanceof RoomSubdivision) {
            return getOccupiedLibraryPlace().getSpaceInformation().getPresentationName();
        }
        return "-";
    }

    public Person getPerson() {
        return Person.readPersonByUsername(getPersonIstUsername());
    }

    public void exit(String responsibleUsername) {
        if (hasOccupiedLibraryPlace()) {
            setResponsibleForExitIstUsername(responsibleUsername);
            setExitTime(new DateTime());
            setOccupiedLibraryPlace(null);
        }
    }

    public void delete() {
        setOccupiedLibraryPlace(null);
        setVisitedLibraryPlace(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasPersonIstUsername() {
        return getPersonIstUsername() != null;
    }

    @Deprecated
    public boolean hasResponsibleForEntranceIstUsername() {
        return getResponsibleForEntranceIstUsername() != null;
    }

    @Deprecated
    public boolean hasResponsibleForExitIstUsername() {
        return getResponsibleForExitIstUsername() != null;
    }

    @Deprecated
    public boolean hasVisitedLibraryPlace() {
        return getVisitedLibraryPlace() != null;
    }

    @Deprecated
    public boolean hasOccupiedLibraryPlace() {
        return getOccupiedLibraryPlace() != null;
    }

    @Deprecated
    public boolean hasEntranceTime() {
        return getEntranceTime() != null;
    }

    @Deprecated
    public boolean hasExitTime() {
        return getExitTime() != null;
    }

}
