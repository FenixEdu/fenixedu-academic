/*
 * Created on 16/Nov/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoServiceProviderRegime extends InfoObject {
    private ProviderRegimeType providerRegimeType;

    private InfoTeacher infoTeacher;

    private Date lastModificationDate;

    public InfoServiceProviderRegime() {
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
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

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IServiceProviderRegime serviceProviderRegime) {
        super.copyFromDomain(serviceProviderRegime);
        if (serviceProviderRegime != null) {
            setLastModificationDate(serviceProviderRegime.getLastModificationDate());
            setProviderRegimeType(serviceProviderRegime.getProviderRegimeType());
        }
    }

    public static InfoServiceProviderRegime newInfoFromDomain(
            IServiceProviderRegime serviceProviderRegime) {
        InfoServiceProviderRegime infoServiceProviderRegime = null;
        if (serviceProviderRegime != null) {
            infoServiceProviderRegime = new InfoServiceProviderRegime();
            infoServiceProviderRegime.copyFromDomain(serviceProviderRegime);
        }
        return infoServiceProviderRegime;
    }
}