package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class ReadFAQSections {

    @Atomic
    public static Collection run() {
        Collection<FAQSection> faqSections = Bennu.getInstance().getFAQSectionsSet();
        return CollectionUtils.collect(faqSections, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                FAQSection faqSection = (FAQSection) arg0;
                return InfoFAQSection.newInfoFromDomain(faqSection);
            }
        });
    }

}