package net.sourceforge.fenixedu.domain.precedences;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author David Santos in Jun 9, 2004
 */

public class Precedence extends Precedence_Base {

    public Precedence() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Precedence(CurricularCourse curricularCourseToAddPrecedence, String className,
	    CurricularCourse precedentCurricularCourse, Integer number) {
	this();
	setCurricularCourse(curricularCourseToAddPrecedence);

	Class[] parameters = { Integer.class, Precedence.class, CurricularCourse.class };
	Object[] arguments = { number, this, precedentCurricularCourse };

	Constructor constructor;
	try {
	    constructor = Class.forName(className).getConstructor(parameters);
	    constructor.newInstance(arguments);
	} catch (Exception e) {
	    throw new DomainException("error.manager.impossible.insertPrecedence");
	}

    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
	List restrictions = getRestrictions();

	int size = restrictions.size();

	CurricularCourseEnrollmentType evaluate = ((Restriction) restrictions.get(0)).evaluate(precedenceContext);

	for (int i = 1; i < size; i++) {
	    Restriction restriction = (Restriction) restrictions.get(i);
	    evaluate = evaluate.and(restriction.evaluate(precedenceContext));
	}

	return evaluate;
    }

    public void delete() {

	Iterator<Restriction> restrictionIterator = getRestrictionsIterator();

	while (restrictionIterator.hasNext()) {
	    Restriction restriction = restrictionIterator.next();

	    restrictionIterator.remove();
	    restriction.delete();
	}

	removeCurricularCourse();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void mergePrecedences(Precedence sourcePrecedence) {

	Iterator<Restriction> restrictionsIterator = sourcePrecedence.getRestrictionsIterator();

	while (restrictionsIterator.hasNext()) {
	    Restriction restriction = restrictionsIterator.next();

	    restrictionsIterator.remove();
	    restriction.setPrecedence(this);
	}

	sourcePrecedence.delete();
    }
}
