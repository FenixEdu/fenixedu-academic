/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class DefineFinalDegreeWorkCandidacyRequirements implements IService {

    public DefineFinalDegreeWorkCandidacyRequirements() {
        super();
    }

    public void run(Integer executionDegreeOID, Integer minimumNumberOfCompletedCourses,
            Integer minimumNumberOfStudents, Integer maximumNumberOfStudents,
            Integer maximumNumberOfProposalCandidaciesPerGroup) throws FenixServiceException {

        if (executionDegreeOID != null) {

            try {
                ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
                IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                        .getIPersistentFinalDegreeWork();

                IExecutionDegree cursoExecucao = (IExecutionDegree) persistentFinalDegreeWork.readByOID(
                        ExecutionDegree.class, executionDegreeOID);

                if (cursoExecucao != null) {
                    IScheduleing scheduleing = persistentFinalDegreeWork
                            .readFinalDegreeWorkScheduleing(executionDegreeOID);

                    if (scheduleing == null) {
                        scheduleing = new Scheduleing();
                        scheduleing.setCurrentProposalNumber(new Integer(1));
                    }

                    persistentFinalDegreeWork.simpleLockWrite(scheduleing);
                    scheduleing.setExecutionDegree(cursoExecucao);
                    scheduleing.setMinimumNumberOfCompletedCourses(minimumNumberOfCompletedCourses);
                    scheduleing.setMinimumNumberOfStudents(minimumNumberOfStudents);
                    scheduleing.setMaximumNumberOfStudents(maximumNumberOfStudents);
                    scheduleing
                            .setMaximumNumberOfProposalCandidaciesPerGroup(maximumNumberOfProposalCandidaciesPerGroup);
                }
            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException(e);
            }

        }

    }

}