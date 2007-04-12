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
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ComputeExecutionCourseStatistics extends ComputeCourseStatistics {

    public List<ExecutionCourseStatisticsDTO> run(Integer competenceCourseId, Integer degreeId,
            Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
        CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseId);
        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);

        List<CurricularCourse> curricularCourses = competenceCourse
                .getAssociatedCurricularCoursesGroupedByDegree().get(degree);

        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

        //for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
        for (CurricularCourse course : curricularCourses) {
            executionCourses.addAll(course.getExecutionCoursesByExecutionPeriod(executionPeriod));
        }
        //}

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
                    .getActiveEnrollments());

            results.add(executionCourseStatistics);
        }

        return results;
    }

    private String getResponsibleTeacherName(ExecutionCourse executionCourse) {
        for (Professorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getResponsibleFor().booleanValue()) {
                return professorship.getTeacher().getPerson().getName();
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
