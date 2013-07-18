package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import pt.ist.fenixWebFramework.services.Service;

public class ReadGlossaryEntries {

    @Service
    public static List<InfoGlossaryEntry> run() {
        List<InfoGlossaryEntry> result = new ArrayList<InfoGlossaryEntry>();

        for (GlossaryEntry glossaryEntry : RootDomainObject.getInstance().getGlossaryEntrys()) {
            result.add(InfoGlossaryEntry.newInfoFromDomain(glossaryEntry));
        }

        return result;
    }

}