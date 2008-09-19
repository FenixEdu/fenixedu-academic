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

public class RemoveClasses extends Service {

    public Boolean run(InfoShift infoShift, List classOIDs) {
	final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());

	for (int i = 0; i < classOIDs.size(); i++) {
	    final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID((Integer) classOIDs.get(i));
	    shift.getAssociatedClasses().remove(schoolClass);
	    schoolClass.getAssociatedShifts().remove(shift);
	}
	return Boolean.TRUE;
    }

}