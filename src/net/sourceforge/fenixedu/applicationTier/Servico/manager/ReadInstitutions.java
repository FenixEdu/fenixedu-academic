/*
 * Created on May 2, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInstitution;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadInstitutions implements IService {

    public ReadInstitutions() {       
    }

    public List run() throws ExcepcaoPersistencia{
        
        IPersistentInstitution persistentInstitution = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentInstitution();
        List<Institution> institutions = persistentInstitution.readAll();
        List<InfoInstitution> infoInstitutions = new ArrayList();
        
        for (Institution institution : institutions) {
            InfoInstitution infoInstitution = new InfoInstitution();
            infoInstitution.copyFromDomain(institution);
            infoInstitutions.add(infoInstitution);
        }
        return infoInstitutions;
    }
}
