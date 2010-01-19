/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryTeachersResById extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static InfoOldInquiriesTeachersRes run(Integer internalId) throws FenixServiceException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	InfoOldInquiriesTeachersRes oldInquiriesTeachersRes = null;

	OldInquiriesTeachersRes oits = rootDomainObject.readOldInquiriesTeachersResByOID(internalId);
	if (oits == null) {
	    throw new FenixServiceException("nullInternalId");
	}

	oldInquiriesTeachersRes = new InfoOldInquiriesTeachersRes();
	oldInquiriesTeachersRes.copyFromDomain(oits);

	return oldInquiriesTeachersRes;
    }

}