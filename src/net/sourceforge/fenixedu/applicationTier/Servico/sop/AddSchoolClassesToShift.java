package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddSchoolClassesToShift extends Service {

	public void run(InfoShift infoShift, List<Integer> schoolClassOIDs) throws ExcepcaoPersistencia,
			FenixServiceException {

		final Shift shift = (Shift) persistentObject.readByOID(Shift.class, infoShift.getIdInternal());
		if (shift == null)
			throw new InvalidArgumentsServiceException();

		for (final Integer schoolClassOID : schoolClassOIDs) {
			final SchoolClass schoolClass = (SchoolClass) persistentObject.readByOID(
					SchoolClass.class, schoolClassOID);
			if (schoolClass == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			shift.associateSchoolClass(schoolClass);
		}
	}

}
