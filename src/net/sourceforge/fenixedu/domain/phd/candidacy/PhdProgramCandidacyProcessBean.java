package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    private DomainReference<PhdProgram> program;

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<Degree> degree;

    private String thesisTitle;

    private PhdIndividualProgramCollaborationType collaborationType;

    private String otherCollaborationType;

    private ChoosePersonBean choosePersonBean;

    public PhdProgramCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
	setPersonBean(new PersonBean());
	setChoosePersonBean(new ChoosePersonBean());
    }

    public LocalDate getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(LocalDate candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public PhdProgram getProgram() {
	return (this.program != null) ? this.program.getObject() : null;
    }

    public void setProgram(PhdProgram program) {
	this.program = (program != null) ? new DomainReference<PhdProgram>(program) : null;
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public Person getOrCreatePersonFromBean() {
	if (!getPersonBean().hasPerson()) {
	    Person person = new Person(getPersonBean());
	    getPersonBean().setPerson(person);
	    return person;
	}

	return getPersonBean().getPerson().edit(personBean);
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public boolean hasDegree() {
	return getDegree() != null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public ExecutionDegree getExecutionDegree() {
	return hasDegree() ? null : getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
		getExecutionYear().getAcademicInterval());
    }

    public String getThesisTitle() {
	return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
	this.thesisTitle = thesisTitle;
    }

    public PhdIndividualProgramCollaborationType getCollaborationType() {
	return collaborationType;
    }

    public void setCollaborationType(PhdIndividualProgramCollaborationType collaborationType) {
	this.collaborationType = collaborationType;
    }

    public String getOtherCollaborationType() {
	return otherCollaborationType;
    }

    public void setOtherCollaborationType(String otherCollaborationType) {
	this.otherCollaborationType = otherCollaborationType;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

}
