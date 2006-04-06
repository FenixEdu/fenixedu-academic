/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */
public class DefineFinalDegreeWorkCandidacyRequirements extends Service {

    public void run(Integer executionDegreeOID, Integer minimumNumberOfCompletedCourses,
            Integer minimumNumberOfStudents, Integer maximumNumberOfStudents,
            Integer maximumNumberOfProposalCandidaciesPerGroup) throws ExcepcaoPersistencia {

        if (executionDegreeOID != null) {

            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            ExecutionDegree cursoExecucao = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);

            if (cursoExecucao != null) {
                Scheduleing scheduleing = persistentFinalDegreeWork
                        .readFinalDegreeWorkScheduleing(executionDegreeOID);

                if (scheduleing == null) {
                    scheduleing = DomainFactory.makeScheduleing();
                    scheduleing.setCurrentProposalNumber(new Integer(1));
                }

                scheduleing.addExecutionDegrees(cursoExecucao);
                scheduleing.setMinimumNumberOfCompletedCourses(minimumNumberOfCompletedCourses);
                scheduleing.setMinimumNumberOfStudents(minimumNumberOfStudents);
                scheduleing.setMaximumNumberOfStudents(maximumNumberOfStudents);
                scheduleing
                        .setMaximumNumberOfProposalCandidaciesPerGroup(maximumNumberOfProposalCandidaciesPerGroup);
            }

        }

    }

}