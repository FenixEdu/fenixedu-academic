/*
 * Created on May 2, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInstitution;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertInstitution implements IService {

    public InsertInstitution() {
    }

    public void run(String institutionName) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentInstitution persistentInstitution = sp.getIPersistentInstitution();
        List institutions = persistentInstitution.readByName(institutionName);

        if (institutions != null && institutions.size() > 0)
        {
            System.out.println("Ja existeeeeeeeeeeeeee :o");
            throw new ExistingServiceException(/* por aki o nome da label ? */);
        }

        IInstitution institution = new Institution();
        persistentInstitution.simpleLockWrite(institution);
        institution.setName(institutionName);

    }

}
