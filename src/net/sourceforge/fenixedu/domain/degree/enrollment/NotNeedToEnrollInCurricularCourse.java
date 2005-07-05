package net.sourceforge.fenixedu.domain.degree.enrollment;

/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    public NotNeedToEnrollInCurricularCourse() {
        super();
    }

    public boolean equals(Object obj) {
        if (obj instanceof INotNeedToEnrollInCurricularCourse) {
            final INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (INotNeedToEnrollInCurricularCourse) obj;
            return this.getIdInternal().equals(notNeedToEnrollInCurricularCourse.getIdInternal());
        }
        return false;
    }

}
