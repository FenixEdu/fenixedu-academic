package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ComputeDegreeCourseStatistics extends ComputeCourseStatistics {

    public List<DegreeCourseStatisticsDTO> run(Integer competenceCourseId, Integer executionYearId)
            throws FenixServiceException, ExcepcaoPersistencia {
        CompetenceCourse competenceCourse = (CompetenceCourse) persistentObject.readByOID(
                CompetenceCourse.class, competenceCourseId);

        ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
                ExecutionYear.class, executionYearId);

        Map<Degree, List<CurricularCourse>> groupedCourses = competenceCourse
                .getAssociatedCurricularCoursesGroupedByDegree();

        List<DegreeCourseStatisticsDTO> results = new ArrayList<DegreeCourseStatisticsDTO>();

        for (Degree degree : groupedCourses.keySet()) {
            List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>();
            List<CurricularCourse> curricularCourses = groupedCourses.get(degree);

            for (CurricularCourse curricularCourse : curricularCourses) {
                evaluations.addAll(curricularCourse.getActiveEnrollmentEvaluations(executionYear));
            }

            DegreeCourseStatisticsDTO degreeCourseStatistics = new DegreeCourseStatisticsDTO();
            degreeCourseStatistics.setIdInternal(degree.getIdInternal());
            degreeCourseStatistics.setName(degree.getNome());
            createCourseStatistics(degreeCourseStatistics, evaluations);

            results.add(degreeCourseStatistics);
        }

        return results;
    }
}
