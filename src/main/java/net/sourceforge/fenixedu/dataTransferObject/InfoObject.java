/*
 * Created on 4/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import pt.ist.fenixframework.DomainObject;

/**
 * @author jpvl
 */
public abstract class InfoObject extends DataTranferObject {
    private Integer externalId;

    public InfoObject() {
    }

    public InfoObject(Integer externalId) {
        setExternalId(externalId);
    }

    /**
     * @return
     */
    public Integer getExternalId() {
        return externalId;
    }

    /**
     * @param integer
     */
    public void setExternalId(Integer integer) {
        externalId = integer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoObject) {
            InfoObject infoObject = (InfoObject) obj;
            return this.getExternalId().equals(infoObject.getExternalId());
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (this.externalId != null) {
            return this.externalId.intValue();
        }

        return 0;
    }

    public void copyFromDomain(DomainObject domainObject) {
        if (domainObject != null) {
            setExternalId(domainObject.getExternalId());
        }
    }
}
