package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQEntry;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class ReadFAQEntries {

    @Atomic
    public static Collection run() {
        List<InfoFAQEntry> result = new ArrayList<InfoFAQEntry>();

        for (FAQEntry faqEntry : Bennu.getInstance().getFAQEntrysSet()) {
            result.add(InfoFAQEntry.newInfoFromDomain(faqEntry));
        }

        return result;
    }

}