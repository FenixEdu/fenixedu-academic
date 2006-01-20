/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryTeachersResById extends Service {

	public InfoOldInquiriesTeachersRes run(Integer internalId) throws FenixServiceException, ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		InfoOldInquiriesTeachersRes oldInquiriesTeachersRes = null;

		if (internalId == null) {
			throw new FenixServiceException("nullInternalId");
		}
		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentOldInquiriesTeachersRes poits = persistentSupport.getIPersistentOldInquiriesTeachersRes();

		OldInquiriesTeachersRes oits = poits.readByInternalId(internalId);

		oldInquiriesTeachersRes = new InfoOldInquiriesTeachersRes();
		oldInquiriesTeachersRes.copyFromDomain(oits);

		return oldInquiriesTeachersRes;
	}

}
