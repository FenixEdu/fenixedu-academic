package ServidorPersistente.OJB;

import java.util.List;

import Dominio.support.GlossaryEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGlossaryEntries;

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