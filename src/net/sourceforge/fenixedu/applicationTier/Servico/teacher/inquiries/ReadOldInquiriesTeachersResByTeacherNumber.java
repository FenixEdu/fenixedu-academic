/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiriesTeachersResByTeacherNumber implements IService {

	public List run(Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {
		List oldInquiriesTeachersResList = null;

		if (teacherNumber == null) {
			throw new FenixServiceException("nullTeacherNumber");
		}
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();

		oldInquiriesTeachersResList = poits.readByTeacherNumber(teacherNumber);

		CollectionUtils.transform(oldInquiriesTeachersResList, new Transformer() {

			public Object transform(Object oldInquiriesTeachersRes) {
				InfoOldInquiriesTeachersRes ioits = new InfoOldInquiriesTeachersRes();
				try {
					ioits.copyFromDomain((OldInquiriesTeachersRes) oldInquiriesTeachersRes);

				} catch (Exception ex) {
				}

				return ioits;
			}
		});

		return oldInquiriesTeachersResList;
	}

}
