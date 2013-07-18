/*
 * Created on 2004/03/10
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalSubmisionPeriod {

    @Service
    public static InfoScheduleing run(final ExecutionDegree executionDegree) throws FenixServiceException {

        InfoScheduleing infoScheduleing = null;

        if (executionDegree != null) {
            Scheduleing scheduleing = executionDegree.getScheduling();

            if (scheduleing != null) {
                infoScheduleing = new InfoScheduleing();
                infoScheduleing.setIdInternal(scheduleing.getIdInternal());
                infoScheduleing.setStartOfProposalPeriod(scheduleing.getStartOfProposalPeriod());
                infoScheduleing.setEndOfProposalPeriod(scheduleing.getEndOfProposalPeriod());
                infoScheduleing.setStartOfCandidacyPeriod(scheduleing.getStartOfCandidacyPeriod());
                infoScheduleing.setEndOfCandidacyPeriod(scheduleing.getEndOfCandidacyPeriod());
                infoScheduleing.setMinimumNumberOfCompletedCourses(scheduleing.getMinimumNumberOfCompletedCourses());
                infoScheduleing.setMaximumCurricularYearToCountCompletedCourses(scheduleing
                        .getMaximumCurricularYearToCountCompletedCourses());
                infoScheduleing.setMinimumCompletedCurricularYear(scheduleing.getMinimumCompletedCurricularYear());
                infoScheduleing.setMinimumNumberOfStudents(scheduleing.getMinimumNumberOfStudents());
                infoScheduleing.setMaximumNumberOfStudents(scheduleing.getMaximumNumberOfStudents());
                infoScheduleing.setMaximumNumberOfProposalCandidaciesPerGroup(scheduleing
                        .getMaximumNumberOfProposalCandidaciesPerGroup());
                infoScheduleing.setAttributionByTeachers(scheduleing.getAttributionByTeachers());
                infoScheduleing.setAllowSimultaneousCoorientationAndCompanion(scheduleing
                        .getAllowSimultaneousCoorientationAndCompanion());
                infoScheduleing.setMinimumCompletedCreditsFirstCycle(scheduleing.getMinimumCompletedCreditsFirstCycle());
                infoScheduleing.setMinimumCompletedCreditsSecondCycle(scheduleing.getMinimumCompletedCreditsSecondCycle());
            }
        }

        return infoScheduleing;
    }

}