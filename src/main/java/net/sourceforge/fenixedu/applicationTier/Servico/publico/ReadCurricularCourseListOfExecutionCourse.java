package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author tfc130
 */
public class ReadCurricularCourseListOfExecutionCourse {

    @Service
    public static Object run(InfoExecutionCourse infoExecCourse) {
        final ExecutionSemester executionSemester =
                AbstractDomainObject.fromExternalId(infoExecCourse.getInfoExecutionPeriod().getExternalId());
        ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(infoExecCourse.getSigla());

        List<InfoCurricularCourse> infoCurricularCourseList = new ArrayList<InfoCurricularCourse>();
        if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null) {

            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);

                // curricular course scope list
                List<InfoCurricularCourseScope> infoCurricularCourseScopeList = new ArrayList<InfoCurricularCourseScope>();
                for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                    InfoCurricularCourseScope infoCurricularCourseScope =
                            InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
                    infoCurricularCourseScopeList.add(infoCurricularCourseScope);
                }
                infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);

                infoCurricularCourseList.add(infoCurricularCourse);
            }
        }

        return infoCurricularCourseList;
    }

}