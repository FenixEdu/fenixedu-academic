/*
 * Created on 4/Mai/2003 by jpvl
 *  
 */
package DataBeans;

import Dominio.IDomainObject;

/**
 * @author jpvl
 */
public abstract class InfoObject extends DataTranferObject {
    private Integer idInternal;

    public InfoObject() {
    }

    public InfoObject(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @return
     */
    public Integer getIdInternal() {
        return idInternal;
    }

    /**
     * @param integer
     */
    public void setIdInternal(Integer integer) {
        idInternal = integer;
    }

    public boolean equals(Object obj) {
        if (obj instanceof InfoObject) {
            InfoObject infoObject = (InfoObject) obj;
            return this.getIdInternal().equals(infoObject.getIdInternal());
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        if (this.idInternal != null) {
            return this.idInternal.intValue();
        }

        return 0;
    }

    public void copyFromDomain(IDomainObject domainObject) {
        if (domainObject != null) {
            setIdInternal(domainObject.getIdInternal());
        }
    }

    public void copyToDomain(InfoObject infoObject, IDomainObject domainObject) {
 		if (domainObject != null) {
            domainObject.setIdInternal(infoObject.getIdInternal());
        }
    }

}