/*
 * Created on 12/12/2003
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantOwnerByPerson implements IService {

    public ReadGrantOwnerByPerson() {
    }

    public InfoGrantOwner run(Integer personId) throws FenixServiceException {
        InfoGrantOwner infoGrantOwner = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantOwner persistentGrantOwner = sp.getIPersistentGrantOwner();
            IGrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(personId);

            infoGrantOwner = InfoGrantOwnerWithPerson.newInfoFromDomain(grantOwner);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        return infoGrantOwner;
    }

}