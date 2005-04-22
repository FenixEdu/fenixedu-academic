package net.sourceforge.fenixedu.domain.precedences;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByNumberOfCurricularCourses extends RestrictionByNumberOfCurricularCourses_Base {
 
	public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        if ((result) && (obj instanceof IRestrictionByNumberOfCurricularCourses)) {
            IRestrictionByNumberOfCurricularCourses restrictionByNumberOfCurricularCourses = (IRestrictionByNumberOfCurricularCourses) obj;
            result = restrictionByNumberOfCurricularCourses.getNumberOfCurricularCourses().equals(
                    this.getNumberOfCurricularCourses())
                    && this.getClass().getName().equals(
                            restrictionByNumberOfCurricularCourses.getClass().getName());
        } else if (result) {
            result = false;
        }
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getNumberOfCurricularCourses()).append("\n");
        return stringBuffer.toString();
    }
}