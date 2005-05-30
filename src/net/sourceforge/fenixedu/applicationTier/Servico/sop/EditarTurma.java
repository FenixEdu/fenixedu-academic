/*
 * EditarTurma.java Created on 27 de Outubro de 2002, 20:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço EditarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho e Luis Cruz
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditarTurma implements IService {

    public Object run(final InfoClass oldClassView, final InfoClass newClassView)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(oldClassView.getInfoExecutionPeriod());

        final IExecutionDegree executionDegree = Cloner
                .copyInfoExecutionDegree2ExecutionDegree(oldClassView.getInfoExecutionDegree());

        final ISchoolClass classToEdit = sp.getITurmaPersistente()
                .readByNameAndExecutionDegreeAndExecutionPeriod(oldClassView.getNome(),
                        executionDegree.getIdInternal(), executionPeriod.getIdInternal());

        final ISchoolClass otherClassWithSameNewName = sp.getITurmaPersistente()
                .readByNameAndExecutionDegreeAndExecutionPeriod(newClassView.getNome(),
                        executionDegree.getIdInternal(), executionPeriod.getIdInternal());

        if (otherClassWithSameNewName != null) {
            throw new ExistingServiceException("Duplicate Entry: " + otherClassWithSameNewName.getNome());
        }

        sp.getITurmaPersistente().simpleLockWrite(classToEdit);
        classToEdit.setNome(newClassView.getNome());

        return new Boolean(true);
    }

}