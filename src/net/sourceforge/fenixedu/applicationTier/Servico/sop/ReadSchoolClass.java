package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author João Mota
 *  
 */

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadSchoolClass extends Service {

	public InfoClass run(InfoClass infoSchoolClass) throws FenixServiceException, ExcepcaoPersistencia {

		InfoClass result = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();
		SchoolClass schoolClass = (SchoolClass) persistentSchoolClass.readByOID(SchoolClass.class,
				infoSchoolClass.getIdInternal());
		if (schoolClass != null) {
			result = InfoClass.newInfoFromDomain(schoolClass);
		}

		return result;
	}

}