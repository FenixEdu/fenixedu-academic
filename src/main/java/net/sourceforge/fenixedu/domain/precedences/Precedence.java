package net.sourceforge.fenixedu.domain.precedences;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author David Santos in Jun 9, 2004
 */

public class Precedence extends Precedence_Base {

    public Precedence() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
        Collection<Restriction> restrictions = getRestrictions();

        CurricularCourseEnrollmentType evaluate = restrictions.iterator().next().evaluate(precedenceContext);

        for (Restriction restriction : restrictions) {
            evaluate = evaluate.and(restriction.evaluate(precedenceContext));
        }

        return evaluate;
    }

    public void delete() {

        Iterator<Restriction> restrictionIterator = getRestrictions().iterator();

        while (restrictionIterator.hasNext()) {
            Restriction restriction = restrictionIterator.next();

            restrictionIterator.remove();
            restriction.delete();
        }

        setCurricularCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public void mergePrecedences(Precedence sourcePrecedence) {

        Iterator<Restriction> restrictionsIterator = sourcePrecedence.getRestrictions().iterator();

        while (restrictionsIterator.hasNext()) {
            Restriction restriction = restrictionsIterator.next();

            restrictionsIterator.remove();
            restriction.setPrecedence(this);
        }

        sourcePrecedence.delete();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.precedences.Restriction> getRestrictions() {
        return getRestrictionsSet();
    }

    @Deprecated
    public boolean hasAnyRestrictions() {
        return !getRestrictionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
