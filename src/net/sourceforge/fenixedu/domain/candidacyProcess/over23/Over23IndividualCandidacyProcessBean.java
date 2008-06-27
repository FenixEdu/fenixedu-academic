package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;

import org.joda.time.LocalDate;

public class Over23IndividualCandidacyProcessBean extends IndividualCandidacyProcessBean {

    private DomainReference<Degree> degreeToAdd;

    private List<DomainReference<Degree>> selectedDegrees;

    private String disabilities;

    private String education;

    private String languages;

    public Over23IndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
	setSelectedDegrees(Collections.EMPTY_LIST);
    }

    public Over23IndividualCandidacyProcessBean(Over23IndividualCandidacyProcess process) {
	setCandidacyDate(process.getCandidacyDate());
	setSelectedDegrees(Collections.EMPTY_LIST);
	addDegrees(process.getSelectedDegreesSortedByOrder());
	setDisabilities(process.getDisabilities());
	setEducation(process.getEducation());
	setLanguages(process.getLanguages());
    }

    @Override
    public Over23CandidacyProcess getCandidacyProcess() {
	return (Over23CandidacyProcess) super.getCandidacyProcess();
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
}
