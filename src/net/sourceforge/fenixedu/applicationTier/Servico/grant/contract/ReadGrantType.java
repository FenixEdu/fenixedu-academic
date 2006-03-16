/*
 * Created on 22/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantType extends ReadDomainObjectService {
    
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantType.newInfoFromDomain((GrantType) domainObject);
    }

    protected Class getDomainObjectClass() {
        return GrantType.class;
    }

}
