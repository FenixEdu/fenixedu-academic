package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

public class RemoveShifts extends Service {

    public Boolean run(final InfoClass infoClass, final List shiftOIDs) {
	final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(infoClass.getIdInternal());
	final List<Shift> shifts = schoolClass.getAssociatedShifts();

	for (int i = 0; i < shifts.size(); i++) {
	    final Shift shift = shifts.get(i);
	    if (shiftOIDs.contains(shift.getIdInternal())) {

		// TODO: temporary ------------------------------------
		shift.checkXpto();
		// TODO: temporary ------------------------------------

		shifts.remove(shift);
		i--;
	    }
	}

	return Boolean.TRUE;
    }

}