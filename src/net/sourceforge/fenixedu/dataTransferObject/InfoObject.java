/*
 * Created on 4/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainObject;

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

	@Override
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
	@Override
	public int hashCode() {
		if (this.idInternal != null) {
			return this.idInternal.intValue();
		}

		return 0;
	}

	public void copyFromDomain(DomainObject domainObject) {
		if (domainObject != null) {
			setIdInternal(domainObject.getIdInternal());
		}
	}
}
