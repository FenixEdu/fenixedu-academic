/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package Dominio;


/**
 * @author jpvl
 */

abstract public class DomainObject implements IDomainObject
{

    private Integer idInternal;
    private Integer ackOptLock;

    public DomainObject()
    {
    }

    public DomainObject(Integer idInternal)
    {
        setIdInternal(idInternal);
    }

    /**
	 * @return Integer
	 */
    public Integer getIdInternal()
    {
        return idInternal;
    }

    /**
	 * Sets the idInternal.
	 * 
	 * @param idInternal
	 *            The idInternal to set
	 */
    public void setIdInternal(Integer idInternal)
    {
        this.idInternal = idInternal;
    }

    public Integer getAckOptLock()
    {
        return ackOptLock;
    }

    public void setAckOptLock(Integer ackOptLock)
    {
        this.ackOptLock = ackOptLock;
    }

}
