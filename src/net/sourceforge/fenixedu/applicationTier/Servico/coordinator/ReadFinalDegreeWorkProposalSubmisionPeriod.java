/*
 * Created on 2004/03/10
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalSubmisionPeriod implements IService {

    public ReadFinalDegreeWorkProposalSubmisionPeriod() {
        super();
    }

    public InfoScheduleing run(Integer executionDegreeOID) throws FenixServiceException {

        InfoScheduleing infoScheduleing = null;

        if (executionDegreeOID != null) {

            try {
                ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
                IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                        .getIPersistentFinalDegreeWork();

                IExecutionDegree cursoExecucao = (IExecutionDegree) persistentFinalDegreeWork.readByOID(
                        ExecutionDegree.class, executionDegreeOID);

                if (cursoExecucao != null) {
                    IScheduleing scheduleing = persistentFinalDegreeWork
                            .readFinalDegreeWorkScheduleing(executionDegreeOID);

                    if (scheduleing != null) {
                        infoScheduleing = new InfoScheduleing();
                        infoScheduleing.setIdInternal(scheduleing.getIdInternal());
                        infoScheduleing.setStartOfProposalPeriod(scheduleing.getStartOfProposalPeriod());
                        infoScheduleing.setEndOfProposalPeriod(scheduleing.getEndOfProposalPeriod());
                        infoScheduleing.setStartOfCandidacyPeriod(scheduleing
                                .getStartOfCandidacyPeriod());
                        infoScheduleing.setEndOfCandidacyPeriod(scheduleing.getEndOfCandidacyPeriod());
                        infoScheduleing.setMinimumNumberOfCompletedCourses(scheduleing
                                .getMinimumNumberOfCompletedCourses());
                        infoScheduleing.setMinimumNumberOfStudents(scheduleing
                                .getMinimumNumberOfStudents());
                        infoScheduleing.setMaximumNumberOfStudents(scheduleing
                                .getMaximumNumberOfStudents());
                        infoScheduleing.setMaximumNumberOfProposalCandidaciesPerGroup(scheduleing
                                .getMaximumNumberOfProposalCandidaciesPerGroup());
                    }
                }
            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException(e);
            }
        }

        return infoScheduleing;
    }

}