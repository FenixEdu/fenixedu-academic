package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ana e Ricardo
 * 
 */
public class ReadExecutionCourseWithAssociatedCurricularCourses extends Service {

    public InfoExecutionCourse run(Integer executionCourseID) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                .newInfoFromDomain(executionCourse);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);

            CollectionUtils.filter(infoCurricularCourse.getInfoScopes(), new Predicate() {
                public boolean evaluate(Object arg0) {
                    InfoCurricularCourseScope scope = (InfoCurricularCourseScope) arg0;
                    return scope.getInfoCurricularSemester().getSemester().equals(
                            executionCourse.getExecutionPeriod().getSemester());
                }
            });
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoExecutionCourse;
    }
}