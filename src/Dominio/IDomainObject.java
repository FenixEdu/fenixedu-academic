/*
 * Created on 12/Mai/2003
 *
 * 
 */
package Dominio;

import java.util.List;

/**
 * @author João Mota
 *
 *
 */
public interface IDomainObject {
	
	public Integer getIdInternal();
	public void setIdInternal(Integer idInternal);
	/**
	 * @return <code>String</code> list of the properties that represents the domain object.
	 */
	public List getUniqueProperties();

}
