package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByCurricularCourse extends Restriction implements
        IRestrictionByCurricularCourse {
    protected Integer keyPrecedentCurricularCourse;

    protected ICurricularCourse precedentCurricularCourse;

    public Integer getKeyPrecedentCurricularCourse() {
        return keyPrecedentCurricularCourse;
    }

    public void setKeyPrecedentCurricularCourse(Integer keyPrecedentCurricularCourse) {
        this.keyPrecedentCurricularCourse = keyPrecedentCurricularCourse;
    }

    public ICurricularCourse getPrecedentCurricularCourse() {
        return precedentCurricularCourse;
    }

    public void setPrecedentCurricularCourse(ICurricularCourse precedentCurricularCourse) {
        this.precedentCurricularCourse = precedentCurricularCourse;
    }

    public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        if ((result) && (obj instanceof IRestrictionByCurricularCourse)) {
            IRestrictionByCurricularCourse restrictionByCurricularCourse = (IRestrictionByCurricularCourse) obj;
            result = this.getPrecedentCurricularCourse().equals(
                    restrictionByCurricularCourse.getPrecedentCurricularCourse())
                    && this.getClass().getName().equals(
                            restrictionByCurricularCourse.getClass().getName());
        } else if (result) {
            result = false;
        }
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getPrecedentCurricularCourse()).append("\n");
        return stringBuffer.toString();
    }
}