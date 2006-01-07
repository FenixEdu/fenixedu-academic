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
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantOwner extends ReadDomainObjectService implements IService {

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantOwner();
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantOwnerWithPerson.newInfoFromDomain((GrantOwner) domainObject);
    }

    protected Class getDomainObjectClass() {
        return GrantOwner.class;
    }

}
