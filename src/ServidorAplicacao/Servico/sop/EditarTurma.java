/*
 * EditarTurma.java Created on 27 de Outubro de 2002, 20:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurma.
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class EditarTurma implements IService
{

    /**
     * The actor of this class.
     */
    public EditarTurma()
    {
    }

    public Object run(InfoClass oldClassView, InfoClass newClassView) throws FenixServiceException
    {

        ITurma turma = null;
        boolean result = false;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(oldClassView.getInfoExecutionPeriod());

            ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(oldClassView
                    .getInfoExecutionDegree());

            turma = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(
                    oldClassView.getNome(), executionDegree, executionPeriod);
            /*
             * :FIXME: we have to change more things... to dump one year to
             * another
             */
            if (turma != null)
            {

                try
                {
                    sp.getITurmaPersistente().simpleLockWrite(turma);
                    turma.setNome(newClassView.getNome());

                }
                catch (ExistingPersistentException ex)
                {
                    throw new ExistingServiceException(ex);
                }

                result = true;
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex.getMessage());
        }

        return new Boolean(result);
    }

}