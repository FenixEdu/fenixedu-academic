package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Mota
 */
public class SelectExecutionCourse extends Service {

    public Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) throws ExcepcaoPersistencia {

        List infoExecutionCourseList = new ArrayList();

        List executionCourseList = null;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        if (infoExecutionDegree != null) {
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoExecutionDegree
                    .getInfoDegreeCurricularPlan());
            infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                    infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree());
        }

        executionCourseList = executionCourseDAO
                .readByCurricularYearAndExecutionPeriodAndExecutionDegree(curricularYear,
                        infoExecutionPeriod.getSemester(), infoExecutionDegree
                                .getInfoDegreeCurricularPlan().getName(), infoExecutionDegree
                                .getInfoDegreeCurricularPlan().getInfoDegree().getSigla(),
                        infoExecutionPeriod.getIdInternal());

        for (int i = 0; i < executionCourseList.size(); i++) {
            ExecutionCourse executionCourse = (ExecutionCourse) executionCourseList.get(i);

            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                    .newInfoFromDomain(executionCourse);
            infoExecutionCourseList.add(infoExecutionCourse);
        }

        return infoExecutionCourseList;
    }

}
