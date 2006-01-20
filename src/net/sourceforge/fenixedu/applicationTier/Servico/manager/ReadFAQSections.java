/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQSection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 */
public class ReadFAQSections extends Service {

    public Collection run() throws ExcepcaoPersistencia {
        IPersistentFAQSection dao = persistentSupport.getIPersistentFAQSection();

        List faqSections = dao.readAll();
        return CollectionUtils.collect(faqSections, new Transformer() {
            public Object transform(Object arg0) {
                FAQSection faqSection = (FAQSection) arg0;
                return InfoFAQSection.newInfoFromDomain(faqSection);
            }
        });
    }

}