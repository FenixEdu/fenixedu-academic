package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGlossaryEntries;

/**
 * Created on 2003/08/30
 * 
 * @author Luis Cruz
 */
public class GlossaryEntriesOJB extends PersistentObjectOJB implements IPersistentGlossaryEntries {

    public List readAll() throws ExcepcaoPersistencia {
        return readByCriteria(GlossaryEntry.class, null);
    }

}