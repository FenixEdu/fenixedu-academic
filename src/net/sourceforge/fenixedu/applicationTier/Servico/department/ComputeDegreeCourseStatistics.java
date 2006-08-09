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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ComputeDegreeCourseStatistics extends ComputeCourseStatistics {

    public List<DegreeCourseStatisticsDTO> run(Integer competenceCourseId, Integer executionPeriodId)
            throws FenixServiceException, ExcepcaoPersistencia {
        CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        Map<Degree, List<CurricularCourse>> groupedCourses = competenceCourse.getAssociatedCurricularCoursesGroupedByDegree();

        List<DegreeCourseStatisticsDTO> results = new ArrayList<DegreeCourseStatisticsDTO>();

        for (Degree degree : groupedCourses.keySet()) {
            List<Enrolment> enrollments = new ArrayList<Enrolment>();
            List<CurricularCourse> curricularCourses = groupedCourses.get(degree);

            for (CurricularCourse curricularCourse : curricularCourses) {
                enrollments.addAll(curricularCourse.getActiveEnrollments(executionPeriod));
            }

            DegreeCourseStatisticsDTO degreeCourseStatistics = new DegreeCourseStatisticsDTO();
            degreeCourseStatistics.setIdInternal(degree.getIdInternal());
            degreeCourseStatistics.setName(degree.getNome());
            createCourseStatistics(degreeCourseStatistics, enrollments);

            results.add(degreeCourseStatistics);
        }

        return results;
    }

}
