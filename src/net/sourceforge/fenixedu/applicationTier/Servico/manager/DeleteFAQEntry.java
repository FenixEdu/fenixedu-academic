/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class DeleteFAQEntry extends Service {

    public void run(Integer entryId) {
        rootDomainObject.readFAQEntryByOID(entryId).delete();
    }

}
