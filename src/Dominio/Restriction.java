/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;


/**
 * @author jpvl
 */

public abstract class Restriction extends DomainObject implements IRestriction {

	protected String ojbConcreteClass;
	protected IPrecedence precedence;
	protected Integer keyPrecedence; 

	public Restriction() {
		super();
		this.ojbConcreteClass = this.getClass().getName();
	}

	public Integer getKeyPrecedence() {
		return keyPrecedence;
	}

	public String getOjbConcreteClass() {
		return ojbConcreteClass;
	}

	public IPrecedence getPrecedence() {
		return precedence;
	}

	public void setKeyPrecedence(Integer integer) {
		keyPrecedence = integer;
	}

	public void setOjbConcreteClass(String string) {
		ojbConcreteClass = string;
	}

	public void setPrecedence(IPrecedence precedence) {
		this.precedence = precedence;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IRestriction){
			IRestriction restriction = (IRestriction) obj;
			result = restriction.getPrecedence().getCurricularCourse().equals(this.getPrecedence().getCurricularCourse());
		}
		return result;
	}
}