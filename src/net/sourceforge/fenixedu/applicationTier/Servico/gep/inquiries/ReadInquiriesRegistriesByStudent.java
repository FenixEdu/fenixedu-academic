package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadInquiriesRegistriesByStudent extends Service {

    public List<InfoInquiriesRegistry> run(InfoStudent infoStudent) throws FenixServiceException,
            ExcepcaoPersistencia, NoSuchMethodException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        if (infoStudent == null) {
            throw new FenixServiceException("nullInfoStudent");
        }

        Registration student = rootDomainObject.readRegistrationByOID(infoStudent.getIdInternal());
        List<InquiriesRegistry> inquiriesRegistries = student.getAssociatedInquiriesRegistries();

        List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(inquiriesRegistries.size());
        for (InquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
            infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
        }

        return infoInquiriesRegistries;
    }

}
