/*
 * Created on 16/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ServiceProviderRegime extends DomainObject implements IServiceProviderRegime {
    private ProviderRegimeType providerRegimeType;

    private Integer keyTeacher;

    private ITeacher teacher;

    private Date lastModificationDate;

    /**
     *  
     */
    public ServiceProviderRegime() {
        super();
    }

    /**
     * @param serviceProviderRegimeId
     */
    public ServiceProviderRegime(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return Returns the providerRegimeType.
     */
    public ProviderRegimeType getProviderRegimeType() {
        return providerRegimeType;
    }

    /**
     * @param providerRegimeType
     *            The providerRegimeType to set.
     */
    public void setProviderRegimeType(ProviderRegimeType providerRegimeType) {
        this.providerRegimeType = providerRegimeType;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate
     *            The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
}