package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author joaosa & rmalo
 */
public class SelectExportExecutionCourse {

    @Service
    public static Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) {

        List infoExecutionCourseList = new ArrayList();

        DegreeCurricularPlan degreeCurricularPlan =
                DegreeCurricularPlan.readByNameAndDegreeSigla(infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
        if (degreeCurricularPlan != null) {
            ExecutionSemester executionSemester =
                    AbstractDomainObject.fromExternalId(infoExecutionPeriod.getExternalId());
            List<ExecutionCourse> executionCourseList =
                    degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionSemester,
                            curricularYear, infoExecutionPeriod.getSemester());

            for (int i = 0; i < executionCourseList.size(); i++) {
                ExecutionCourse aux = executionCourseList.get(i);
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(aux);
                infoExecutionCourseList.add(infoExecutionCourse);
            }
        }

        return infoExecutionCourseList;

    }

}