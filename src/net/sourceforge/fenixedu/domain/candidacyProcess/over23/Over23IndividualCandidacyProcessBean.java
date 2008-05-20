package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;

public class Over23IndividualCandidacyProcessBean implements Serializable {

    private DomainReference<Over23CandidacyProcess> candidacyProcess;

    private ChoosePersonBean choosePersonBean;

    private PersonBean personBean;

    private YearMonthDay candidacyDate;

    private DomainReference<Degree> degreeToAdd;

    private List<DomainReference<Degree>> selectedDegrees;

    private String disabilities;

    private String education;

    private String languages;

    public Over23IndividualCandidacyProcessBean() {
	setCandidacyDate(new YearMonthDay());
	setSelectedDegrees(new ArrayList<Degree>());
    }

    public Over23IndividualCandidacyProcessBean(Over23IndividualCandidacyProcess process) {
	this();
	setCandidacyDate(process.getCandidacyDate());
	addDegrees(process.getSelectedDegreesSortedByOrder());
	setDisabilities(process.getDisabilities());
	setEducation(process.getEducation());
	setLanguages(process.getLanguages());
    }

    public Over23CandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(Over23CandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<Over23CandidacyProcess>(candidacyProcess) : null;
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

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public YearMonthDay getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(final YearMonthDay candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public Degree getDegreeToAdd() {
	return (this.degreeToAdd != null) ? this.degreeToAdd.getObject() : null;
    }

    public void setDegreeToAdd(Degree degreeToAdd) {
	this.degreeToAdd = (degreeToAdd != null) ? new DomainReference<Degree>(degreeToAdd) : null;
    }

    public boolean hasDegreeToAdd() {
	return getDegreeToAdd() != null;
    }

    public void removeDegreeToAdd() {
	degreeToAdd = null;
    }

    public List<Degree> getSelectedDegrees() {
	final List<Degree> result = new ArrayList<Degree>();
	for (final DomainReference<Degree> degree : selectedDegrees) {
	    result.add(degree.getObject());
	}
	return result;
    }

    public void setSelectedDegrees(final List<Degree> degrees) {
	selectedDegrees = new ArrayList<DomainReference<Degree>>();
	for (final Degree degree : degrees) {
	    selectedDegrees.add(new DomainReference<Degree>(degree));
	}
    }

    public void addDegree(final Degree degree) {
	selectedDegrees.add(new DomainReference<Degree>(degree));
    }

    public void addDegrees(final Collection<Degree> degrees) {
	for (final Degree degree : degrees) {
	    addDegree(degree);
	}
    }

    public void removeDegree(final Degree degree) {
	final Iterator<DomainReference<Degree>> iter = selectedDegrees.iterator();
	while (iter.hasNext()) {
	    if (iter.next().getObject() == degree) {
		iter.remove();
		break;
	    }
	}
    }

    public boolean containsDegree(final Degree value) {
	for (final Degree degree : getSelectedDegrees()) {
	    if (degree == value) {
		return true;
	    }
	}
	return false;
    }

    public void removeSelectedDegrees() {
	selectedDegrees.clear();
    }

    public String getDisabilities() {
	return disabilities;
    }

    public void setDisabilities(String disabilities) {
	this.disabilities = disabilities;
    }

    public String getEducation() {
	return education;
    }

    public void setEducation(String education) {
	this.education = education;
    }

    public String getLanguages() {
	return languages;
    }

    public void setLanguages(String languages) {
	this.languages = languages;
    }
    
    public boolean hasChoosenPerson() {
	return getChoosePersonBean().hasPerson();
    }

    public void removeChoosePersonBean() {
	setChoosePersonBean(null);
    }
}
