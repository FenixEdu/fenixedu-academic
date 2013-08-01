/*
 * Created on Aug 3, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import pt.ist.bennu.core.domain.Bennu;

import org.joda.time.DateTime;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidacies extends ResidenceCandidacies_Base {

    public ResidenceCandidacies() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDateDateTime(new DateTime());
    }

    public ResidenceCandidacies(String observations) {
        this();
        setObservations(observations);
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.DateTime dt = getCreationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateDateTime(null);
        } else {
            setCreationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreationDateDateTime() {
        return getCreationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasCandidate() {
        return getCandidate() != null;
    }

    @Deprecated
    public boolean hasObservations() {
        return getObservations() != null;
    }

    @Deprecated
    public boolean hasStudentDataByExecutionYear() {
        return getStudentDataByExecutionYear() != null;
    }

}
