/*
 * Created on 12/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

/**
 * @author João Mota
 * 
 *  
 */
public interface IDomainObject extends Serializable {
    public Integer getIdInternal();

    public void setIdInternal(Integer idInternal);

    public Integer getAckOptLock();

    public void setAckOptLock(Integer ackOptLock);
}