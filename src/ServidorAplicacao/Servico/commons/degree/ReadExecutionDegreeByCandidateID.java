package ServidorAplicacao.Servico.commons.degree;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID implements IServico
{
    private static ReadExecutionDegreeByCandidateID service = new ReadExecutionDegreeByCandidateID();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionDegreeByCandidateID getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionDegreeByCandidateID";
    }

    public InfoExecutionDegree run(Integer candidateID) throws NonExistingServiceException
    {

        ICursoExecucao executionDegree = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IMasterDegreeCandidate mdcTemp = new MasterDegreeCandidate();
            mdcTemp.setIdInternal(candidateID);
            IMasterDegreeCandidate masterDegreeCandidate =
                (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(
                    mdcTemp,
                    false);

            ICursoExecucao executionDegreeTemp = new CursoExecucao();
            executionDegreeTemp.setIdInternal(
                masterDegreeCandidate.getExecutionDegree().getIdInternal());

            executionDegree =
                (ICursoExecucao) sp.getICursoExecucaoPersistente().readByOId(executionDegreeTemp, false);

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new RuntimeException(ex);
        }

        if (executionDegree == null)
        {
            throw new NonExistingServiceException();
        }

        return Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
    }

}
