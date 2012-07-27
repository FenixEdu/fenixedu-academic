package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceNotes;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.Interval;

public class AnnualTeachingCreditsByPeriodBean implements Serializable {
    private ExecutionSemester executionPeriod;
    private Teacher teacher;
    private TeacherServiceNotes teacherServiceNotes;

    public AnnualTeachingCreditsByPeriodBean(ExecutionSemester executionPeriod, Teacher teacher) {
	super();
	this.executionPeriod = executionPeriod;
	this.teacher = teacher;
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
	if (teacherService != null) {
	    this.teacherServiceNotes = teacherService.getTeacherServiceNotes();
	}
    }

    public List<Professorship> getProfessorships() {
	List<Professorship> professorships = new ArrayList<Professorship>();
	for (Professorship professorship : getTeacher().getProfessorships()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)
		    && (!professorship.getExecutionCourse().isDissertation())
		    && (!professorship.getExecutionCourse().getProjectTutorialCourse())) {
		professorships.add(professorship);
	    }
	}

	Collections.sort(professorships, new BeanComparator("executionCourse.name"));
	return professorships;
    }

    public List<InstitutionWorkTime> getInstitutionWorkTime() {
	List<InstitutionWorkTime> institutionWorkingTimes = new ArrayList<InstitutionWorkTime>();
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
	if (teacherService != null && !teacherService.getInstitutionWorkTimes().isEmpty()) {
	    institutionWorkingTimes.addAll(teacherService.getInstitutionWorkTimes());
	}

	ComparatorChain comparatorChain = new ComparatorChain();
	BeanComparator semesterComparator = new BeanComparator("teacherService.executionPeriod");
	BeanComparator weekDayComparator = new BeanComparator("weekDay");
	BeanComparator startTimeComparator = new BeanComparator("startTime");
	comparatorChain.addComparator(semesterComparator);
	comparatorChain.addComparator(weekDayComparator);
	comparatorChain.addComparator(startTimeComparator);
	Collections.sort(institutionWorkingTimes, comparatorChain);
	return institutionWorkingTimes;
    }

    public List<PersonFunction> getPersonFunctions() {
	List<PersonFunction> personFunctions = new ArrayList<PersonFunction>(teacher.getPerson().getPersonFuntions(
		executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay()));
	return personFunctions;
    }

    public List<OtherService> getOtherServices() {
	List<OtherService> otherServices = new ArrayList<OtherService>();
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
	if (teacherService != null && !teacherService.getOtherServices().isEmpty()) {
	    otherServices.addAll(teacherService.getOtherServices());
	}
	return otherServices;
    }

    public List<PersonContractSituation> getServiceExemptions() {
	Interval executionYearInterval = new Interval(executionPeriod.getBeginDateYearMonthDay().toDateTimeAtMidnight(),
		executionPeriod.getEndDateYearMonthDay().plusDays(1).toDateTimeAtMidnight());
	return new ArrayList<PersonContractSituation>(teacher.getValidTeacherServiceExemptions(executionYearInterval));
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public ExecutionSemester getExecutionPeriod() {
	return executionPeriod;
    }

    public TeacherServiceNotes getTeacherServiceNotes() {
	return teacherServiceNotes;
    }

}
