/*
 * Created on 4/Mai/2003 by jpvl
 *  
 */
package DataBeans;

import java.io.Serializable;

/**
 * @author jpvl
 */
public abstract class InfoObject implements Serializable
{
    private Integer idInternal;
	private Integer ackOptLock;

    public InfoObject()
    {
    }
    
    public InfoObject(Integer idInternal)
    {
        setIdInternal(idInternal);
    }

    /**
	 * @return
	 */
    public Integer getIdInternal()
    {
        return idInternal;
    }

    /**
	 * @param integer
	 */
    public void setIdInternal(Integer integer)
    {
        idInternal = integer;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode()
    {
        if (this.idInternal != null)
            return this.idInternal.intValue();
        else
            return super.hashCode();
    }

	/**
	 * @return Returns the ackOptLock.
	 */
	public Integer getAckOptLock()
	{
		return ackOptLock;
	}

	/**
	 * @param ackOptLock The ackOptLock to set.
	 */
	public void setAckOptLock(Integer ackOptLock)
	{
		this.ackOptLock = ackOptLock;
	}

}
