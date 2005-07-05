package net.sourceforge.fenixedu.domain.precedences;


/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class Restriction extends Restriction_Base {
    
    public Restriction() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    public boolean equals(Object obj) {
        if (obj instanceof IRestriction) {
            final IRestriction restriction = (IRestriction) obj;
            return this.getIdInternal().equals(restriction.getIdInternal());
        }
        return false;
    }
}