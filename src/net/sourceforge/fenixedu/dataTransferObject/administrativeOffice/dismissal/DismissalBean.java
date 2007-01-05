package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DismissalBean implements Serializable{
    
    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<CurriculumGroup> curriculumGroup;
    
    private Collection<SelectedEnrolment> enrolments;
    
    private Collection<SelectedCurricularCourse> curricularCourses;
    
    private Double credits;
    
    private String grade;

    public Collection<SelectedCurricularCourse> getCurricularCourses() {
        return curricularCourses;
    }

    public void setCurricularCourses(Collection<SelectedCurricularCourse> curricularCourses) {
        this.curricularCourses = curricularCourses;
    }

    public Collection<SelectedEnrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(Collection<SelectedEnrolment> enrolments) {
        this.enrolments = enrolments;
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
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(studentCurricularPlan) : null;
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
    
    public static class SelectedCurricularCourse implements Serializable {
	
	private Boolean selected = Boolean.FALSE;
	
	private GenericPair<DomainReference<CurriculumGroup>, DomainReference<CurricularCourse>> curricularCourse;
	
	public SelectedCurricularCourse(GenericPair<CurriculumGroup, CurricularCourse> curricularCourse) {
	    
	}

	public GenericPair<CurriculumGroup, CurricularCourse> getCurricularCourse() {
	    return new GenericPair<CurriculumGroup, CurricularCourse>((this.curricularCourse.getLeft() != null) ? this.curricularCourse.getLeft().getObject() : null, 
	    (this.curricularCourse.getRight() != null) ? this.curricularCourse.getRight().getObject() : null);   
	}

	public void setCurricularCourse(
		GenericPair<CurriculumGroup, CurricularCourse> curricularCourse) {
	    this.curricularCourse = new GenericPair<DomainReference<CurriculumGroup>, DomainReference<CurricularCourse>>(
		    (curricularCourse.getLeft() != null) ? new DomainReference<CurriculumGroup>(curricularCourse.getLeft()) : null,
			    (curricularCourse.getRight() != null) ? new DomainReference<CurricularCourse>(curricularCourse.getRight()) : null);
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}	
    }
    
}
