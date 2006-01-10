package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AddSchoolClassesToShift implements IService {

	public void run(InfoShift infoShift, List<Integer> schoolClassOIDs) throws ExcepcaoPersistencia,
			FenixServiceException {

		final ISuportePersistente persistentSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

		final Shift shift = (Shift) persistentShift.readByOID(Shift.class, infoShift.getIdInternal());
		if (shift == null)
			throw new InvalidArgumentsServiceException();

		final ITurmaPersistente persistentSchoolClass = persistentSupport.getITurmaPersistente();
		for (final Integer schoolClassOID : schoolClassOIDs) {
			final SchoolClass schoolClass = (SchoolClass) persistentSchoolClass.readByOID(
					SchoolClass.class, schoolClassOID);
			if (schoolClass == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			shift.associateSchoolClass(schoolClass);
		}
	}

}
