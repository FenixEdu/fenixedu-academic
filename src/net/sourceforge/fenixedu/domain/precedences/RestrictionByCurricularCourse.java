package net.sourceforge.fenixedu.domain.precedences;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByCurricularCourse extends RestrictionByCurricularCourse_Base {

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