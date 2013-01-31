package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

public class ReadFAQSections extends FenixService {

	@Service
	public static Collection run() {
		List<FAQSection> faqSections = rootDomainObject.getFAQSections();
		return CollectionUtils.collect(faqSections, new Transformer() {
			@Override
			public Object transform(Object arg0) {
				FAQSection faqSection = (FAQSection) arg0;
				return InfoFAQSection.newInfoFromDomain(faqSection);
			}
		});
	}

}