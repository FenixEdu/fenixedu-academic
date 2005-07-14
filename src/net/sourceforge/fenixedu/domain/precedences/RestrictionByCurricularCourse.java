package net.sourceforge.fenixedu.domain.precedences;


/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByCurricularCourse extends RestrictionByCurricularCourse_Base {

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getPrecedentCurricularCourse()).append("\n");
        return stringBuffer.toString();
    }
	
	public void deleteRestriction(){
		super.deleteRestriction();
		setPrecedentCurricularCourse(null);
	}
}