package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilantWrapperBean implements Serializable {

    public DomainReference<Person> person;
    public List<DomainReference<VigilantGroup>> convokableForGroups;
    public List<DomainReference<VigilantGroup>> notConvokableForGroups;

    public VigilantWrapperBean(Person person) {
	setPerson(person);
	setConvokableForGroups((List<VigilantGroup>) Collections.EMPTY_LIST);
	setNotConvokableForGroups((List<VigilantGroup>) Collections.EMPTY_LIST);
    }

    public Person getPerson() {
	return person.getObject();
    }

    public void setPerson(Person person) {
	this.person = new DomainReference<Person>(person);
    }

    public List<VigilantGroup> getConvokableForGroups() {
	List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
	for (DomainReference<VigilantGroup> group : convokableForGroups) {
	    groups.add(group.getObject());
	}
	return groups;
    }

    public void setConvokableForGroups(List<VigilantGroup> convokableForGroups) {
	this.convokableForGroups = new ArrayList<DomainReference<VigilantGroup>>();
	for (VigilantGroup group : convokableForGroups) {
	    this.convokableForGroups.add(new DomainReference<VigilantGroup>(group));
	}

    }

    public List<VigilantGroup> getNotConvokableForGroups() {
	List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
	for (DomainReference<VigilantGroup> group : notConvokableForGroups) {
	    groups.add(group.getObject());
	}
	return groups;
    }

    public void setNotConvokableForGroups(List<VigilantGroup> notConvokableForGroups) {
	this.notConvokableForGroups = new ArrayList<DomainReference<VigilantGroup>>();
	for (VigilantGroup group : notConvokableForGroups) {
	    this.notConvokableForGroups.add(new DomainReference<VigilantGroup>(group));
	}

    }

}
