/*
 * Created on Aug 3, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidacies extends DomainObject implements IResidenceCandidancies {

    private Integer keyStudent;

    private Boolean dislocated;

    private String observations;

    private Date creationDate;

    private IStudent student;

    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }

    /**
     * @param keyStudent
     *            The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }

    /**
     * @return Returns the dislocated.
     */
    public Boolean getDislocated() {
        return dislocated;
    }

    /**
     * @return Returns the observations.
     */
    public String getObservations() {
        return observations;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param dislocated
     *            The dislocated to set.
     */
    public void setDislocated(Boolean dislocated) {
        this.dislocated = dislocated;
    }

    /**
     * @param observations
     *            The observations to set.
     */
    public void setObservations(String observations) {
        this.observations = observations;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IResidenceCandidancies) {
            IResidenceCandidancies residenceCandidacy = (IResidenceCandidancies) obj;
            result = (this.student.equals(residenceCandidacy.getStudent()) && this.creationDate
                    .equals(residenceCandidacy.getCreationDate()));
        }
        return result;
    }

    public String toString() {
        String result = "ResidenceCandidancy :\n";
        result += "\n  - InternalId : " + getIdInternal();
        result += "\n  - Student : " + getStudent();
        result += "\n  - Creation Date : " + getCreationDate();
        result += "\n  - Observations : " + getObservations();
        result += "\n  - Dislocated : " + getDislocated();
        return result;
    }
}