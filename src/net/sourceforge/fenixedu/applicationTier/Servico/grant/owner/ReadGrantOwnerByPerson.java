/*
 * Created on 12/12/2003
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantOwnerByPerson extends Service {

	public InfoGrantOwner run(Integer personId) throws FenixServiceException{

        InfoGrantOwner infoGrantOwner = null;
		Person person = (Person) rootDomainObject.readPartyByOID(personId);
		GrantOwner grantOwner = person.getGrantOwner();

		infoGrantOwner = InfoGrantOwnerWithPerson.newInfoFromDomain(grantOwner);
		return infoGrantOwner;
	}

}