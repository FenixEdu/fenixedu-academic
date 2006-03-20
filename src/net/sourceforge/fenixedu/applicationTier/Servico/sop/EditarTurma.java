package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditarTurma extends Service {

    public void run(final InfoClass oldClassView, final InfoClass newClassView)
            throws FenixServiceException, ExcepcaoPersistencia {
        final SchoolClass classToEdit = (SchoolClass) persistentObject.readByOID(
                SchoolClass.class, oldClassView.getIdInternal());
        final ExecutionDegree executionDegree = classToEdit.getExecutionDegree();
        final ExecutionPeriod executionPeriod = classToEdit.getExecutionPeriod();

        final SchoolClass otherClassWithSameNewName = executionDegree.findSchoolClassesByExecutionPeriodAndName(executionPeriod, newClassView.getNome());

        if (otherClassWithSameNewName != null) {
            throw new ExistingServiceException("Duplicate Entry: " + otherClassWithSameNewName.getNome());
        }

        classToEdit.setNome(newClassView.getNome());
    }

}
