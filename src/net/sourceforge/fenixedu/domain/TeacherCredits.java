package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class TeacherCredits extends TeacherCredits_Base {

    public TeacherCredits(Teacher teacher, TeacherCreditsState teacherCreditsState) throws ParseException {
	super();
	setTeacher(teacher);
	setTeacherCreditsState(teacherCreditsState);
	setRootDomainObject(RootDomainObject.getInstance());
	saveTeacherCredits();
    }

    public static TeacherCredits readTeacherCredits(ExecutionSemester executionSemester, Teacher teacher) {
	Set<TeacherCredits> teacherCredits = teacher.getTeacherCreditsSet();
	for (TeacherCredits teacherCredit : teacherCredits) {
	    if (teacherCredit.getTeacherCreditsState().getExecutionSemester().equals(executionSemester)) {
		return teacherCredit;
	    }
	}
	return null;
    }

    @Service
    public static void closeAllTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
	List<Teacher> teachers = RootDomainObject.getInstance().getTeachers();
	for (Teacher teacher : teachers) {
	    closeTeacherCredits(teacher, executionSemester);
	}
    }

    @Service
    public static void closeTeacherCredits(Teacher teacher, ExecutionSemester executionSemester) throws ParseException {
	if (teacher.hasTeacherCredits(executionSemester)
		&& teacher.getTeacherCredits(executionSemester).getTeacherCreditsState().isOpenState()) {
	    TeacherCredits teacherCredits = teacher.getTeacherCredits(executionSemester);
	    teacherCredits.saveTeacherCredits();
	}
	if (!teacher.hasTeacherCredits(executionSemester)) {
	    new TeacherCredits(teacher, new TeacherCreditsState(executionSemester));
	}
    }

    @Service
    public static void openAllTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
	List<Teacher> teachers = RootDomainObject.getInstance().getTeachers();
	for (Teacher teacher : teachers) {
	    if (teacher.getTeacherCredits(executionSemester).getTeacherCreditsState().isCloseState()) {
		TeacherCredits teacherCredits = teacher.getTeacherCredits(executionSemester);
		teacherCredits.openTeacherCredits(executionSemester);
	    }
	}
    }

    @Service
    public void openTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
	Teacher teacher = getTeacher();
	if (teacher.getTeacherCredits(executionSemester).getTeacherCreditsState().isCloseState()) {
	    getTeacherCreditsState().setOpenState();
	    setBasicOperations();
	}
    }

    @Service
    public void editTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
	saveTeacherCredits();
	setBasicOperations();
    }

    private void saveTeacherCredits() throws ParseException {
	Teacher teacher = getTeacher();
	ExecutionSemester executionSemester = getTeacherCreditsState().getExecutionSemester();
	double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
	double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionSemester);
	double thesesCredits = teacher.getThesesCredits(executionSemester);
	int mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	setTeacherService(teacherService);
	setThesesCredits(new BigDecimal(thesesCredits));
	setBalanceOfCredits(new BigDecimal(teacher.getBalanceOfCreditsUntil(executionSemester.getPreviousExecutionPeriod())));
	setMandatoryLessonHours(new BigDecimal(mandatoryLessonHours));
	setManagementCredits(new BigDecimal(managementCredits));
	setServiceExemptionCredits(new BigDecimal(serviceExemptionsCredits));

	setBasicOperations();
	getTeacherCreditsState().setCloseState();
    }

    private void setBasicOperations() {
	setPerson(AccessControl.getPerson());
	setLastModifiedDate(new DateTime());
    }

    private void setTeacherService(TeacherService teacherService) throws ParseException {
	if (teacherService != null) {
	    setTeachingDegreeCredits(new BigDecimal(teacherService.getTeachingDegreeCredits()));
	    setSupportLessonHours(new BigDecimal(teacherService.getSupportLessonHours()));
	    setMasterDegreeCredits(new BigDecimal(teacherService.getMasterDegreeServiceCredits()));
	    setTfcAdviseCredits(new BigDecimal(teacherService.getTeacherAdviseServiceCredits()));
	    setOtherCredits(new BigDecimal(teacherService.getOtherServiceCredits()));
	    setInstitutionWorkingHours(new BigDecimal(teacherService.getInstitutionWorkingHours()));
	    setPastServiceCredits(new BigDecimal(teacherService.getPastServiceCredits()));
	} else {
	    setTeachingDegreeCredits(new BigDecimal(0));
	    setSupportLessonHours(new BigDecimal(0));
	    setMasterDegreeCredits(new BigDecimal(0));
	    setTfcAdviseCredits(new BigDecimal(0));
	    setOtherCredits(new BigDecimal(0));
	    setInstitutionWorkingHours(new BigDecimal(0));
	    setPastServiceCredits(new BigDecimal(0));
	}
    }
}
