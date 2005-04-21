package net.sourceforge.fenixedu.domain;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos on Jul 26, 2004
 */

public abstract class CurricularCourseGroup extends CurricularCourseGroup_Base {

    public CurricularCourseGroup() {
        super();
        this.setOjbConcreteClass(this.getClass().getName());
    }
}