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
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditarTurma implements IService {

    public Object run(final InfoClass oldClassView, final InfoClass newClassView)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final SchoolClass classToEdit = (SchoolClass) sp.getITurmaPersistente().readByOID(
                SchoolClass.class, oldClassView.getIdInternal());
        final ExecutionPeriod executionPeriod = classToEdit.getExecutionPeriod();
        final ExecutionDegree executionDegree = classToEdit.getExecutionDegree();

        final SchoolClass otherClassWithSameNewName = sp.getITurmaPersistente()
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