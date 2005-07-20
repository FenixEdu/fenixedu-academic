package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author tfc130
 */
public class ReadCurricularCourseListOfExecutionCourse implements IService {

    public Object run(InfoExecutionCourse infoExecCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecCourse.getSigla(),
                        infoExecCourse.getInfoExecutionPeriod().getIdInternal());

        List<InfoCurricularCourse> infoCurricularCourseList = new ArrayList<InfoCurricularCourse>();
        if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null) {

            for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);

                // curricular course scope list
                List<InfoCurricularCourseScope> infoCurricularCourseScopeList = new ArrayList<InfoCurricularCourseScope>();
                for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                    InfoCurricularCourseScope infoCurricularCourseScope = Cloner
                            .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                    infoCurricularCourseScopeList.add(infoCurricularCourseScope);
                }
                infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);

                infoCurricularCourseList.add(infoCurricularCourse);
            }
        }

        return infoCurricularCourseList;
    }

}
