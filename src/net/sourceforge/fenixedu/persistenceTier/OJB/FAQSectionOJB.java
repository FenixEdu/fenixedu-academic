package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQSection;

import org.apache.ojb.broker.query.Criteria;

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