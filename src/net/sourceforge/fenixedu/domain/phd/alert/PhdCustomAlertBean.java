package net.sourceforge.fenixedu.domain.phd.alert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeAdministrativeOfficeGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.LocalDate;

public class PhdCustomAlertBean implements Serializable {

    static enum PhdAlertTargetGroupType {

	MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PERSONS,

	ONLY_FOR_ME;

    }

    /**
     * 
     */
    private static final long serialVersionUID = -3274906509546432695L;

    private PhdAlertTargetGroupType targetGroupType;

    private List<DomainReference<Person>> targetPersons;

    private String subject;

    private String body;

    private boolean toSendEmail;

    private LocalDate fireDate;

    private DomainReference<Person> personToAdd;

    public PhdCustomAlertBean() {
    }

    public PhdAlertTargetGroupType getTargetGroupType() {
	return targetGroupType;
    }

    public void setTargetGroupType(PhdAlertTargetGroupType targetGroupType) {
	this.targetGroupType = targetGroupType;
    }

    public void setTargetPersons(List<Person> targetPersons) {
	final List<DomainReference<Person>> result = new ArrayList<DomainReference<Person>>();
	for (final Person each : targetPersons) {
	    result.add(new DomainReference<Person>(each));
	}

	this.targetPersons = result;
    }

    public List<Person> getTargetPersons() {
	final List<Person> result = new ArrayList<Person>();
	for (final DomainReference<Person> each : this.targetPersons) {
	    result.add(each.getObject());
	}

	return result;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public boolean isToSendEmail() {
	return toSendEmail;
    }

    public void setToSendEmail(boolean toSendEmail) {
	this.toSendEmail = toSendEmail;
    }

    public LocalDate getFireDate() {
	return fireDate;
    }

    public void setFireDate(LocalDate fireDate) {
	this.fireDate = fireDate;
    }

    public Group getTargetGroup() {
	switch (getTargetGroupType()) {

	case MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PERSONS:
	    return new MasterDegreeAdministrativeOfficeGroup();
	case ONLY_FOR_ME:
	    return new PersonGroup(AccessControl.getPerson());

	default:
	    throw new RuntimeException("Target group type not supported");
	}
    }

    public Person getPersonToAdd() {
	return (this.personToAdd != null) ? this.personToAdd.getObject() : null;
    }

    public void setPersonToAdd(Person personToAdd) {
	this.personToAdd = (personToAdd != null) ? new DomainReference<Person>(personToAdd) : null;
    }

}
