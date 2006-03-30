package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteManagementPositionCreditLineService extends Service {

    public void run(Integer managementPositionCreditLineID) throws ExcepcaoPersistencia, FenixServiceException {
        ManagementPositionCreditLine managementPositionCreditLine = (ManagementPositionCreditLine) persistentObject.readByOID(
                ManagementPositionCreditLine.class, managementPositionCreditLineID);
        if (managementPositionCreditLine == null) {
            throw new FenixServiceException("error.noManagementPositionCreditLine");
        }
        managementPositionCreditLine.delete();
    }

}
