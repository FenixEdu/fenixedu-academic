package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.support.FAQSection;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFAQSection;

/**
 * Created on 2003/08/30
 * 
 * @author Luis Cruz
 */
public class FAQSectionOJB extends PersistentObjectOJB implements IPersistentFAQSection {

    public List readAll() throws ExcepcaoPersistencia {
        return readByCriteria(FAQSection.class, null);
    }

    public List readSubSectionsInSection(Integer sectionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (sectionId != null) {
            criteria.addEqualTo("parentSection.idInternal", sectionId);
        } else {
            criteria.addIsNull("parentSection.idInternal");
        }
        return readByCriteria(FAQSection.class, criteria);
    }

}