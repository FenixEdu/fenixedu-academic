package net.sourceforge.fenixedu.domain.precedences;

import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author David Santos in Jun 9, 2004
 */

public class Precedence extends Precedence_Base {

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Precedence:\n");
        stringBuffer.append(this.getCurricularCourse()).append("\n");
        List restrictions = this.getRestrictions();
        for (int i = 0; i < restrictions.size(); i++) {
            IRestriction restriction = (IRestriction) restrictions.get(i);
            stringBuffer.append(restriction).append("\n");
        }
        stringBuffer.append("---------\n");
        return stringBuffer.toString();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        List restrictions = getRestrictions();

        int size = restrictions.size();

        CurricularCourseEnrollmentType evaluate = ((IRestriction) restrictions.get(0))
                .evaluate(precedenceContext);

        for (int i = 1; i < size; i++) {
            IRestriction restriction = (IRestriction) restrictions.get(i);
            evaluate = evaluate.and(restriction.evaluate(precedenceContext));
        }

        return evaluate;
    }
	
	
	public void deletePrecedence() {
		
		List<IRestriction> restrictions = getRestrictions();
		for (IRestriction restriction : restrictions) {
			if (restriction instanceof IRestrictionByCurricularCourse)
				((IRestrictionByCurricularCourse)restriction).setPrecedentCurricularCourse(null);
		}
		
		setCurricularCourse(null);
		getRestrictions().clear();
	}
}
