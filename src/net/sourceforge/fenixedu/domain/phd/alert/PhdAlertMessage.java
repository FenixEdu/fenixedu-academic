package net.sourceforge.fenixedu.domain.phd.alert;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdAlertMessage extends PhdAlertMessage_Base {

    protected PhdAlertMessage() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public PhdAlertMessage(PhdIndividualProgramProcess process, Person person, MultiLanguageString subject,
	    MultiLanguageString body) {
	this();
	init(process, person, subject, body);
    }

    protected void init(PhdIndividualProgramProcess process, Person person, MultiLanguageString subject, MultiLanguageString body) {
	checkParameters(process, person, subject, body);
	super.setProcess(process);
	super.setPerson(person);
	super.setSubject(subject);
	super.setBody(body);
	super.setTaskPerformed(Boolean.FALSE);
    }

    private void checkParameters(PhdIndividualProgramProcess process, Person person, MultiLanguageString subject,
	    MultiLanguageString body) {
	check(process, "error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.process.cannot.be.null");
	check(person, "error.phd.alert.PhdAlertMessage.person.cannot.be.null");
	check(subject, "error.phd.alert.PhdAlertMessage.subject.cannot.be.null");
	check(body, "error.phd.alert.PhdAlertMessage.body.cannot.be.null");
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.process");
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.person");
    }

    @Override
    public void setSubject(MultiLanguageString subject) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.subject");
    }

    @Override
    public void setBody(MultiLanguageString body) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.body");
    }

    @Service
    @Override
    public void setTaskPerformed(Boolean taskPerformed) {
	check(taskPerformed, "error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.taskPerformed.cannot.be.null");

	super.setTaskPerformed(taskPerformed);
    }

    public boolean isTaskPerformed() {
	return getTaskPerformed().booleanValue();
    }

    @Service
    public void delete() {
	super.setPerson(null);
	super.setProcess(null);
	super.setRootDomainObject(null);
	super.deleteDomainObject();
    }

}
