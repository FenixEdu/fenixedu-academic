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

public class PhdProgramCandidacyProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PersonBean personBean;

    private DomainReference<PhdProgram> program;

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<Degree> degree;

    private String thesisTitle;

    private PhdIndividualProgramCollaborationType collaborationType;

    private String otherCollaborationType;

    private ChoosePersonBean choosePersonBean;

    public static class GuiderBean {

	private String name;

	private String academicQualification;

	private String professionalQualification;

	private String workLocation;

	private String address;

	private String email;

	private String phone;

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getAcademicQualification() {
	    return academicQualification;
	}

	public void setAcademicQualification(String academicQualification) {
	    this.academicQualification = academicQualification;
	}

	public String getWorkLocation() {
	    return workLocation;
	}

	public void setWorkLocation(String workLocation) {
	    this.workLocation = workLocation;
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public String getPhone() {
	    return phone;
	}

	public void setPhone(String phone) {
	    this.phone = phone;
	}

	public String getProfessionalQualification() {
	    return professionalQualification;
	}

	public void setProfessionalQualification(String professionalQualification) {
	    this.professionalQualification = professionalQualification;
	}

    }

    public PhdProgramCandidacyProcessBean() {
	setPersonBean(new PersonBean());
	setChoosePersonBean(new ChoosePersonBean());
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
