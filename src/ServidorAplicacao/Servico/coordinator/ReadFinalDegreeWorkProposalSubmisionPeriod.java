/*
 * Created on 2004/03/10
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.finalDegreeWork.InfoScheduleing;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.finalDegreeWork.IScheduleing;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalSubmisionPeriod implements IService
{

    public ReadFinalDegreeWorkProposalSubmisionPeriod()
    {
        super();
    }

    public InfoScheduleing run(Integer executionDegreeOID) throws FenixServiceException
    {

        InfoScheduleing infoScheduleing = null;

        if (executionDegreeOID != null)
        {

            try
            {
                ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
                IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                        .getIPersistentFinalDegreeWork();

                ICursoExecucao cursoExecucao = (ICursoExecucao) persistentFinalDegreeWork.readByOID(
                        CursoExecucao.class, executionDegreeOID);

                if (cursoExecucao != null)
                {
                    IScheduleing scheduleing = persistentFinalDegreeWork
                            .readFinalDegreeWorkScheduleing(executionDegreeOID);

                    if (scheduleing != null)
                    {
                        infoScheduleing = new InfoScheduleing();
                        infoScheduleing.setIdInternal(scheduleing.getIdInternal());
                        infoScheduleing.setStartOfProposalPeriod(scheduleing.getStartOfProposalPeriod());
                        infoScheduleing.setEndOfProposalPeriod(scheduleing.getEndOfProposalPeriod());
                        infoScheduleing.setStartOfCandidacyPeriod(scheduleing.getStartOfCandidacyPeriod());
                        infoScheduleing.setEndOfCandidacyPeriod(scheduleing.getEndOfCandidacyPeriod());
                        infoScheduleing.setMinimumNumberOfCompletedCourses(scheduleing.getMinimumNumberOfCompletedCourses());
                        infoScheduleing.setMinimumNumberOfStudents(scheduleing.getMinimumNumberOfStudents());
                        infoScheduleing.setMaximumNumberOfStudents(scheduleing.getMaximumNumberOfStudents());
                        infoScheduleing.setMaximumNumberOfProposalCandidaciesPerGroup(scheduleing.getMaximumNumberOfProposalCandidaciesPerGroup());
                    }
                }
            }
            catch (ExcepcaoPersistencia e)
            {
                throw new FenixServiceException(e);
            }
        }

        return infoScheduleing;
    }

}