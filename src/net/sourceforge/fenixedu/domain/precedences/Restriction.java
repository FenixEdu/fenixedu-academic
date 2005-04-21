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
        boolean result = false;
        if (obj instanceof IRestriction) {
            IRestriction restriction = (IRestriction) obj;
            result = restriction.getPrecedence().getCurricularCourse().equals(
                    this.getPrecedence().getCurricularCourse());
        }
        return result;
    }
}