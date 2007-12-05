package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.YearMonthDay;

public class RegistrationCurriculumBean extends RegistrationSelectExecutionYearBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private DomainReference<CycleCurriculumGroup> cycleCurriculumGroup;

    public RegistrationCurriculumBean(Registration registration) {
	setRegistration(registration);

	if (registration.isBolonha()) {
	    final List<CycleCurriculumGroup> internalCycleCurriculumGrops = registration.getLastStudentCurricularPlan()
		    .getInternalCycleCurriculumGrops();
	    if (internalCycleCurriculumGrops.size() == 1) {
		setCycleCurriculumGroup(internalCycleCurriculumGrops.iterator().next());
	    }
	}
    }

    public CycleCurriculumGroup getCycleCurriculumGroup() {
	return (this.cycleCurriculumGroup != null) ? this.cycleCurriculumGroup.getObject() : null;
    }

    public void setCycleCurriculumGroup(CycleCurriculumGroup cycleCurriculumGroup) {
	this.cycleCurriculumGroup = (cycleCurriculumGroup != null) ? new DomainReference<CycleCurriculumGroup>(
		cycleCurriculumGroup) : null;
    }

    public boolean hasCycleCurriculumGroup() {
	return cycleCurriculumGroup != null;
    }

    public Integer getFinalAverage() {
	if (hasCycleCurriculumGroup() && getCycleCurriculumGroup().isConclusionProcessed()) {
	    return getCycleCurriculumGroup().getFinalAverage();
	} else if (getRegistration().isRegistrationConclusionProcessed()) {
	    return getRegistration().getFinalAverage();
	} else {
	    return null;
	}
    }

    public BigDecimal getAverage() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateAverage() : getRegistration().getAverage();
    }

    public YearMonthDay getConclusionDate() {
	if (hasCycleCurriculumGroup() && getCycleCurriculumGroup().isConclusionProcessed()) {
	    return getCycleCurriculumGroup().getConclusionDate();
	} else if (getRegistration().isRegistrationConclusionProcessed()) {
	    return getRegistration().getConclusionDate();
	} else {
	    return null;
	}
    }

    public double getEctsCredits() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCreditsConcluded() : getRegistration().getEctsCredits();
    }

    public ICurriculum getCurriculum(final ExecutionYear executionYear) {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCurriculum(executionYear) : getRegistration()
		.getCurriculum(executionYear);
    }

    public boolean isConclusionProcessed() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().isConclusionProcessed() : getRegistration()
		.isRegistrationConclusionProcessed();
    }

}
