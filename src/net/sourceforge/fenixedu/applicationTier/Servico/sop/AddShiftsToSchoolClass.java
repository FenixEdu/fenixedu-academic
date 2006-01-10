package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AddShiftsToSchoolClass implements IService {

	public void run(InfoClass infoClass, List<String> shiftOIDs) throws FenixServiceException,
			ExcepcaoPersistencia {

		final ISuportePersistente persistentSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		final ITurmaPersistente persistentSchoolClass = persistentSupport.getITurmaPersistente();

		final SchoolClass schoolClass = (SchoolClass) persistentSchoolClass.readByOID(
				SchoolClass.class, infoClass.getIdInternal());
		if (schoolClass == null) {
			throw new InvalidArgumentsServiceException();
		}

		final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
		for (final String shiftOID : shiftOIDs) {
			final Shift shift = (Shift) persistentShift.readByOID(Shift.class, new Integer(shiftOID));
			if (shift == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			schoolClass.associateShift(shift);
		}
	}

}
