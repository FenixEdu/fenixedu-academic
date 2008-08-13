package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

public class AddSchoolClassesToShift extends Service {

    public void run(InfoShift infoShift, List<Integer> schoolClassOIDs) throws FenixServiceException {

	final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());
	if (shift == null)
	    throw new InvalidArgumentsServiceException();

	for (final Integer schoolClassOID : schoolClassOIDs) {
	    final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(schoolClassOID);
	    if (schoolClass == null) {
		throw new InvalidArgumentsServiceException();
	    }

	    shift.associateSchoolClass(schoolClass);
	}
    }

}
