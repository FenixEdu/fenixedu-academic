/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package Dominio;

import java.util.Arrays;
import java.util.List;

/**
 * @author jpvl
 */

abstract public class DomainObject implements IDomainObject {

	private Integer idInternal;

	private Integer ackOptLock;

	public DomainObject() {
	}

	public DomainObject(Integer idInternal) {
		setIdInternal(idInternal);
	}

	/**
	 * @return Integer
	 */
	public Integer getIdInternal() {
		return idInternal;
	}

	/**
	 * Sets the idInternal.
	 * 
	 * @param idInternal
	 *            The idInternal to set
	 */
	public void setIdInternal(Integer idInternal) {
		this.idInternal = idInternal;
	}

	public Integer getAckOptLock() {
		return ackOptLock;
	}

	public void setAckOptLock(Integer ackOptLock) {
		this.ackOptLock = ackOptLock;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj instanceof IDomainObject) {
			IDomainObject domainObject = (IDomainObject) obj;
			if (getIdInternal() != null && domainObject.getIdInternal() != null
					&& getIdInternal().equals(domainObject.getIdInternal())) {
				List thisInterfaces = Arrays.asList(getClass().getInterfaces());
				List objInterfaces = Arrays.asList(domainObject.getClass().getInterfaces());
				if (thisInterfaces.containsAll(objInterfaces) && objInterfaces.containsAll(thisInterfaces)) {
					return true;
				}
			}
		}
		return false;
	}

	public int hashCode() {
		if (getIdInternal() != null) {
			return getIdInternal().intValue();
		} else {
			return super.hashCode();
		}
	}

}