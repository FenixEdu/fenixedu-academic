package middleware.middlewareDomain;

/**
 * @author David Santos
 * Dec 29, 2003
 */

abstract public class MWDomainObject {

	private Integer ackOptLock;

	public MWDomainObject() {
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