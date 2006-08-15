package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditExecutionDegree extends Service {

    public void run(InfoExecutionDegreeEditor infoExecutionDegree) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ExecutionDegree oldExecutionDegree = rootDomainObject.readExecutionDegreeByOID(
                        infoExecutionDegree.getIdInternal());
        if (oldExecutionDegree == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
        }

        final Campus campus = rootDomainObject.readCampusByOID(
                infoExecutionDegree.getInfoCampus().getIdInternal());
        if (campus == null) {
            throw new NonExistingServiceException("message.nonExistingCampus", null);
        }

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(
                        infoExecutionDegree.getInfoExecutionYear().getIdInternal());
        if (executionYear == null) {
            throw new NonExistingServiceException("message.non.existing.execution.year", null);
        }

        oldExecutionDegree.setCampus(campus);
        oldExecutionDegree.setExecutionYear(executionYear);
        oldExecutionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
    }

}
