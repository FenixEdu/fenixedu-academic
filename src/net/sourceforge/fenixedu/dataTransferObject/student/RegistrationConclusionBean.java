package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.YearMonthDay;

public class RegistrationConclusionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5825221957160251388L;

    private DomainReference<Registration> registration;

    private DomainReference<CycleCurriculumGroup> cycleCurriculumGroup;

    public RegistrationConclusionBean(Registration registration) {
	setRegistration(registration);

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
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateRoundedAverage() : getRegistration()
		.calculateFinalAverage();
    }

    public BigDecimal getAverage() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateAverage() : getRegistration().getAverage();
    }

    public YearMonthDay getConclusionDate() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateConclusionDate() : getRegistration()
		.calculateConclusionDate();
    }

    public ICurriculum getCurriculumForConclusion() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCurriculum() : getRegistration().getCurriculum();
    }

    public boolean isConcluded() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().isConcluded() : getRegistration().hasConcluded();
    }

    public boolean isByCycle() {
	return hasCycleCurriculumGroup();
    }

}
