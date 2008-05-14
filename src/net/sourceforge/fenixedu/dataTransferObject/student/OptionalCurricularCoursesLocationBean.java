package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleMoveWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class OptionalCurricularCoursesLocationBean implements Serializable {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private List<EnrolmentLocationBean> enrolmentBeans;

    private List<OptionalEnrolmentLocationBean> optionalEnrolmentBeans;

    public OptionalCurricularCoursesLocationBean(final StudentCurricularPlan studentCurricularPlan) {
	setStudentCurricularPlan(studentCurricularPlan);
	setEnrolmentBeans(new ArrayList<EnrolmentLocationBean>());
	setOptionalEnrolmentBeans(new ArrayList<OptionalEnrolmentLocationBean>());
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public List<EnrolmentLocationBean> getEnrolmentBeans() {
	return enrolmentBeans;
    }

    public void setEnrolmentBeans(List<EnrolmentLocationBean> enrolmentBeans) {
	this.enrolmentBeans = enrolmentBeans;
    }

    public List<OptionalEnrolmentLocationBean> getOptionalEnrolmentBeans() {
	return optionalEnrolmentBeans;
    }

    public void setOptionalEnrolmentBeans(List<OptionalEnrolmentLocationBean> optionalEnrolmentBeans) {
	this.optionalEnrolmentBeans = optionalEnrolmentBeans;
    }

    public void addEnrolment(Enrolment enrolment) {
	if (enrolment.isOptional()) {
	    getOptionalEnrolmentBeans().add(new OptionalEnrolmentLocationBean((OptionalEnrolment) enrolment));
	} else {
	    getEnrolmentBeans().add(new EnrolmentLocationBean(enrolment));
	}
    }

    public void addEnrolments(final Collection<Enrolment> enrolments) {
	for (final Enrolment enrolment : enrolments) {
	    addEnrolment(enrolment);
	}
    }

    public Set<IDegreeModuleToEvaluate> getIDegreeModulesToEvaluate(final ExecutionSemester executionPeriod) {
	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
	for (final EnrolmentLocationBean bean : this.enrolmentBeans) {
	    result.add(CurriculumModuleMoveWrapper.create(bean.getCurriculumGroup(getStudentCurricularPlan()), executionPeriod));
	}
	for (final OptionalEnrolmentLocationBean bean : this.optionalEnrolmentBeans) {
	    result.add(CurriculumModuleMoveWrapper.create(bean.getCurriculumGroup(), executionPeriod));
	}
	return result;
    }

    static public class EnrolmentLocationBean implements Serializable {
	private DomainReference<Enrolment> enrolment;

	private DomainReference<OptionalCurricularCourse> optionalCurricularCourse;

	public EnrolmentLocationBean(final Enrolment enrolment) {
	    setEnrolment(enrolment);
	}

	public Enrolment getEnrolment() {
	    return (this.enrolment != null) ? this.enrolment.getObject() : null;
	}

	public void setEnrolment(Enrolment enrolment) {
	    this.enrolment = (enrolment != null) ? new DomainReference<Enrolment>(enrolment) : null;
	}

	public OptionalCurricularCourse getOptionalCurricularCourse() {
	    return (this.optionalCurricularCourse != null) ? this.optionalCurricularCourse.getObject() : null;
	}

	public void setOptionalCurricularCourse(OptionalCurricularCourse optionalCurricularCourse) {
	    this.optionalCurricularCourse = (optionalCurricularCourse != null) ? new DomainReference<OptionalCurricularCourse>(
		    optionalCurricularCourse) : null;
	}

	public CurriculumGroup getCurriculumGroup(final StudentCurricularPlan studentCurricularPlan) {
	    final List<CurriculumGroup> curriculumGroups = new ArrayList<CurriculumGroup>(studentCurricularPlan
		    .getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(getOptionalCurricularCourse()));
	    Collections.sort(curriculumGroups, CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
	    return curriculumGroups.isEmpty() ? null : curriculumGroups.get(0);
	}
    }

    static public class OptionalEnrolmentLocationBean implements Serializable {
	private DomainReference<OptionalEnrolment> enrolment;

	private DomainReference<CurriculumGroup> curriculumGroup;

	public OptionalEnrolmentLocationBean(final OptionalEnrolment enrolment) {
	    setEnrolment(enrolment);
	}

	public OptionalEnrolment getEnrolment() {
	    return (this.enrolment != null) ? this.enrolment.getObject() : null;
	}

	public void setEnrolment(OptionalEnrolment enrolment) {
	    this.enrolment = (enrolment != null) ? new DomainReference<OptionalEnrolment>(enrolment) : null;
	}

	public CurriculumGroup getCurriculumGroup() {
	    return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
	}

	public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	    this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
	}
    }

}
