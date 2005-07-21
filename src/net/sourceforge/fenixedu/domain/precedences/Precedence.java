package net.sourceforge.fenixedu.domain.precedences;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class Precedence extends Precedence_Base {

	public Precedence () {}
	
	public Precedence(ICurricularCourse curricularCourseToAddPrecedence, String className, ICurricularCourse precedentCurricularCourse, Integer number) {
		
		setCurricularCourse(curricularCourseToAddPrecedence);
		
		Class[] parameters = {Integer.class, IPrecedence.class, ICurricularCourse.class};
		Object[] arguments = {number, this, precedentCurricularCourse};
				
		Constructor constructor;
		try {
			constructor = Class.forName(className).getConstructor(parameters);
			constructor.newInstance(arguments);
		} catch (Exception e) {
			throw new RuntimeException("error.manager.impossible.insertPrecedence");
		} 

	}
	
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
	
	
	public void delete() {
		
		Iterator<IRestriction> restrictionIterator = getRestrictionsIterator();
		
		while (restrictionIterator.hasNext()) {
			IRestriction restriction = restrictionIterator.next();
			
			restrictionIterator.remove();
			restriction.delete();
		}
		
		removeCurricularCourse();
		super.deleteDomainObject();
	}
	
	
	public void mergePrecedences(IPrecedence sourcePrecedence) {
		
        Iterator<IRestriction> restrictionsIterator = sourcePrecedence.getRestrictionsIterator();
		
        while (restrictionsIterator.hasNext()) {
			IRestriction restriction = restrictionsIterator.next();
			
			restrictionsIterator.remove();
			restriction.setPrecedence(this);
        }

		sourcePrecedence.delete();
	}
}
