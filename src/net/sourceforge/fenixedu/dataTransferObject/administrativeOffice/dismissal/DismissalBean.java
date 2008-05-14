package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class DismissalBean implements Serializable, IStudentCurricularPlanBean {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private DomainReference<ExecutionSemester> executionSemester;
    private Collection<SelectedCurricularCourse> dismissals;
    private Collection<SelectedOptionalCurricularCourse> optionalDismissals;
    private DomainReference<CourseGroup> courseGroup;
    private DomainReference<CurriculumGroup> curriculumGroup;
    private Collection<SelectedEnrolment> enrolments;
    private Collection<SelectedExternalEnrolment> externalEnrolments;
    private DismissalType dismissalType;
    private Double credits;
    private Grade grade;

    public Collection<SelectedCurricularCourse> getDismissals() {
	return dismissals;
    }

    public void setDismissals(Collection<SelectedCurricularCourse> dismissals) {
	this.dismissals = dismissals;
    }

    public boolean hasAnyDismissals() {
	return getDismissals() != null && !getDismissals().isEmpty();
    }

    public boolean containsDismissal(CurricularCourse curricularCourse) {
	if (getDismissals() != null) {
	    for (SelectedCurricularCourse selectedCurricularCourse : getDismissals()) {
		if (selectedCurricularCourse.getCurricularCourse().equals(curricularCourse)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public Collection<SelectedOptionalCurricularCourse> getOptionalDismissals() {
	return optionalDismissals;
    }

    public void setOptionalDismissals(Collection<SelectedOptionalCurricularCourse> optionalDismissals) {
	this.optionalDismissals = optionalDismissals;
    }

    public boolean hasAnyOptionalDismissals() {
	return getOptionalDismissals() != null && !getOptionalDismissals().isEmpty();
    }

    public boolean containsOptionalDismissal(CurricularCourse curricularCourse) {
	if (getOptionalDismissals() != null) {
	    for (SelectedOptionalCurricularCourse selectedCurricularCourse : getOptionalDismissals()) {
		if (selectedCurricularCourse.getCurricularCourse().equals(curricularCourse)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean containsDismissalOrOptionalDismissal(final CurricularCourse curricularCourse) {
	return containsDismissal(curricularCourse) || containsOptionalDismissal(curricularCourse);
    }

    public Collection<SelectedCurricularCourse> getAllDismissals() {
	final Collection<SelectedCurricularCourse> result = new ArrayList<SelectedCurricularCourse>();
	if (getDismissals() != null) {
	    result.addAll(getDismissals());
	}
	if (getOptionalDismissals() != null) {
	    result.addAll(getOptionalDismissals());
	}
	return result;
    }

    public Collection<SelectedEnrolment> getEnrolments() {
	return enrolments;
    }

    public void setEnrolments(Collection<SelectedEnrolment> enrolments) {
	this.enrolments = enrolments;
    }

    public CourseGroup getCourseGroup() {
	return (this.courseGroup != null) ? this.courseGroup.getObject() : null;
    }

    public void setCourseGroup(CourseGroup courseGroup) {
	this.courseGroup = (courseGroup != null) ? new DomainReference<CourseGroup>(courseGroup) : null;
    }

    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public DismissalType getDismissalType() {
	return dismissalType;
    }

    public void setDismissalType(DismissalType dismissalType) {
	this.dismissalType = dismissalType;
    }

    public Grade getGrade() {
	return grade;
    }

    public void setGrade(Grade grade) {
	this.grade = grade;
    }

    public Double getCredits() {
	return credits;
    }

    public void setCredits(Double credits) {
	this.credits = credits;
    }

    public Collection<SelectedExternalEnrolment> getExternalEnrolments() {
	return externalEnrolments;
    }

    public void setExternalEnrolments(Collection<SelectedExternalEnrolment> externalEnrolments) {
	this.externalEnrolments = externalEnrolments;
    }

    public Collection<IEnrolment> getSelectedEnrolments() {
	final Collection<IEnrolment> result = new ArrayList<IEnrolment>();

	if (getEnrolments() != null) {
	    for (final SelectedEnrolment selectedEnrolment : getEnrolments()) {
		if (selectedEnrolment.getSelected()) {
		    result.add(selectedEnrolment.getEnrolment());
		}
	    }
	}

	if (getExternalEnrolments() != null) {
	    for (final SelectedExternalEnrolment selectedEnrolment : getExternalEnrolments()) {
		if (selectedEnrolment.getSelected()) {
		    result.add(selectedEnrolment.getExternalEnrolment());
		}
	    }
	}

	return result;
    }

    public static class SelectedCurricularCourse implements Serializable {

	private Boolean selected = Boolean.FALSE;

	private DomainReference<CurricularCourse> curricularCourse;
	private DomainReference<CurriculumGroup> curriculumGroup;
	private DomainReference<StudentCurricularPlan> studentCurricularPlan;

	public SelectedCurricularCourse(CurricularCourse curricularCourse, StudentCurricularPlan studentCurricularPlan) {
	    setCurricularCourse(curricularCourse);
	    setStudentCurricularPlan(studentCurricularPlan);
	}

	public CurricularCourse getCurricularCourse() {
	    return (this.curricularCourse != null) ? this.curricularCourse.getObject() : null;
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
	    this.curricularCourse = (curricularCourse != null) ? new DomainReference<CurricularCourse>(curricularCourse) : null;
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}

	public CurriculumGroup getCurriculumGroup() {
	    return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
	}

	public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	    this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
	}

	public StudentCurricularPlan getStudentCurricularPlan() {
	    return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
	}

	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	    this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		    studentCurricularPlan) : null;
	}

	public String getKey() {
	    StringBuilder stringBuilder = new StringBuilder();
	    if (this.getCurricularCourse() != null) {
		stringBuilder.append(this.getCurricularCourse().getClass().getName()).append(":").append(
			this.getCurricularCourse().getIdInternal());
	    }
	    stringBuilder.append(",");
	    if (this.getCurriculumGroup() != null) {
		stringBuilder.append(this.getCurriculumGroup().getClass().getName()).append(":").append(
			this.getCurriculumGroup().getIdInternal());
	    }
	    stringBuilder.append(",");
	    if (this.getStudentCurricularPlan() != null) {
		stringBuilder.append(this.getStudentCurricularPlan().getClass().getName()).append(":").append(
			this.getStudentCurricularPlan().getIdInternal());
	    }
	    return stringBuilder.toString();
	}

	public boolean isOptional() {
	    return false;
	}
    }

    public static class SelectedOptionalCurricularCourse extends SelectedCurricularCourse {

	private Double credits;

	public SelectedOptionalCurricularCourse(final OptionalCurricularCourse curricularCourse,
		final StudentCurricularPlan studentCurricularPlan) {
	    super(curricularCourse, studentCurricularPlan);
	}

	@Override
	public OptionalCurricularCourse getCurricularCourse() {
	    return (OptionalCurricularCourse) super.getCurricularCourse();
	}

	public Double getCredits() {
	    return credits;
	}

	public void setCredits(Double credits) {
	    this.credits = credits;
	}

	@Override
	public boolean isOptional() {
	    return true;
	}
    }

    public static class SelectedEnrolment implements Serializable {

	private Boolean selected = Boolean.FALSE;

	private DomainReference<Enrolment> enrolment;

	public SelectedEnrolment(Enrolment enrolment) {
	    setEnrolment(enrolment);
	}

	public Enrolment getEnrolment() {
	    return (this.enrolment != null) ? this.enrolment.getObject() : null;
	}

	public void setEnrolment(Enrolment enrolment) {
	    this.enrolment = (enrolment != null) ? new DomainReference<Enrolment>(enrolment) : null;
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof SelectedEnrolment)) {
		return false;
	    }
	    return equals((SelectedEnrolment) obj);
	}

	public boolean equals(final SelectedEnrolment other) {
	    return getEnrolment() == other.getEnrolment();
	}

	@Override
	public int hashCode() {
	    return getEnrolment().hashCode();
	}
    }

    public static class SelectedExternalEnrolment implements Serializable {

	private Boolean selected = Boolean.FALSE;

	private DomainReference<ExternalEnrolment> externalEnrolment;

	public SelectedExternalEnrolment(ExternalEnrolment externalEnrolment) {
	    setExternalEnrolment(externalEnrolment);
	}

	public ExternalEnrolment getExternalEnrolment() {
	    return (this.externalEnrolment != null) ? this.externalEnrolment.getObject() : null;
	}

	public void setExternalEnrolment(ExternalEnrolment externalEnrolment) {
	    this.externalEnrolment = (externalEnrolment != null) ? new DomainReference<ExternalEnrolment>(externalEnrolment)
		    : null;
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof SelectedExternalEnrolment)) {
		return false;
	    }
	    return equals((SelectedExternalEnrolment) obj);
	}

	public boolean equals(final SelectedExternalEnrolment other) {
	    return getExternalEnrolment() == other.getExternalEnrolment();
	}

	@Override
	public int hashCode() {
	    return getExternalEnrolment().hashCode();
	}
    }

    public static enum DismissalType {
	CURRICULAR_COURSE_CREDITS, CURRICULUM_GROUP_CREDITS, NO_COURSE_GROUP_CURRICULUM_GROUP_CREDITS;

	public String getName() {
	    return name();
	}
    }

}
