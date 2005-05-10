/*
 * Created on 13/Abr/2005 - 16:20:23
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadInquiriesRegistriesByStudent implements IServico {

	private static ReadInquiriesRegistriesByStudent service = new ReadInquiriesRegistriesByStudent();
	
	public ReadInquiriesRegistriesByStudent() {
	}

	public String getNome() {
		return "inquiries.ReadInquiriesRegistriesByStudent";
	}

    /**
     * @return Returns the service.
     */
    public static ReadInquiriesRegistriesByStudent getService() {
        return service;
    }
    
	public List<InfoInquiriesRegistry> run(InfoStudent infoStudent) throws FenixServiceException {
		
		if(infoStudent == null) {
			throw new FenixServiceException("nullInfoStudent");
		}
		
		try {
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
			
			IPersistentInquiriesRegistry inquiriesRegistryDAO = sp.getIPersistentInquiriesRegistry();
			List<IInquiriesRegistry> inquiriesRegistries = inquiriesRegistryDAO.readByStudentId(infoStudent.getIdInternal());
			
			List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>();
			
			for(IInquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
				infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
			}
			
			return infoInquiriesRegistries;
			

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		
	}
	
}
