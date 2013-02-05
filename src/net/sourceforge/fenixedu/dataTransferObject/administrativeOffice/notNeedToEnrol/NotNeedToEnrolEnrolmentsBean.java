package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class NotNeedToEnrolEnrolmentsBean extends PageContainerBean {

    private Integer number;
    private Student student;
    private Collection<SelectedAprovedEnrolment> aprovedEnrolments;
    private Collection<SelectedExternalEnrolment> externalEnrolments;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setAprovedEnrolments(Collection<SelectedAprovedEnrolment> enrolments) {
        this.aprovedEnrolments = enrolments;
    }

    public Collection<SelectedAprovedEnrolment> getAprovedEnrolments() {
        return aprovedEnrolments;
    }

    public void setExternalEnrolments(Collection<SelectedExternalEnrolment> enrolments) {
        this.externalEnrolments = enrolments;
    }

    public Collection<SelectedExternalEnrolment> getExternalEnrolments() {
        return externalEnrolments;
    }

    public Collection<Enrolment> getSelectedAprovedEnrolments() {
        Collection<Enrolment> selectedEnrolments = new ArrayList<Enrolment>();
        if (getAprovedEnrolments() != null) {
            for (SelectedAprovedEnrolment selectedAprovedEnrolment : getAprovedEnrolments()) {
                if (selectedAprovedEnrolment.getSelected()) {
                    selectedEnrolments.add(selectedAprovedEnrolment.getAprovedEnrolment());
                }
            }
        }

        return selectedEnrolments;
    }

    public Collection<ExternalEnrolment> getSelectedExternalEnrolments() {
        Collection<ExternalEnrolment> selectedEnrolments = new ArrayList<ExternalEnrolment>();
        if (getExternalEnrolments() != null) {
            for (SelectedExternalEnrolment selectedExternalEnrolment : getExternalEnrolments()) {
                if (selectedExternalEnrolment.getSelected()) {
                    selectedEnrolments.add(selectedExternalEnrolment.getExternalEnrolment());
                }
            }
        }

        return selectedEnrolments;
    }

    public static class SelectedAprovedEnrolment implements Serializable {
        private Boolean selected;
        private Enrolment aprovedEnrolment;

        public SelectedAprovedEnrolment(Enrolment enrolment, Boolean selected) {
            this.aprovedEnrolment = enrolment;
            this.selected = selected;
        }

        public Enrolment getAprovedEnrolment() {
            return this.aprovedEnrolment;
        }

        public void setAprovedEnrolment(Enrolment aprovedEnrolment) {
            this.aprovedEnrolment = aprovedEnrolment;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }
    }

    public static class SelectedExternalEnrolment implements Serializable {
        private Boolean selected;
        private ExternalEnrolment externalEnrolment;

        public SelectedExternalEnrolment(ExternalEnrolment externalEnrolment, Boolean selected) {
            this.externalEnrolment = externalEnrolment;
            this.selected = selected;
        }

        public ExternalEnrolment getExternalEnrolment() {
            return this.externalEnrolment;
        }

        public void setExternalEnrolment(ExternalEnrolment externalEnrolment) {
            this.externalEnrolment = externalEnrolment;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }
    }

}
