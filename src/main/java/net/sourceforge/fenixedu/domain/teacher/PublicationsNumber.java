/*
 * Created on 21/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class PublicationsNumber extends PublicationsNumber_Base {

    public PublicationsNumber() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public PublicationsNumber(Teacher teacher, InfoPublicationsNumber infoPublicationsNumber) {
        this();
        if (teacher == null) {
            throw new DomainException("The teacher should not be null!");
        }

        setTeacher(teacher);
        setBasicProperties(infoPublicationsNumber);

    }

    public void edit(InfoPublicationsNumber infoPublicationsNumber) {
        setBasicProperties(infoPublicationsNumber);

    }

    private void setBasicProperties(InfoPublicationsNumber infoPublicationsNumber) {
        this.setNational(infoPublicationsNumber.getNational());
        this.setInternational(infoPublicationsNumber.getInternational());
        this.setPublicationType(infoPublicationsNumber.getPublicationType());

    }

    @Deprecated
    public java.util.Date getLastModificationDate() {
        org.joda.time.DateTime dt = getLastModificationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModificationDate(java.util.Date date) {
        if (date == null) {
            setLastModificationDateDateTime(null);
        } else {
            setLastModificationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasLastModificationDateDateTime() {
        return getLastModificationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPublicationType() {
        return getPublicationType() != null;
    }

    @Deprecated
    public boolean hasNational() {
        return getNational() != null;
    }

    @Deprecated
    public boolean hasInternational() {
        return getInternational() != null;
    }

}
