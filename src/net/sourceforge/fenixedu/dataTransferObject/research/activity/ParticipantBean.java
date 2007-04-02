package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ParticipationsInterface;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public abstract class ParticipantBean implements Serializable {

    private DomainReference<Person> person;

    private DomainReference<Unit> unit;

    private ResearchActivityParticipationRole role;

    private String personName;

    private String unitName;

    private ParticipationType participationType;

    public ParticipantBean() {
	setPerson(null);
	setUnit(null);
    }

    public ResearchActivityParticipationRole getRole() {
	return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
	this.role = role;
    }

    public Person getPerson() {
	return this.person.getObject();
    }

    public void setPerson(Person person) {
	this.person = new DomainReference<Person>(person);
    }

    public String getPersonName() {
	return personName;
    }

    public void setPersonName(String name) {
	this.personName = name;
    }

    public void setUnitName(String name) {
	this.unitName = name;
    }

    public String getUnitName() {
	return unitName;
    }

    public Unit getUnit() {
	return this.unit.getObject();
    }

    public void setUnit(Unit organization) {
	this.unit = new DomainReference<Unit>(organization);
    }

    public ExternalContract getExternalPerson() {
	if (!person.isNullReference())
	    return person.getObject().getExternalPerson();
	else
	    return new DomainReference<ExternalContract>(null).getObject();
    }

    public void setExternalPerson(ExternalContract externalPerson) {
	if (externalPerson == null) {
	    this.person = new DomainReference<Person>(null);
	} else {
	    setPerson(externalPerson.getPerson());
	}
    }

    public ParticipationType getParticipationType() {
	return participationType;
    }

    public void setParticipationType(ParticipationType participationType) {
	this.participationType = participationType;
    }

    public boolean isExternalParticipation() {
	return this.participationType.equals(ParticipationType.EXTERNAL_PARTICIPANT);
    }

    public static enum ParticipationType {
	INTERNAL_PARTICIPANT("Internal"), EXTERNAL_PARTICIPANT("External");

	private String type;

	private ParticipationType(String type) {
	    this.type = type;
	}

	public String getType() {
	    return type;
	}
    }
    
    public abstract List<ResearchActivityParticipationRole> getAllowedRoles();
    public abstract DomainObject getActivity();

    public static ParticipantBean getParticipantBean(ParticipationsInterface objectWithParticipations) {
	
	ParticipantBean bean = null;
	if (objectWithParticipations instanceof Event) {
	    bean = new EventParticipantBean();
	    ((EventParticipantBean) bean).setEvent((Event) objectWithParticipations);
	}
	if (objectWithParticipations instanceof EventEdition) {
	    bean = new EventEditionParticipantBean();
	    ((EventEditionParticipantBean) bean).setEventEdition((EventEdition) objectWithParticipations);
	}
	if (objectWithParticipations instanceof ScientificJournal) {
	    bean = new ScientificJournalParticipantBean();
	    ((ScientificJournalParticipantBean) bean)
		    .setScientificJournal((ScientificJournal) objectWithParticipations);
	}
	if (objectWithParticipations instanceof JournalIssue) {
	    bean = new JournalIssueParticipantBean();
	    ((JournalIssueParticipantBean) bean).setJournalIssue((JournalIssue)objectWithParticipations);
	}
	if (objectWithParticipations instanceof Cooperation) {
	    bean = new CooperationParticipantBean();
	    ((CooperationParticipantBean) bean).setCooperation((Cooperation) objectWithParticipations);
	}
	return bean;
    }
}
