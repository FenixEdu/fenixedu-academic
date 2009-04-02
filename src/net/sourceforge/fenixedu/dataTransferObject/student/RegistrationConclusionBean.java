package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class RegistrationConclusionBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private DomainReference<Registration> registration;

    private DomainReference<CycleCurriculumGroup> cycleCurriculumGroup;

    private Boolean hasAccessToRegistrationConclusionProcess = Boolean.TRUE;

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

    public ExecutionYear getStartExecutionYear() {
	return getRegistration().getStartExecutionYear();
    }

    public Integer getFinalAverage() {
	if (isConclusionProcessed()) {
	    return isByCycle() ? getCycleCurriculumGroup().getFinalAverage() : getRegistration().getFinalAverage();
	}

	return calculateFinalAverage();
    }

    public Integer calculateFinalAverage() {
	return isByCycle() ? getCycleCurriculumGroup().calculateRoundedAverage() : getRegistration().calculateRoundedAverage();
    }

    public BigDecimal getAverage() {
	if (isConclusionProcessed()) {
	    return isByCycle() ? getCycleCurriculumGroup().getAverage() : getRegistration().getAverage();
	}

	return calculateAverage();
    }

    public BigDecimal calculateAverage() {
	return isByCycle() ? getCycleCurriculumGroup().calculateAverage() : getRegistration().calculateAverage();
    }

    public YearMonthDay getConclusionDate() {
	if (isConclusionProcessed()) {
	    return isByCycle() ? getCycleCurriculumGroup().getConclusionDate() : getRegistration().getConclusionDate();
	}

	return calculateConclusionDate();
    }

    public YearMonthDay calculateConclusionDate() {
	return isByCycle() ? getCycleCurriculumGroup().calculateConclusionDate() : getRegistration().calculateConclusionDate();
    }

    public ExecutionYear getIngressionYear() {
	if (isConclusionProcessed()) {
	    return isByCycle() ? getCycleCurriculumGroup().getIngressionYear() : getRegistration().getIngressionYear();
	}

	return calculateIngressionYear();
    }

    public ExecutionYear calculateIngressionYear() {
	return isByCycle() ? getCycleCurriculumGroup().calculateIngressionYear() : getRegistration().calculateIngressionYear();
    }

    public ExecutionYear getConclusionYear() {
	if (isConclusionProcessed()) {
	    return isByCycle() ? getCycleCurriculumGroup().getConclusionYear() : getRegistration().getConclusionYear();
	}

	return calculateConclusionYear();
    }

    public ExecutionYear calculateConclusionYear() {
	return isByCycle() ? getCycleCurriculumGroup().calculateConclusionYear() : getRegistration().calculateConclusionYear();
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
	if (isConclusionProcessed()) {
	    return isByCycle() ? getCycleCurriculumGroup().getCreditsConcluded() : getRegistration().getEctsCredits();
	}

	return calculateCredits();
    }

    public double calculateCredits() {
	return isByCycle() ? getCycleCurriculumGroup().calculateCreditsConcluded() : getRegistration().calculateCredits();
    }

    public ICurriculum getCurriculumForConclusion() {
	return isByCycle() ? getCycleCurriculumGroup().getCurriculum() : getRegistration().getCurriculum();
    }

    public int getCurriculumEntriesSize() {
	return getCurriculumForConclusion().getCurriculumEntries().size();
    }

    public String getDegreeDescription() {
	return isByCycle() ? getRegistration().getDegreeDescription(getCycleCurriculumGroup().getCycleType()) : getRegistration()
		.getDegreeDescription();
    }

    public boolean isConcluded() {
	return isByCycle() ? getCycleCurriculumGroup().isConcluded() : getRegistration().hasConcluded();
    }

    public Collection<CurriculumModule> getCurriculumModulesWithNoConlusionDate() {
	final Collection<CurriculumModule> result = new HashSet<CurriculumModule>();
	if (isByCycle()) {
	    getCycleCurriculumGroup().assertConclusionDate(result);
	} else {
	    getRegistration().assertConclusionDate(result);
	}
	return result;
    }

    public Collection<CurriculumGroup> getCurriculumGroupsNotVerifyingStructure() {
	if (isByCycle()) {
	    final Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
	    getCycleCurriculumGroup().assertCorrectStructure(result);
	    return result;
	} else {
	    return Collections.emptyList();
	}
    }

    public boolean isConclusionProcessed() {
	return isByCycle() ? getCycleCurriculumGroup().isConclusionProcessed() : getRegistration()
		.isRegistrationConclusionProcessed();
    }

    public boolean getCanBeConclusionProcessed() {
	return (!isConclusionProcessed() || (isConclusionProcessed() && getRegistration().canRepeatConclusionProcess(
		AccessControl.getPerson())))
		&& isConcluded()
		&& (getRegistration().getWasTransition() || groupStructureIsValid())
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
	return isByCycle() ? getCycleCurriculumGroup().getConclusionProcessNotes() : getRegistration()
		.getConclusionProcessNotes();
    }

    public Person getConclusionProcessResponsible() {
	return isByCycle() ? getCycleCurriculumGroup().getConclusionProcessResponsible() : getRegistration()
		.getConclusionProcessResponsible();
    }

    public Person getConclusionProcessLastResponsible() {
	return isByCycle() ? getCycleCurriculumGroup().getConclusionProcessLastResponsible() : getRegistration()
		.getConclusionProcessLastResponsible();
    }

    public DateTime getConclusionProcessCreationDateTime() {
	return isByCycle() ? getCycleCurriculumGroup().getConclusionProcessCreationDateTime() : getRegistration()
		.getConclusionProcessCreationDateTime();
    }

    public DateTime getConclusionProcessLastModificationDateTime() {
	return isByCycle() ? getCycleCurriculumGroup().getConclusionProcessLastModificationDateTime() : getRegistration()
		.getConclusionProcessLastModificationDateTime();
    }

    public Boolean getHasAccessToRegistrationConclusionProcess() {
	return hasAccessToRegistrationConclusionProcess;
    }

    public void setHasAccessToRegistrationConclusionProcess(Boolean hasAccessToRegistrationConclusionProcess) {
	this.hasAccessToRegistrationConclusionProcess = hasAccessToRegistrationConclusionProcess;
    }

}
