/*
 * EditarTurma.java Created on 27 de Outubro de 2002, 20:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho e Luis Cruz
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

public class EditarTurma implements IService {

    public Object run(final InfoClass oldClassView, final InfoClass newClassView) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        final IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(oldClassView
                .getInfoExecutionPeriod());

        final ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(oldClassView
                .getInfoExecutionDegree());

        final ITurma classToEdit = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(
                oldClassView.getNome(), executionDegree, executionPeriod);

        final ITurma otherClassWithSameNewName = sp.getITurmaPersistente()
                .readByNameAndExecutionDegreeAndExecutionPeriod(newClassView.getNome(), executionDegree,
                        executionPeriod);

        if (otherClassWithSameNewName != null) {
            throw new ExistingServiceException("Duplicate Entry: " + otherClassWithSameNewName.getNome());
        }

        sp.getITurmaPersistente().simpleLockWrite(classToEdit);
        classToEdit.setNome(newClassView.getNome());

        return new Boolean(true);
    }

}