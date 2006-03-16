package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadGlossaryEntries extends Service {

    public List<InfoGlossaryEntry> run() throws ExcepcaoPersistencia {
        List<InfoGlossaryEntry> result = new ArrayList<InfoGlossaryEntry>();
        
        List<GlossaryEntry> glossaryEntries = (List<GlossaryEntry>) persistentObject.readAll(GlossaryEntry.class);
        for (GlossaryEntry glossaryEntry : glossaryEntries) {
            result.add(InfoGlossaryEntry.newInfoFromDomain(glossaryEntry));
        }
        
        return result;
    }

}
