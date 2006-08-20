package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadCurricularCourseScopesByExecutionCourseID extends Service {

    public List run(Integer executionCourseID) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( executionCourseID);
        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Set<CurricularCourseScope> curricularCourseScopes = curricularCourse.findCurricularCourseScopesIntersectingPeriod(
                    executionPeriod.getBeginDate(), executionPeriod.getEndDate());

            final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(
                    curricularCourse);
            infoCurricularCourse.setInfoScopes(new ArrayList());

            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                infoCurricularCourse.getInfoScopes().add(
                        InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
            infoCurricularCourses.add(infoCurricularCourse);
        }

        return infoCurricularCourses;
    }
}