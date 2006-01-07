/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateGlossaryEntry implements IService {

    public void run(InfoGlossaryEntry infoGlossaryEntry) throws ExcepcaoPersistencia {
        GlossaryEntry glossaryEntry = DomainFactory.makeGlossaryEntry();
        glossaryEntry.setTerm(infoGlossaryEntry.getTerm());
        glossaryEntry.setDefinition(infoGlossaryEntry.getDefinition());
    }

}