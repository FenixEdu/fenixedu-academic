package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ComputeExecutionCourseStatistics extends ComputeCourseStatistics implements IService {

    public List<ExecutionCourseStatisticsDTO> run(Integer competenceCourseId, Integer degreeId,
            Integer executionYearId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentCompetenceCourse persistentCompetenceCourse = sp.getIPersistentCompetenceCourse();
        CompetenceCourse competenceCourse = (CompetenceCourse) persistentCompetenceCourse.readByOID(
                CompetenceCourse.class, competenceCourseId);

        ICursoPersistente persistenteDegree = sp.getICursoPersistente();
        Degree degree = (Degree) persistenteDegree.readByOID(Degree.class, degreeId);

        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearId);

        List<CurricularCourse> curricularCourses = competenceCourse
                .getAssociatedCurricularCoursesGroupedByDegree().get(degree);

        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

        for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            for (CurricularCourse course : curricularCourses) {
                executionCourses.addAll(course.getExecutionCoursesByExecutionPeriod(executionPeriod));
            }
        }

        List<ExecutionCourseStatisticsDTO> results = new ArrayList<ExecutionCourseStatisticsDTO>();

        for (ExecutionCourse executionCourse : executionCourses) {
            ExecutionCourseStatisticsDTO executionCourseStatistics = new ExecutionCourseStatisticsDTO();
            executionCourseStatistics.setIdInternal(competenceCourse.getIdInternal());
            executionCourseStatistics.setName(competenceCourse.getName());

            executionCourseStatistics.setExecutionPeriod(executionCourse.getExecutionPeriod().getName());
            executionCourseStatistics.setTeacher(getResponsibleTeacherName(executionCourse));
            executionCourseStatistics.setExecutionYear(executionCourse.getExecutionPeriod()
                    .getExecutionYear().getYear());
            executionCourseStatistics.setDegrees(getDegrees(executionCourse));

            createCourseStatistics(executionCourseStatistics, executionCourse
                    .getActiveEnrollmentEvaluations());

            results.add(executionCourseStatistics);
        }

        return results;
    }

    private String getResponsibleTeacherName(ExecutionCourse executionCourse) {
        for (Professorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getResponsibleFor().booleanValue()) {
                return professorship.getTeacher().getPerson().getNome();
            }
        }

        return "";
    }

    private List<String> getDegrees(ExecutionCourse executionCourse) {
        Set<Degree> degrees = new HashSet<Degree>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            degrees.add(curricularCourse.getDegreeCurricularPlan().getDegree());
        }

        List<String> degreeNames = new ArrayList<String>();
        for (Degree degree : degrees) {
            degreeNames.add(degree.getNome());
        }

        return degreeNames;
    }
}
