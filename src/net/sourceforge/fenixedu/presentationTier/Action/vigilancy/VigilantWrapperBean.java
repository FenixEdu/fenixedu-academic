package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilantWrapperBean implements Serializable {

	public Person person;
	public List<VigilantGroup> convokableForGroups;
	public List<VigilantGroup> notConvokableForGroups;

	public VigilantWrapperBean(Person person) {
		setPerson(person);
		setConvokableForGroups(Collections.EMPTY_LIST);
		setNotConvokableForGroups(Collections.EMPTY_LIST);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<VigilantGroup> getConvokableForGroups() {
		List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
		for (VigilantGroup group : convokableForGroups) {
			groups.add(group);
		}
		return groups;
	}

	public void setConvokableForGroups(List<VigilantGroup> convokableForGroups) {
		this.convokableForGroups = new ArrayList<VigilantGroup>();
		for (VigilantGroup group : convokableForGroups) {
			this.convokableForGroups.add(group);
		}

	}

	public List<VigilantGroup> getNotConvokableForGroups() {
		List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
		for (VigilantGroup group : notConvokableForGroups) {
			groups.add(group);
		}
		return groups;
	}

	public void setNotConvokableForGroups(List<VigilantGroup> notConvokableForGroups) {
		this.notConvokableForGroups = new ArrayList<VigilantGroup>();
		for (VigilantGroup group : notConvokableForGroups) {
			this.notConvokableForGroups.add(group);
		}

	}

}
