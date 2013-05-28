/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author pcma
 */
public class ComputeCompetenceCourseStatistics extends ComputeCourseStatistics {

    public List<CompetenceCourseStatisticsDTO> run(Integer departementID, Integer executionPeriodID) throws FenixServiceException {
        final List<CompetenceCourseStatisticsDTO> results = new ArrayList<CompetenceCourseStatisticsDTO>();

        final Department department = AbstractDomainObject.fromExternalId(departementID);

        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodID);

        final Set<CompetenceCourse> competenceCourses = new HashSet<CompetenceCourse>();
        department.addAllCompetenceCoursesByExecutionPeriod(competenceCourses, executionSemester);
        department.addAllBolonhaCompetenceCourses(competenceCourses, executionSemester);

        for (CompetenceCourse competenceCourse : competenceCourses) {
            final List<Enrolment> enrollments = competenceCourse.getActiveEnrollments(executionSemester);
            if (enrollments.size() > 0) {
                CompetenceCourseStatisticsDTO competenceCourseStatistics = new CompetenceCourseStatisticsDTO();
                competenceCourseStatistics.setExternalId(competenceCourse.getExternalId());
                competenceCourseStatistics.setName(competenceCourse.getName());
                createCourseStatistics(competenceCourseStatistics, enrollments);
                results.add(competenceCourseStatistics);
            }
        }

        Collections.sort(results, CompetenceCourseStatisticsDTO.COURSE_STATISTICS_COMPARATOR_BY_NAME);

        return results;
    }
    // Service Invokers migrated from Berserk

    private static final ComputeCompetenceCourseStatistics serviceInstance = new ComputeCompetenceCourseStatistics();

    @Service
    public static List<CompetenceCourseStatisticsDTO> runComputeCompetenceCourseStatistics(Integer departementID, Integer executionPeriodID) throws FenixServiceException  {
        return serviceInstance.run(departementID, executionPeriodID);
    }

}