/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package Dominio;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

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
				thisInterfaces = (List) CollectionUtils.select(thisInterfaces, new IS_NOT_IDOMAIN_PREDICATE());
				objInterfaces = (List) CollectionUtils.select(objInterfaces, new IS_NOT_IDOMAIN_PREDICATE());
				if (!CollectionUtils.intersection(thisInterfaces, objInterfaces).isEmpty()) {
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

	private class IS_NOT_IDOMAIN_PREDICATE implements Predicate {
	    
		public boolean evaluate(Object arg0) {
			Class class1 = (Class) arg0;
			if (class1.getName().equals("Dominio.IDomainObject")) {
				return false; 
			} else {
				return true;
			}
		}
	}

}