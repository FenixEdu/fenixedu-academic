/*
 * Created on 03/12/2003
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantOwner extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	return InfoGrantOwnerWithPerson.newInfoFromDomain((GrantOwner) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
	return rootDomainObject.readGrantOwnerByOID(idInternal);
    }

}
