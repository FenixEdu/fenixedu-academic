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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author pcma
 */

public class ComputeCompetenceCourseStatistics extends ComputeCourseStatistics {

    public List<CompetenceCourseStatisticsDTO> run(Integer departementID, Integer executionPeriodID)
            throws FenixServiceException, ExcepcaoPersistencia {

        final StringBuilder stringBuilder = new StringBuilder();
        final IUserView userView = AccessControl.getUserView();
        if (userView != null) {
            final Person person = userView.getPerson();
            stringBuilder.append("Loggend user: ");
            stringBuilder.append(person.getUsername());
            stringBuilder.append(" : ");
            stringBuilder.append(person.getName());
        }
        stringBuilder.append(" department: ");
        stringBuilder.append(departementID);
        stringBuilder.append(" executionPeriod: ");
        stringBuilder.append(executionPeriodID);
        System.out.println(stringBuilder.toString());

        Department department = rootDomainObject.readDepartmentByOID(
                departementID);

        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        
        Set<CompetenceCourse> competenceCourses = new HashSet(department
                .getCompetenceCoursesByExecutionPeriod(executionPeriod));
        
        competenceCourses.addAll(department.getBolonhaCompetenceCourses(executionPeriod));
        
      //  List<CompetenceCourse> sortedCompetenceCourses = new ArrayList<CompetenceCourse>();
       // sortedCompetenceCourses.addAll(competenceCourses);

        List<CompetenceCourse> sortedCourses = new ArrayList<CompetenceCourse>(competenceCourses);
        Collections.sort(sortedCourses, new BeanComparator("name"));

        List<CompetenceCourseStatisticsDTO> results = new ArrayList<CompetenceCourseStatisticsDTO>();

        for (CompetenceCourse competenceCourse : sortedCourses) {
            List<Enrolment> enrollments = competenceCourse.getActiveEnrollments(executionPeriod);
            
           
            if(enrollments.size()>0) {
            	CompetenceCourseStatisticsDTO competenceCourseStatistics = new CompetenceCourseStatisticsDTO();
            	competenceCourseStatistics.setIdInternal(competenceCourse.getIdInternal());
            	competenceCourseStatistics.setName(competenceCourse.getName());
            	createCourseStatistics(competenceCourseStatistics, enrollments);
            	
            	results.add(competenceCourseStatistics);
            }
        }

        return results;
    }
}
