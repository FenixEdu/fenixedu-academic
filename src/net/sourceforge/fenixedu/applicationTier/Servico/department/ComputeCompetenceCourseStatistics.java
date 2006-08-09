/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author pcma
 */

public class ComputeCompetenceCourseStatistics extends ComputeCourseStatistics {

    public List<CompetenceCourseStatisticsDTO> run(Integer departementID, Integer executionPeriodID)
            throws FenixServiceException, ExcepcaoPersistencia {
        Department department = rootDomainObject.readDepartmentByOID(
                departementID);

        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        
        List<CompetenceCourse> competenceCourses = department
                .getCompetenceCoursesByExecutionPeriod(executionPeriod);
        

        List<CompetenceCourse> sortedCompetenceCourses = new ArrayList<CompetenceCourse>();
        sortedCompetenceCourses.addAll(competenceCourses);

        Collections.sort(sortedCompetenceCourses, new BeanComparator("name"));

        List<CompetenceCourseStatisticsDTO> results = new ArrayList<CompetenceCourseStatisticsDTO>();

        for (CompetenceCourse competenceCourse : sortedCompetenceCourses) {
            List<Enrolment> enrollments = competenceCourse
                    .getActiveEnrollments(executionPeriod);
            
           

            CompetenceCourseStatisticsDTO competenceCourseStatistics = new CompetenceCourseStatisticsDTO();
            competenceCourseStatistics.setIdInternal(competenceCourse.getIdInternal());
            competenceCourseStatistics.setName(competenceCourse.getName());
            createCourseStatistics(competenceCourseStatistics, enrollments);

            results.add(competenceCourseStatistics);
        }

        return results;
    }
}
