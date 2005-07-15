/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadOldPublication extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return OldPublication.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentOldPublication();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoOldPublication.newInfoFromDomain((IOldPublication) domainObject);
    }

}
