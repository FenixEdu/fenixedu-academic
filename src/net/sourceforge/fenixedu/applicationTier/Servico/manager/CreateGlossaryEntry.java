/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class CreateGlossaryEntry extends Service {

    public void run(InfoGlossaryEntry infoGlossaryEntry) throws ExcepcaoPersistencia {
        GlossaryEntry glossaryEntry = new GlossaryEntry();
        glossaryEntry.setTerm(infoGlossaryEntry.getTerm());
        glossaryEntry.setDefinition(infoGlossaryEntry.getDefinition());
    }

}