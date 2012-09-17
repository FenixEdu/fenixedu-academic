package net.sourceforge.fenixedu.domain.credits;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class AnnualTeachingCredits extends AnnualTeachingCredits_Base {

    public AnnualTeachingCredits(Teacher teacher, AnnualCreditsState annualCreditsState) {
	super();
	setTeacher(teacher);
	setAnnualCreditsState(annualCreditsState);
	setHasAnyLimitation(false);
	setCreationDate(new DateTime());
    }

    public boolean isPastResume() {
	return getTeachingCredits() == null && getMasterDegreeThesesCredits() == null && getPhdDegreeThesesCredits() == null
		&& getProjectsTutorialsCredits() == null && getManagementFunctionCredits() == null && getOthersCredits() == null
		&& getCreditsReduction() == null && getServiceExemptionCredits() == null && getAnnualTeachingLoad() == null
		&& getYearCredits() == null && getFinalCredits() == null;
    }

    public static AnnualTeachingCredits readByYearAndTeacher(ExecutionYear executionYear, Teacher teacher) {
	if (executionYear != null) {
	    for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCredits()) {
		if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(executionYear)) {
		    return annualTeachingCredits;
		}
	    }
	}
	return null;
    }

    public boolean isClosed() {
	return getAnnualCreditsState() != null ? getAnnualCreditsState().getIsCreditsClosed() : false;
    }

    @Service
    public void calculateCredits() {
	setMasterDegreeThesesCredits(BigDecimal.ZERO);
	setPhdDegreeThesesCredits(BigDecimal.ZERO);
	setProjectsTutorialsCredits(BigDecimal.ZERO);

	BigDecimal teachingCredits = BigDecimal.ZERO;
	BigDecimal managementFunctionsCredits = BigDecimal.ZERO;
	BigDecimal reductionServiceCredits = BigDecimal.ZERO;
	BigDecimal serviceExemptionCredits = BigDecimal.ZERO;
	BigDecimal othersCredits = BigDecimal.ZERO;
	BigDecimal annualTeachingLoad = BigDecimal.ZERO;
	BigDecimal yearCredits = BigDecimal.ZERO;
	BigDecimal yearCreditsForFinalCredits = BigDecimal.ZERO;
	BigDecimal annualTeachingLoadFinalCredits = BigDecimal.ZERO;

	boolean hasOrientantionCredits = false;
	boolean hasAccumulatedCredits = false;

	for (ExecutionSemester executionSemester : getAnnualCreditsState().getExecutionYear().getExecutionPeriods()) {
	    if (getTeacher().isActiveForSemester(executionSemester) || getTeacher().hasTeacherAuthorization(executionSemester)) {
		BigDecimal thisSemesterManagementFunctionCredits = new BigDecimal(getTeacher().getManagementFunctionsCredits(
			executionSemester));
		managementFunctionsCredits = managementFunctionsCredits.add(thisSemesterManagementFunctionCredits);
		serviceExemptionCredits = serviceExemptionCredits.add(new BigDecimal(getTeacher().getServiceExemptionCredits(
			executionSemester)));
		BigDecimal thisSemesterTeachingLoad = new BigDecimal(getTeacher().getMandatoryLessonHours(executionSemester));
		annualTeachingLoad = annualTeachingLoad.add(thisSemesterTeachingLoad);
		TeacherService teacherService = getTeacher().getTeacherServiceByExecutionPeriod(executionSemester);
		BigDecimal thisSemesterCreditsReduction = BigDecimal.ZERO;
		if (teacherService != null) {
		    teachingCredits = teachingCredits.add(new BigDecimal(teacherService.getTeachingDegreeCredits()));
		    thisSemesterCreditsReduction = teacherService.getReductionServiceCredits();
		    othersCredits = othersCredits.add(new BigDecimal(teacherService.getOtherServiceCredits()));
		}
		reductionServiceCredits = reductionServiceCredits.add(thisSemesterCreditsReduction);
		BigDecimal reductionAndManagement = thisSemesterManagementFunctionCredits.add(thisSemesterCreditsReduction);
		BigDecimal thisSemesterYearCredits = thisSemesterTeachingLoad;
		if (thisSemesterTeachingLoad.compareTo(reductionAndManagement) > 0) {
		    thisSemesterYearCredits = reductionAndManagement;
		} else {
		    setHasAnyLimitation(true);
		}
		yearCredits = yearCredits.add(thisSemesterYearCredits);
		if (getTeacher().isActiveForSemester(executionSemester) && !getTeacher().isMonitor(executionSemester)) {
		    yearCreditsForFinalCredits = yearCreditsForFinalCredits.add(thisSemesterYearCredits);
		    annualTeachingLoadFinalCredits = annualTeachingLoadFinalCredits.add(thisSemesterTeachingLoad);
		    if (executionSemester.getSemester() == 2) {
			hasAccumulatedCredits = true;
		    } else {
			hasOrientantionCredits = true;
		    }
		}
	    }
	}
	setTeachingCredits(teachingCredits);
	setManagementFunctionCredits(managementFunctionsCredits);
	setCreditsReduction(reductionServiceCredits);
	setServiceExemptionCredits(serviceExemptionCredits);
	setOthersCredits(othersCredits);
	setAnnualTeachingLoad(annualTeachingLoad);

	yearCredits = yearCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
	yearCreditsForFinalCredits = yearCreditsForFinalCredits.add(teachingCredits).add(serviceExemptionCredits)
		.add(othersCredits);
	if (hasOrientantionCredits) {
	    yearCredits = yearCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
		    .add(getProjectsTutorialsCredits());
	    yearCreditsForFinalCredits = yearCreditsForFinalCredits.add(getMasterDegreeThesesCredits())
		    .add(getPhdDegreeThesesCredits()).add(getProjectsTutorialsCredits());
	}

	setYearCredits(yearCredits);
	setFinalCredits(yearCreditsForFinalCredits.subtract(annualTeachingLoadFinalCredits));

	BigDecimal accumulatedCredits = BigDecimal.ZERO;
	if (hasAccumulatedCredits) {
	    BigDecimal lastYearAccumulated = getPreviousAccumulatedCredits();
	    accumulatedCredits = getFinalCredits().add(lastYearAccumulated);
	}
	setAccumulatedCredits(accumulatedCredits);
	setLastModifiedDate(new DateTime());

    }

    private BigDecimal getPreviousAccumulatedCredits() {
	AnnualTeachingCredits previousAnnualTeachingCredits = AnnualTeachingCredits.readByYearAndTeacher(getAnnualCreditsState()
		.getExecutionYear().getPreviousExecutionYear(), getTeacher());
	return previousAnnualTeachingCredits != null ? previousAnnualTeachingCredits.getAccumulatedCredits() : BigDecimal.ZERO;
    }
}
