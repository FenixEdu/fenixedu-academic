package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.student.Student;

public class NotNeedToEnrolEnrolmentsBean extends PageContainerBean {
    
    private Integer number;
    private DomainReference<Student> student;
    private Collection<SelectedAprovedEnrolment> aprovedEnrolments;
    
    
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public Student getStudent() {
	return (this.student != null) ? this.student.getObject() : null;
    }

    public void setStudent(Student student) {
	this.student = (student != null) ? new DomainReference<Student>(student) : null;
    }
    
    public void setAprovedEnrolments(Collection<SelectedAprovedEnrolment> enrolments) {
	this.aprovedEnrolments = enrolments;
    }
    
    public Collection<SelectedAprovedEnrolment> getAprovedEnrolments() {
	return aprovedEnrolments;
    }
    
    public Collection<Enrolment> getSelectedAprovedEnrolments() {
	Collection<Enrolment> selectedEnrolments = new ArrayList<Enrolment>();
	for (SelectedAprovedEnrolment selectedAprovedEnrolment : getAprovedEnrolments()) {
	    if(selectedAprovedEnrolment.getSelected()) {
		selectedEnrolments.add(selectedAprovedEnrolment.getAprovedEnrolment());
	    }
	}
	
	return selectedEnrolments;
    }

    
    public static class SelectedAprovedEnrolment implements Serializable {
	private Boolean selected;
	private DomainReference<Enrolment> aprovedEnrolment;
	
	public SelectedAprovedEnrolment(Enrolment enrolment, Boolean selected) {
	    this.aprovedEnrolment = new DomainReference<Enrolment>(enrolment);
	    this.selected = selected;
	}
	
	public Enrolment getAprovedEnrolment() {
	    return (this.aprovedEnrolment != null) ? this.aprovedEnrolment.getObject() : null;
	}

	public void setAprovedEnrolment(Enrolment aprovedEnrolment) {
	    this.aprovedEnrolment = (aprovedEnrolment != null) ? new DomainReference<Enrolment>(aprovedEnrolment) : null;
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}
    }

}
