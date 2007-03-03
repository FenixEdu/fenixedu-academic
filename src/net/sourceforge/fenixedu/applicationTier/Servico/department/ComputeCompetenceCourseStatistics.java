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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author pcma
 */

public class ComputeCompetenceCourseStatistics extends ComputeCourseStatistics {

    public List<CompetenceCourseStatisticsDTO> run(Integer departementID, Integer executionPeriodID)
	    throws FenixServiceException, ExcepcaoPersistencia {
	final List<CompetenceCourseStatisticsDTO> results = new ArrayList<CompetenceCourseStatisticsDTO>();

	final Department department = rootDomainObject.readDepartmentByOID(departementID);

	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

	final Set<CompetenceCourse> competenceCourses = new HashSet(department.getCompetenceCoursesByExecutionPeriod(executionPeriod));
	competenceCourses.addAll(department.getBolonhaCompetenceCourses(executionPeriod));

	for (CompetenceCourse competenceCourse : competenceCourses) {
	    final List<Enrolment> enrollments = competenceCourse.getActiveEnrollments(executionPeriod);
	    if (enrollments.size() > 0) {
		CompetenceCourseStatisticsDTO competenceCourseStatistics = new CompetenceCourseStatisticsDTO();
		competenceCourseStatistics.setIdInternal(competenceCourse.getIdInternal());
		competenceCourseStatistics.setName(competenceCourse.getName());
		createCourseStatistics(competenceCourseStatistics, enrollments);
		results.add(competenceCourseStatistics);
	    }
	}

	Collections.sort(results, new BeanComparator("name"));

	return results;
    }
}
