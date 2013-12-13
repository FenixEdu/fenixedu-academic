/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.domain.gesdis;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CourseHistoric extends CourseHistoric_Base {

    public CourseHistoric() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setCurricularCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

    @Deprecated
    public boolean hasEvaluated() {
        return getEvaluated() != null;
    }

    @Deprecated
    public boolean hasEnrolled() {
        return getEnrolled() != null;
    }

    @Deprecated
    public boolean hasApproved() {
        return getApproved() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
