/*
 * Created on 12/12/2003
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantOwnerByPerson extends Service {

	public InfoGrantOwner run(Integer personId) throws FenixServiceException, ExcepcaoPersistencia {
		InfoGrantOwner infoGrantOwner = null;
		IPersistentGrantOwner persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();
		GrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(personId);

		infoGrantOwner = InfoGrantOwnerWithPerson.newInfoFromDomain(grantOwner);
		return infoGrantOwner;
	}

}