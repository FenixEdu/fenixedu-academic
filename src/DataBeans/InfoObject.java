/*
 * Created on 4/Mai/2003 by jpvl
 *
 */
package DataBeans;

import java.io.Serializable;

/**
 * @author jpvl
 */
public abstract class InfoObject implements Serializable {
	private Integer idInternal;
	
	

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

}
