package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author tfc130
 */
public class ReadCurricularCourseListOfExecutionCourse extends Service {

    public Object run(InfoExecutionCourse infoExecCourse) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecCourse.getInfoExecutionPeriod().getIdInternal());
        ExecutionCourse executionCourse = executionPeriod.getExecutionCourseByInitials(infoExecCourse.getSigla());

        List<InfoCurricularCourse> infoCurricularCourseList = new ArrayList<InfoCurricularCourse>();
        if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null) {

            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);

                // curricular course scope list
                List<InfoCurricularCourseScope> infoCurricularCourseScopeList = new ArrayList<InfoCurricularCourseScope>();
                for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                    InfoCurricularCourseScope infoCurricularCourseScope = InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
                    infoCurricularCourseScopeList.add(infoCurricularCourseScope);
                }
                infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);

                infoCurricularCourseList.add(infoCurricularCourse);
            }
        }

        return infoCurricularCourseList;
    }

}
