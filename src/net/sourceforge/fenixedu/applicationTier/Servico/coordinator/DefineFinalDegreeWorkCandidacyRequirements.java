/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class DefineFinalDegreeWorkCandidacyRequirements extends Service {

    public void run(Integer executionDegreeOID, Integer minimumNumberOfCompletedCourses,
    		Integer maximumCurricularYearToCountCompletedCourses,
    		Integer minimumCompletedCurricularYear,
            Integer minimumNumberOfStudents, Integer maximumNumberOfStudents,
            Integer maximumNumberOfProposalCandidaciesPerGroup, Boolean attributionByTeachers) throws ExcepcaoPersistencia {

        if (executionDegreeOID != null) {

            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);

            if (executionDegree != null) {
                Scheduleing scheduleing = executionDegree.getScheduling();

                if (scheduleing == null) {
                    scheduleing = new Scheduleing();
                    scheduleing.setCurrentProposalNumber(new Integer(1));
                }

                scheduleing.addExecutionDegrees(executionDegree);
                scheduleing.setMinimumNumberOfCompletedCourses(minimumNumberOfCompletedCourses);
                scheduleing.setMaximumCurricularYearToCountCompletedCourses(maximumCurricularYearToCountCompletedCourses);
                scheduleing.setMinimumCompletedCurricularYear(minimumCompletedCurricularYear);
                scheduleing.setMinimumNumberOfStudents(minimumNumberOfStudents);
                scheduleing.setMaximumNumberOfStudents(maximumNumberOfStudents);
                scheduleing.setMaximumNumberOfProposalCandidaciesPerGroup(maximumNumberOfProposalCandidaciesPerGroup);
                scheduleing.setAttributionByTeachers(Boolean.valueOf(attributionByTeachers != null && attributionByTeachers.booleanValue()));
            }

        }

    }

}