/*
 * Created on 12/Mai/2003
 * 
 *  
 */
package Dominio;

/**
 * @author João Mota
 * 
 *  
 */
public interface IDomainObject
{
	public Integer getIdInternal();
	public void setIdInternal(Integer idInternal);
	public Integer getAckOptLock();
	public void setAckOptLock(Integer ackOptLock);
}
