package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

public class StrategySugestion {

	private List<VigilantWrapper> vigilantsThatAreTeachers;

	private List<VigilantWrapper> sugestedVigilants;

	private List<UnavailableInformation> unavailableVigilants;

	public StrategySugestion(List<VigilantWrapper> teachers, List<VigilantWrapper> sugestion,
			List<UnavailableInformation> unvailables) {
		this.vigilantsThatAreTeachers = teachers;
		this.sugestedVigilants = sugestion;
		this.unavailableVigilants = unvailables;

	}

	public List<VigilantWrapper> getVigilantSugestion() {
		return sugestedVigilants;
	}

	public List<UnavailableInformation> getUnavailableVigilantsWithInformation() {
		return unavailableVigilants;
	}

	public List<VigilantWrapper> getUnavailableVigilants() {
		List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
		for (UnavailableInformation information : unavailableVigilants) {
			vigilants.add(information.getVigilant());
		}
		return vigilants;
	}

	public List<VigilantWrapper> getVigilantsThatAreTeachersSugestion() {
		return vigilantsThatAreTeachers;
	}

}
