package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class StrategySugestion {

    private List<Vigilant> sugestedVigilants;

    private List<Vigilant> unavailableVigilants;

    private List<Vigilant> associatedTeachers;

    public StrategySugestion(List<Vigilant> associatedTeachers, List<Vigilant> sugestion,
            List<Vigilant> unvailables) {
        this.sugestedVigilants = sugestion;
        this.unavailableVigilants = unvailables;
        this.associatedTeachers = associatedTeachers;
    }

    public List<Vigilant> getVigilantSugestion() {
        return sugestedVigilants;
    }

    public List<Vigilant> getUnavailableVigilants() {
        return unavailableVigilants;
    }

    public List<Vigilant> getAssociatedTeachers() {
        return associatedTeachers;
    }
}
