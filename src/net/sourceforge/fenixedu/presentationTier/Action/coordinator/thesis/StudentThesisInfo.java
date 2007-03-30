package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class StudentThesisInfo {

    private Student student;
    private Enrolment enrolment;
    private Thesis thesis;
    private ThesisPresentationState state;
    
    public StudentThesisInfo(Student student, Enrolment enrolment) {
        setStudent(student);
        setEnrolment(enrolment);
        setThesis(enrolment.getThesis());
    }

    public Student getStudent() {
        return this.student;
    }

    protected void setStudent(Student student) {
        this.student = student;
    }
   
    public Enrolment getEnrolment() {
        return this.enrolment;
    }
    
    protected void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public Thesis getThesis() {
        return this.thesis;
    }

    protected void setThesis(Thesis thesis) {
        this.thesis = thesis;
        
        setState(thesis);
    }

    public MultiLanguageString getTitle() {
        Thesis thesis = getThesis();
        
        return thesis == null ? new MultiLanguageString("-") : thesis.getTitle();
    }
    
    public ThesisPresentationState getState() {
        return this.state;
    }

    private void setState(Thesis thesis) {
        if (thesis == null) {
            this.state = ThesisPresentationState.UNEXISTING;
        }
        else {
            if (thesis.isRejected()) {
                this.state = ThesisPresentationState.REJECTED;
            }
            else if (thesis.isDraft()) {
                this.state = ThesisPresentationState.DRAFT;
            }
            else if (thesis.isSubmitted()) {
                this.state = ThesisPresentationState.SUBMITTED;
            }
            else if (thesis.isWaitingConfirmation()) {
                this.state = ThesisPresentationState.APPROVED;
            }
            else if (thesis.isConfirmed()) {
                this.state = ThesisPresentationState.CONFIRMED;
            }
            else if (thesis.isEvaluated()) {
                this.state = ThesisPresentationState.EVALUATED;
            }
            else {
                this.state = ThesisPresentationState.UNKNOWN;
            }
        }
    }
    
    public Integer getThesisId() {
        Thesis thesis = getThesis();
        
        return thesis == null ? null : thesis.getIdInternal();
    }

    public boolean isUnassigned() {
        return getThesis() == null;
    }

    public boolean isDraft() {
        return getThesis() != null && getThesis().isDraft();
    }
    
    public boolean isSubmitted() {
        return getThesis() != null && getThesis().isSubmitted();
    }
    
    public boolean isWaitingConfirmation() {
        return getThesis() != null && getThesis().isWaitingConfirmation();
    }
    
    public boolean isConfirmed() {
        return getThesis() != null && getThesis().isConfirmed();
    }
    
    public boolean isEvaluated() {
        return getThesis() != null && getThesis().isEvaluated();
    }
    
}
