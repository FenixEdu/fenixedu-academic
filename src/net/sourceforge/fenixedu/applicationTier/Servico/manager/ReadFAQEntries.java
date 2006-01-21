/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 */
public class ReadFAQEntries extends Service {

    public Collection run() throws ExcepcaoPersistencia {
        List faqEntries = (List) persistentObject.readAll(FAQEntry.class);
        return CollectionUtils.collect(faqEntries, new Transformer() {
            public Object transform(Object arg0) {
                FAQEntry faqEntry = (FAQEntry) arg0;
                return InfoFAQEntry.newInfoFromDomain(faqEntry);
            }
        });
    }

}