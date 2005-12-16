package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProfessorship;
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
        ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentCompetenceCourse.readByOID(
                CompetenceCourse.class, competenceCourseId);

        ICursoPersistente persistenteDegree = sp.getICursoPersistente();
        IDegree degree = (IDegree) persistenteDegree.readByOID(Degree.class, degreeId);

        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearId);

        List<ICurricularCourse> curricularCourses = competenceCourse
                .getAssociatedCurricularCoursesGroupedByDegree().get(degree);

        List<IExecutionCourse> executionCourses = new ArrayList<IExecutionCourse>();

        for (IExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            for (ICurricularCourse course : curricularCourses) {
                executionCourses.addAll(course.getExecutionCoursesByExecutionPeriod(executionPeriod));
            }
        }

        List<ExecutionCourseStatisticsDTO> results = new ArrayList<ExecutionCourseStatisticsDTO>();

        for (IExecutionCourse executionCourse : executionCourses) {
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

    private String getResponsibleTeacherName(IExecutionCourse executionCourse) {
        for (IProfessorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getResponsibleFor().booleanValue()) {
                return professorship.getTeacher().getPerson().getNome();
            }
        }

        return "";
    }

    private List<String> getDegrees(IExecutionCourse executionCourse) {
        Set<IDegree> degrees = new HashSet<IDegree>();
        for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            degrees.add(curricularCourse.getDegreeCurricularPlan().getDegree());
        }

        List<String> degreeNames = new ArrayList<String>();
        for (IDegree degree : degrees) {
            degreeNames.add(degree.getNome());
        }

        return degreeNames;
    }
}
