package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod, Integer year) {

        List listInfoDE = new ArrayList();

        CurricularYear curricularYear = CurricularYear.readByYear(year);
        ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(infoExecutionPeriod.getExternalId());
        DegreeCurricularPlan degreeCurricularPlan =
                AbstractDomainObject.fromExternalId(infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());

        if (executionSemester != null) {
            List<ExecutionCourse> listDCDE =
                    executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                            degreeCurricularPlan, curricularYear, "%");

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                ExecutionCourse elem = (ExecutionCourse) iterator.next();

                listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

            }
        }
        return listInfoDE;
    }

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List run(InfoExecutionDegree infoExecutionDegree, AcademicInterval academicInterval, Integer year) {

        List listInfoDE = new ArrayList();

        CurricularYear curricularYear = CurricularYear.readByYear(year);

        DegreeCurricularPlan degreeCurricularPlan =
                AbstractDomainObject.fromExternalId(infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());

        if (academicInterval != null) {
            List<ExecutionCourse> listDCDE =
                    ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(academicInterval,
                            degreeCurricularPlan, curricularYear, "%");

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                ExecutionCourse elem = (ExecutionCourse) iterator.next();

                listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

            }
        }
        return listInfoDE;
    }
}