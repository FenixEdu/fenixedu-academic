package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.support.FAQEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFAQEntries;

/**
 * Created on 2003/08/30
 * 
 * @author Luis Cruz
 */
public class FAQEntriesOJB extends PersistentObjectOJB implements IPersistentFAQEntries {

    public List readAll() throws ExcepcaoPersistencia {
        return readByCriteria(FAQEntry.class, null);
    }

    public List readEntriesInSection(Integer sectionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (sectionId != null) {
            criteria.addEqualTo("parentSection.idInternal", sectionId);
        } else {
            criteria.addIsNull("parentSection.idInternal");
        }
        return readByCriteria(FAQEntry.class, criteria);
    }

}