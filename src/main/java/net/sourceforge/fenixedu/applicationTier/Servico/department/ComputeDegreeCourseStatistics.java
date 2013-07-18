package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ComputeDegreeCourseStatistics extends ComputeCourseStatistics {

    public List<DegreeCourseStatisticsDTO> run(Integer competenceCourseId, Integer executionPeriodId)
            throws FenixServiceException {
        CompetenceCourse competenceCourse = RootDomainObject.getInstance().readCompetenceCourseByOID(competenceCourseId);
        ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);
        Map<Degree, List<CurricularCourse>> groupedCourses = competenceCourse.getAssociatedCurricularCoursesGroupedByDegree();

        List<DegreeCourseStatisticsDTO> results = new ArrayList<DegreeCourseStatisticsDTO>();

        for (Degree degree : groupedCourses.keySet()) {
            List<Enrolment> enrollments = new ArrayList<Enrolment>();
            List<CurricularCourse> curricularCourses = groupedCourses.get(degree);

            for (CurricularCourse curricularCourse : curricularCourses) {
                enrollments.addAll(curricularCourse.getActiveEnrollments(executionSemester));
            }

            DegreeCourseStatisticsDTO degreeCourseStatistics = new DegreeCourseStatisticsDTO();
            degreeCourseStatistics.setIdInternal(degree.getIdInternal());
            degreeCourseStatistics.setName(degree.getSigla());
            createCourseStatistics(degreeCourseStatistics, enrollments);

            results.add(degreeCourseStatistics);
        }

        return results;
    }

    // Service Invokers migrated from Berserk

    private static final ComputeDegreeCourseStatistics serviceInstance = new ComputeDegreeCourseStatistics();

    @Service
    public static List<DegreeCourseStatisticsDTO> runComputeDegreeCourseStatistics(Integer competenceCourseId, Integer executionPeriodId) throws FenixServiceException  {
        return serviceInstance.run(competenceCourseId, executionPeriodId);
    }

}