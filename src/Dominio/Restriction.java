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
	private Precedence precedence;
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
	public Precedence getPrecedence() {
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
	public void setPrecedence(Precedence precedence) {
		this.precedence = precedence;
	}

}
