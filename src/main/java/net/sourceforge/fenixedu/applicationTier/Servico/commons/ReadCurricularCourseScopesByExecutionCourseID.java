package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadCurricularCourseScopesByExecutionCourseID {

    @Atomic
    public static List<InfoCurricularCourse> run(String executionCourseID) throws FenixServiceException {

        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Set<CurricularCourseScope> curricularCourseScopes =
                    curricularCourse.findCurricularCourseScopesIntersectingExecutionCourse(executionCourse);

            final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(new ArrayList());

            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                infoCurricularCourse.getInfoScopes().add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
            infoCurricularCourses.add(infoCurricularCourse);
        }

        return infoCurricularCourses;
    }
}