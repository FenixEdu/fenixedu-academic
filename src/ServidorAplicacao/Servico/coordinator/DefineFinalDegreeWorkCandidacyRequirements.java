/*
 * Created on 2004/04/21
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.finalDegreeWork.IScheduleing;
import Dominio.finalDegreeWork.Scheduleing;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

                ICursoExecucao cursoExecucao = (ICursoExecucao) persistentFinalDegreeWork.readByOID(
                        CursoExecucao.class, executionDegreeOID);

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