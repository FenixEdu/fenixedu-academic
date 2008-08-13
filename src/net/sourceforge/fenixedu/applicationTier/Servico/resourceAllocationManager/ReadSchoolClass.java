package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * @author João Mota
 *  
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadSchoolClass extends Service {

	public InfoClass run(InfoClass infoSchoolClass) throws FenixServiceException{
		InfoClass result = null;
		SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(
				infoSchoolClass.getIdInternal());
		if (schoolClass != null) {
			result = InfoClass.newInfoFromDomain(schoolClass);
		}

		return result;
	}

}