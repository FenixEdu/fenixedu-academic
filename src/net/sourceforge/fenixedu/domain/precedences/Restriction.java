package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author David Santos in Jun 9, 2004
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

    public void setKeyPrecedence(Integer keyPrecedence) {
        this.keyPrecedence = keyPrecedence;
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

    public IPrecedence getPrecedence() {
        return precedence;
    }

    public void setPrecedence(IPrecedence precedence) {
        this.precedence = precedence;
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