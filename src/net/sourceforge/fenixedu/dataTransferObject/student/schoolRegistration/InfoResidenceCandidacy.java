/*
 * Created on Aug 15, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.IResidenceCandidacies;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoResidenceCandidacy extends InfoObject {

    private Boolean dislocated;

    private String observations;

    private Date creationDate;

    private InfoStudent infoStudent;

    public InfoResidenceCandidacy() {

    }

    public InfoResidenceCandidacy(InfoStudent infostudent, Date creationDate, String observations,
            Boolean dislocated) {

        setCreationDate(creationDate);
        setObservations(observations);
        setInfoStudent(infostudent);
        setDislocated(dislocated);
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return Returns the dislocated.
     */
    public Boolean getDislocated() {
        return dislocated;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @return Returns the observations.
     */
    public String getObservations() {
        return observations;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @param dislocated
     *            The dislocated to set.
     */
    public void setDislocated(Boolean dislocated) {
        this.dislocated = dislocated;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    /**
     * @param observations
     *            The observations to set.
     */
    public void setObservations(String observations) {
        this.observations = observations;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoResidenceCandidacy) {
            InfoResidenceCandidacy infoResidenceCandidacy = (InfoResidenceCandidacy) obj;
            result = (this.getInfoStudent().equals(infoResidenceCandidacy.getInfoStudent()) && this
                    .getCreationDate().equals(infoResidenceCandidacy.getCreationDate()));
        }
        return result;
    }

    public void copyFromDomain(IResidenceCandidacies residentCandidacy) {
        super.copyFromDomain(residentCandidacy);

        if (residentCandidacy != null) {
            setCreationDate(residentCandidacy.getCreationDate());
            setDislocated(residentCandidacy.getDislocated());
            setObservations(residentCandidacy.getObservations());
        }
    }

    public static InfoResidenceCandidacy newInfoFromDomain(IResidenceCandidacies residenceCandidacy) {
        InfoResidenceCandidacy infoResidenceCandidacy = null;
        if (residenceCandidacy != null) {
            infoResidenceCandidacy = new InfoResidenceCandidacy();
            infoResidenceCandidacy.copyFromDomain(residenceCandidacy);
        }
        return infoResidenceCandidacy;
    }
}