package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeCompetenceCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeDegreeCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeExecutionCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class CourseStatisticsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Department department;

    private CompetenceCourse competenceCourse = null;

    private Degree degree = null;

    private ExecutionSemester executionSemester = null;

    private List<DegreeCourseStatisticsDTO> degreeCourses = null;

    private List<ExecutionCourseStatisticsDTO> executionCourses = null;

    private List<CompetenceCourseStatisticsDTO> competenceCourses = null;

    public CourseStatisticsBean(Department department, ExecutionSemester executionSemester) {
        this.department = department;
        this.executionSemester = executionSemester;
    }

    public CourseStatisticsBean(Department department, CompetenceCourse competenceCourse, ExecutionSemester executionSemester) {
        this.department = department;
        this.competenceCourse = competenceCourse;
        this.executionSemester = executionSemester;
    }

    public CourseStatisticsBean(Department department, CompetenceCourse competenceCourse, Degree degree,
            ExecutionSemester executionSemester) {
        this.department = department;
        this.degree = degree;
        this.competenceCourse = competenceCourse;
        this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionSemester() {
        if (executionSemester == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        }
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) throws FenixServiceException {
        this.executionSemester = executionSemester;
    }

    private void loadCompetenceCourses() throws FenixServiceException {
        competenceCourses =
                ComputeCompetenceCourseStatistics.runComputeCompetenceCourseStatistics(department, getExecutionSemester());
    }

    public List<CompetenceCourseStatisticsDTO> getCompetenceCourses() throws FenixServiceException {
        if (competenceCourses == null) {
            loadCompetenceCourses();
        }

        return competenceCourses;
    }

    private void loadDegreeCourses() throws FenixServiceException {
        degreeCourses = ComputeDegreeCourseStatistics.runComputeDegreeCourseStatistics(competenceCourse, getExecutionSemester());
    }

    public List<DegreeCourseStatisticsDTO> getDegreeCourses() throws FenixServiceException {
        if (degreeCourses == null) {
            loadDegreeCourses();
        }

        return degreeCourses;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public Degree getDegree() {
        return degree;
    }

    private void loadExecutionCourses() throws FenixServiceException {
        executionCourses =
                ComputeExecutionCourseStatistics.runComputeExecutionCourseStatistics(getCompetenceCourse(), getDegree(),
                        getExecutionSemester());
    }

    public List<ExecutionCourseStatisticsDTO> getExecutionCourses() throws FenixServiceException {
        if (executionCourses == null) {
            loadExecutionCourses();
        }

        return executionCourses;
    }

    public Department getDepartment() {
        return department;
    }

    public String getCompetenceCourseName() {
        return competenceCourse.getNameI18N(getExecutionSemester()).getContent();
    }
}
