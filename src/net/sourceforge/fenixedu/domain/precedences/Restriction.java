package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class Restriction extends Restriction_Base {
    
    public Restriction() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }
	
	public void deleteRestriction() {
		setPrecedence(null);
	}

}