package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularCourse;

public interface CurricularCourseFunctor {

    public abstract void doWith(final CurricularCourse curricularCourse);

    public abstract boolean keepDoing();

}
