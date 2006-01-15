package net.sourceforge.fenixedu.domain.precedences;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByNumberOfCurricularCourses extends RestrictionByNumberOfCurricularCourses_Base {
 
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getNumberOfCurricularCourses()).append("\n");
        return stringBuffer.toString();
    }
}