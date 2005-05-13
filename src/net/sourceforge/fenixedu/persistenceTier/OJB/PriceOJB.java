package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.IPrice;
import net.sourceforge.fenixedu.domain.Price;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrice;
import net.sourceforge.fenixedu.domain.GraduationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PriceOJB extends PersistentObjectOJB implements IPersistentPrice {

    public PriceOJB() {
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Price.class, new Criteria());
    }

    public List readByGraduationType(GraduationType graduationType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("graduationType", graduationType.name());
        return queryList(Price.class, crit);

    }

    public IPrice readByGraduationTypeAndDocumentTypeAndDescription(GraduationType graduationType,
            DocumentType documentType, String description) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("graduationType", graduationType.name());
        crit.addEqualTo("documentType", documentType.name());
        crit.addEqualTo("description", description);
        return (IPrice) queryObject(Price.class, crit);

    }

    public List readByGraduationTypeAndDocumentType(GraduationType graduationType,
            DocumentType documentType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("graduationType", graduationType.name());
        crit.addEqualTo("documentType", documentType.name());

        return queryList(Price.class, crit);

    }

    public List readByGraduationTypeAndDocumentType(GraduationType graduationType, List types)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        Criteria criteriaDocs = new Criteria();
        criteria.addEqualTo("graduationType", graduationType.name());
        
        CollectionUtils.transform(types, new Transformer() {        
            public Object transform(Object arg0) {                
                return ((DocumentType)arg0).name();
            }        
        });
        
        criteriaDocs.addIn("documentType", types);
        criteria.addAndCriteria(criteriaDocs);
        List result = queryList(Price.class, criteria);
        return result;

    }
}