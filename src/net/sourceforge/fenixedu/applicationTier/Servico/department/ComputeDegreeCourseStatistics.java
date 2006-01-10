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
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ComputeDegreeCourseStatistics extends ComputeCourseStatistics implements IService {

    public List<DegreeCourseStatisticsDTO> run(Integer competenceCourseId, Integer executionYearId)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentCompetenceCourse persistentCompetenceCourse = sp.getIPersistentCompetenceCourse();
        CompetenceCourse competenceCourse = (CompetenceCourse) persistentCompetenceCourse.readByOID(
                CompetenceCourse.class, competenceCourseId);

        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
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
