package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class RegistrationConclusionBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private DomainReference<Registration> registration;

    private DomainReference<CycleCurriculumGroup> cycleCurriculumGroup;

    public RegistrationConclusionBean(Registration registration) {
	this(registration, null);
    }

    public RegistrationConclusionBean(Registration registration, CycleCurriculumGroup cycleCurriculumGroup) {
	setRegistration(registration);
	setCycleCurriculumGroup(cycleCurriculumGroup);
    }

    public CycleCurriculumGroup getCycleCurriculumGroup() {
	return (this.cycleCurriculumGroup != null) ? this.cycleCurriculumGroup.getObject() : null;
    }

    public void setCycleCurriculumGroup(CycleCurriculumGroup cycleCurriculumGroup) {
	this.cycleCurriculumGroup = (cycleCurriculumGroup != null) ? new DomainReference<CycleCurriculumGroup>(
		cycleCurriculumGroup) : null;
    }

    public boolean hasCycleCurriculumGroup() {
	return getCycleCurriculumGroup() != null;
    }

    public Registration getRegistration() {
	return (this.registration != null) ? this.registration.getObject() : null;
    }

    public void setRegistration(Registration registration) {
	this.registration = (registration != null) ? new DomainReference<Registration>(registration) : null;
    }

    public Integer getFinalAverage() {
	if (isConclusionProcessed()) {
	    return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getFinalAverage() : getRegistration().getFinalAverage();
	}

	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateRoundedAverage() : getRegistration()
		.calculateFinalAverage();
    }

    public BigDecimal getAverage() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateAverage() : getRegistration().calculateAverage();
    }

    public YearMonthDay getConclusionDate() {
	if (isConclusionProcessed()) {
	    return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getConclusionDate() : getRegistration()
		    .getConclusionDate();
	}

	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateConclusionDate() : getRegistration()
		.calculateConclusionDate();
    }

    public boolean hasDissertationThesis() {
	return getRegistration().hasDissertationThesis();
    }

    public String getDissertationThesisTitle() {
	return hasDissertationThesis() ? getRegistration().getDissertationThesisTitle() : null;
    }

    public LocalDate getDissertationThesisDiscussedDate() {
	return hasDissertationThesis() ? getRegistration().getDissertationThesisDiscussedDate() : null;
    }

    public double getEctsCredits() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCreditsConcluded() : getRegistration().getEctsCredits();
    }

    public ICurriculum getCurriculumForConclusion() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCurriculum() : getRegistration().getCurriculum();
    }

    public int getCurriculumEntriesSize() {
	return getCurriculumForConclusion().getCurriculumEntries().size();
    }

    public String getDegreeDescription() {
	return hasCycleCurriculumGroup() ? getRegistration().getDegreeDescription(getCycleCurriculumGroup().getCycleType())
		: getRegistration().getDegreeDescription();
    }

    public boolean isConcluded() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().isConcluded() : getRegistration().hasConcluded();
    }

    public Collection<CurriculumModule> getCurriculumModulesWithNoConlusionDate() {
	final Collection<CurriculumModule> result = new HashSet<CurriculumModule>();
	if (hasCycleCurriculumGroup()) {
	    getCycleCurriculumGroup().assertConclusionDate(result);
	} else {
	    getRegistration().assertConclusionDate(result);
	}
	return result;
    }

    public Collection<CurriculumGroup> getCurriculumGroupsNotVerifyingStructure() {
	if (hasCycleCurriculumGroup()) {
	    final Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
	    getCycleCurriculumGroup().assertCorrectStructure(result);
	    return result;
	} else {
	    return Collections.EMPTY_LIST;
	}
    }

    public boolean isConclusionProcessed() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().isConclusionProcessed() : getRegistration()
		.isRegistrationConclusionProcessed();
    }

    public boolean getCanBeConclusionProcessed() {
	return !isConclusionProcessed() && isConcluded() && (getRegistration().getWasTransition() || groupStructureIsValid())
		&& getCurriculumModulesWithNoConlusionDate().isEmpty();
    }

    private boolean groupStructureIsValid() {
	final Collection<CurriculumGroup> groups = getCurriculumGroupsNotVerifyingStructure();
	if (groups.isEmpty()) {
	    return true;
	} else {
	    for (final CurriculumGroup group : groups) {
		if (group.hasInsufficientCredits()) {
		    return false;
		}
	    }

	    return true;
	}
    }

    public boolean isByCycle() {
	return hasCycleCurriculumGroup();
    }

    public String getConclusionProcessNotes() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getConclusionProcessNotes() : getRegistration()
		.getConclusionProcessNotes();
    }

    public String getConclusionProcessResponsibleIstUsername() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getConclusionProcessResponsibleIstUsername()
		: getRegistration().getConclusionProcessResponsibleIstUsername();
    }

}
