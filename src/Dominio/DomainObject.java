/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
abstract public class DomainObject {
	private Integer idInternal;
	public DomainObject(){
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

}
