/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.IGlossaryEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGlossaryEntries;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ReadGlossaryEntries implements IService {

    public Collection run() throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentGlossaryEntries dao = sp.getIPersistentGlossaryEntries();

        List glossaryEntries = dao.readAll();
        return CollectionUtils.collect(glossaryEntries, new Transformer() {
            public Object transform(Object arg0) {
                IGlossaryEntry glossaryEntry = (IGlossaryEntry) arg0;
                return InfoGlossaryEntry.newInfoFromDomain(glossaryEntry);
            }
        });
    }

}