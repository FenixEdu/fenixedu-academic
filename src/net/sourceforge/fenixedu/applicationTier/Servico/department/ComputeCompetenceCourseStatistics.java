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
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author pcma
 */

public class ComputeCompetenceCourseStatistics extends ComputeCourseStatistics implements IService {

    public List<CompetenceCourseStatisticsDTO> run(Integer departementID, Integer executionYearID)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
        Department department = (Department) persistentDepartment.readByOID(Department.class,
                departementID);
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        List<CompetenceCourse> competenceCourses = department
                .getCompetenceCoursesByExecutionYear(executionYear);

        List<CompetenceCourse> sortedCompetenceCourses = new ArrayList<CompetenceCourse>();
        sortedCompetenceCourses.addAll(competenceCourses);

        Collections.sort(sortedCompetenceCourses, new BeanComparator("name"));

        List<CompetenceCourseStatisticsDTO> results = new ArrayList<CompetenceCourseStatisticsDTO>();

        for (CompetenceCourse competenceCourse : sortedCompetenceCourses) {
            List<EnrolmentEvaluation> evaluations = competenceCourse
                    .getActiveEnrollmentEvaluations(executionYear);

            CompetenceCourseStatisticsDTO competenceCourseStatistics = new CompetenceCourseStatisticsDTO();
            competenceCourseStatistics.setIdInternal(competenceCourse.getIdInternal());
            competenceCourseStatistics.setName(competenceCourse.getName());
            createCourseStatistics(competenceCourseStatistics, evaluations);

            results.add(competenceCourseStatistics);
        }

        return results;
    }
}
