/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveClasses extends Service {

    public Boolean run(InfoShift infoShift, List classOIDs) {

        boolean result = false;

        Shift shift = rootDomainObject.readShiftByOID(
                infoShift.getIdInternal());

        for (int i = 0; i < classOIDs.size(); i++) {
            SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(
                    (Integer) classOIDs.get(i));

            shift.getAssociatedClasses().remove(schoolClass);

            schoolClass.getAssociatedShifts().remove(shift);
        }

        result = true;

        return new Boolean(result);
    }

}