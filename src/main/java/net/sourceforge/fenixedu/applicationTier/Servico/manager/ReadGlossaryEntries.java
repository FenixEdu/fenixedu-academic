package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class ReadGlossaryEntries {

    @Atomic
    public static List<InfoGlossaryEntry> run() {
        List<InfoGlossaryEntry> result = new ArrayList<InfoGlossaryEntry>();

        for (GlossaryEntry glossaryEntry : Bennu.getInstance().getGlossaryEntrysSet()) {
            result.add(InfoGlossaryEntry.newInfoFromDomain(glossaryEntry));
        }

        return result;
    }

}