package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadInquiriesRegistriesByStudent extends FenixService {

    @Service
    public static List<InfoInquiriesRegistry> run(Registration registration) throws FenixServiceException, NoSuchMethodException,
	    InvocationTargetException, NoSuchMethodException, IllegalAccessException {

	if (registration == null) {
	    throw new FenixServiceException("nullInfoStudent");
	}

	List<InquiriesRegistry> inquiriesRegistries = registration.getAssociatedInquiriesRegistries();

	List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(inquiriesRegistries.size());
	for (InquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
	    infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
	}

	return infoInquiriesRegistries;
    }

}