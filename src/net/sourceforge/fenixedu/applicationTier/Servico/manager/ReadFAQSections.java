package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadFAQSections extends Service {

    public Collection run() {
        List<FAQSection> faqSections = rootDomainObject.getFAQSections();
        return CollectionUtils.collect(faqSections, new Transformer() {
            public Object transform(Object arg0) {
                FAQSection faqSection = (FAQSection) arg0;
                return InfoFAQSection.newInfoFromDomain(faqSection);
            }
        });
    }

}
