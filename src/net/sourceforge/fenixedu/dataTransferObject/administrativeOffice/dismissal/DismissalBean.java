package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class DismissalBean implements Serializable {
    
    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private Collection<SelectedCurricularCourse> dismissals;
    private DomainReference<CourseGroup> courseGroup;
    private Collection<SelectedEnrolment> enrolments;
    private Collection<SelectedExternalEnrolment> externalEnrolments;
    private DismissalType dismissalType;
    private Double credits;
    private String grade;
  
    public Collection<SelectedCurricularCourse> getDismissals() {
        return dismissals;
    }

    public void setDismissals(Collection<SelectedCurricularCourse> dismissals) {
        this.dismissals = dismissals;
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
    
    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(studentCurricularPlan) : null;
    }
    
    public DismissalType getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(DismissalType dismissalType) {
        this.dismissalType = dismissalType;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
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

    public boolean containsDismissal(CurricularCourse curricularCourse) {
	if(getDismissals() != null) {
	    for (SelectedCurricularCourse selectedCurricularCourse : getDismissals()) {
		if(selectedCurricularCourse.getCurricularCourse().equals(curricularCourse)) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    public Collection<IEnrolment> getSelectedEnrolments() {
	final Collection<IEnrolment> result = new ArrayList<IEnrolment>();
	
	if (getEnrolments() != null) {
	    for (final SelectedEnrolment selectedEnrolment : getEnrolments()) {
		if(selectedEnrolment.getSelected()) {
		    result.add(selectedEnrolment.getEnrolment());
		}
	    }	    
	}
	
	if (getExternalEnrolments() != null) {
	    for (final SelectedExternalEnrolment selectedEnrolment : getExternalEnrolments()) {
		if(selectedEnrolment.getSelected()) {
		    result.add(selectedEnrolment.getExternalEnrolment());
		}
	    }	
	}
	
	return result;
    }
    
    public static class SelectedCurricularCourse implements Serializable {
	private Boolean selected = Boolean.FALSE;
	private DomainReference<CurricularCourse> curricularCourse;
	
	public SelectedCurricularCourse(CurricularCourse curricularCourse) {
	    setCurricularCourse(curricularCourse);
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
	    this.externalEnrolment = (externalEnrolment != null) ? new DomainReference<ExternalEnrolment>(externalEnrolment) : null;
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}
    }

    public static enum DismissalType {
	CURRICULAR_COURSE_CREDITS,
	CURRICULUM_GROUP_CREDITS;
	
	public String getName() {
	    return name();
	}
    }
    
 }
