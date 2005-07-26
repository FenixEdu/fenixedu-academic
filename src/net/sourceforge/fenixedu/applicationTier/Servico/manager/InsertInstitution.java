/*
 * Created on May 2, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertInstitution implements IService {

    public void run(String institutionName) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List institutions = sp.getIPersistentInstitution().readByName(institutionName);

        if (institutions != null && institutions.size() > 0) {
            // FIXME 
            System.out.println("Ja existeeeeeeeeeeeeee :o");
            throw new ExistingServiceException(/* por aki o nome da label ? */);
        }

        IInstitution institution = DomainFactory.makeInstitution();
        institution.setName(institutionName);
    }

}
