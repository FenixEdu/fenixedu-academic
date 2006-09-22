package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class StrategySugestion {

	private List<Vigilant> vigilantsThatAreTeachers;
	
    private List<Vigilant> sugestedVigilants;

    private List<UnavailableInformation> unavailableVigilants;

    public StrategySugestion(List<Vigilant> teachers, List<Vigilant> sugestion,
            List<UnavailableInformation> unvailables) {
        this.vigilantsThatAreTeachers = teachers;
    	this.sugestedVigilants = sugestion;
        this.unavailableVigilants = unvailables;
        
    }

    public List<Vigilant> getVigilantSugestion() {
        return sugestedVigilants;
    }

    public List<UnavailableInformation> getUnavailableVigilantsWithInformation() {
        return unavailableVigilants;
    }

    public List<Vigilant> getUnavailableVigilants() {
    	List<Vigilant> vigilants = new ArrayList<Vigilant>();
    	for(UnavailableInformation information : unavailableVigilants) {
    		vigilants.add(information.getVigilant());
    	}
    	return vigilants;
    }

	public List<Vigilant> getVigilantsThatAreTeachersSugestion() {
		return vigilantsThatAreTeachers;
	}

    
}
