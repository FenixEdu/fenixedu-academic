/*
 * Created on 13/Abr/2005 - 16:20:23
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadInquiriesRegistriesByStudent implements IService {

    public List<InfoInquiriesRegistry> run(InfoStudent infoStudent) throws FenixServiceException,
            ExcepcaoPersistencia, NoSuchMethodException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        if (infoStudent == null) {
            throw new FenixServiceException("nullInfoStudent");
        }

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentInquiriesRegistry inquiriesRegistryDAO = sp.getIPersistentInquiriesRegistry();
        List<IInquiriesRegistry> inquiriesRegistries = inquiriesRegistryDAO.readByStudentId(infoStudent
                .getIdInternal());

        List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(
                inquiriesRegistries.size());

        for (IInquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
            infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
        }

        return infoInquiriesRegistries;
    }

}
