/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package Dominio;

import org.apache.commons.lang.builder.HashCodeBuilder;

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

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
//    public int hashCode()
//    {
//        if (idInternal != null)
//        {
//            return this.idInternal.intValue();
//        }
//        else
//        {
//            return super.hashCode();
//        }
//    }

    // This method may be deleted along with every equals when we upgrade OJB >= RC7.
    public int hashCode() {
        HashCodeBuilder hb = new HashCodeBuilder();
        hb.append(this.getClass().hashCode());

        hb.append(idInternal);

        return new Integer(hb.toHashCode()).intValue();
    }
}
