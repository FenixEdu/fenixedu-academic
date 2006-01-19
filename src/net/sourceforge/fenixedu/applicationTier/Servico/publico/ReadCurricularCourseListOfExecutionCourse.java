package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author tfc130
 */
public class ReadCurricularCourseListOfExecutionCourse extends Service {

    public Object run(InfoExecutionCourse infoExecCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        ExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecCourse.getSigla(),
                        infoExecCourse.getInfoExecutionPeriod().getIdInternal());

        List<InfoCurricularCourse> infoCurricularCourseList = new ArrayList<InfoCurricularCourse>();
        if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null) {

            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse);

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
