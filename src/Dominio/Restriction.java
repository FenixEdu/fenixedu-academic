/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;


/**
 * @author jpvl
 */

public abstract class Restriction extends DomainObject implements IRestriction {
	private String ojbConcreteClass;
	private IPrecedence precedence;
	private Integer keyPrecedence; 

	/**
	 * 
	 */
	public Restriction() {
		super();
		this.ojbConcreteClass = this.getClass().getName();
	}

	/**
	 * @return
	 */
	public Integer getKeyPrecedence() {
		return keyPrecedence;
	}

	/**
	 * @return
	 */
	public String getOjbConcreteClass() {
		return ojbConcreteClass;
	}

	/**
	 * @return
	 */
	public IPrecedence getPrecedence() {
		return precedence;
	}

	/**
	 * @param integer
	 */
	public void setKeyPrecedence(Integer integer) {
		keyPrecedence = integer;
	}

	/**
	 * @param string
	 */
	public void setOjbConcreteClass(String string) {
		ojbConcreteClass = string;
	}

	/**
	 * @param precedence
	 */
	public void setPrecedence(IPrecedence precedence) {
		this.precedence = precedence;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IRestriction){
			IRestriction restriction = (IRestriction) obj;
			result = restriction.getPrecedence().getCurricularCourse().equals(this.getPrecedence().getCurricularCourse());
		}
		return result;
	}
}