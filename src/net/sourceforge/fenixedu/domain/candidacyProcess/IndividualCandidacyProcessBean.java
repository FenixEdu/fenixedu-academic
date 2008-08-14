package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.LocalDate;

abstract public class IndividualCandidacyProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2860833709120576930L;

    private DomainReference<CandidacyProcess> candidacyProcess;

    private ChoosePersonBean choosePersonBean;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    public CandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(CandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<CandidacyProcess>(candidacyProcess) : null;
    }

    public boolean hasCandidacyProcess() {
	return getCandidacyProcess() != null;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

    public boolean hasChoosenPerson() {
	return getChoosePersonBean().hasPerson();
    }

    public void removeChoosePersonBean() {
	setChoosePersonBean(null);
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public LocalDate getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(final LocalDate candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public Person getOrCreatePersonFromBean() {
	return getPersonBean().hasPerson() ? getPersonBean().getPerson().edit(getPersonBean()) : new Person(getPersonBean());
    }

    protected ExecutionInterval getCandidacyExecutionInterval() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }
}
