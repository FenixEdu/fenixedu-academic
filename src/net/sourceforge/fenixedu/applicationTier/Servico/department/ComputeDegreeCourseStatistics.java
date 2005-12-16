package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionYear;
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
        ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentCompetenceCourse.readByOID(
                CompetenceCourse.class, competenceCourseId);

        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearId);

        Map<IDegree, List<ICurricularCourse>> groupedCourses = competenceCourse
                .getAssociatedCurricularCoursesGroupedByDegree();

        List<DegreeCourseStatisticsDTO> results = new ArrayList<DegreeCourseStatisticsDTO>();

        for (IDegree degree : groupedCourses.keySet()) {
            List<IEnrolmentEvaluation> evaluations = new ArrayList<IEnrolmentEvaluation>();
            List<ICurricularCourse> curricularCourses = groupedCourses.get(degree);

            for (ICurricularCourse curricularCourse : curricularCourses) {
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
