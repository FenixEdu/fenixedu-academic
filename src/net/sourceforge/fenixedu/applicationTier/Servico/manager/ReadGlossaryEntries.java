/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGlossaryEntries;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 */
public class ReadGlossaryEntries extends Service {

    public Collection run() throws ExcepcaoPersistencia {
        IPersistentGlossaryEntries dao = persistentSupport.getIPersistentGlossaryEntries();

        List glossaryEntries = dao.readAll();
        return CollectionUtils.collect(glossaryEntries, new Transformer() {
            public Object transform(Object arg0) {
                GlossaryEntry glossaryEntry = (GlossaryEntry) arg0;
                return InfoGlossaryEntry.newInfoFromDomain(glossaryEntry);
            }
        });
    }

}