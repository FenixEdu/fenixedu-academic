/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jpvl
 */
abstract public class DomainObject implements IDomainObject {
	private Integer idInternal;
	public DomainObject() {
	}

	/**
	 * @return Integer
	 */
	public Integer getIdInternal() {
		return idInternal;
	}

	/**
	 * Sets the idInternal.
	 * @param idInternal The idInternal to set
	 */
	public void setIdInternal(Integer idInternal) {
		this.idInternal = idInternal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (idInternal != null) {
			return this.idInternal.intValue();
		} else {
			return super.hashCode();
		}
	}

	/* (non-Javadoc)
	 * @see Dominio.IDomainObject#getUniqueProperties()
	 * FIXME this should not be here
	 */
	public List getUniqueProperties() {
		return new ArrayList();
	}

}
