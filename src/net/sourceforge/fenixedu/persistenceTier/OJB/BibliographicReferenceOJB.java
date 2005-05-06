package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author EP 15
 * @author João Mota
 */
public class BibliographicReferenceOJB extends PersistentObjectOJB implements
        IPersistentBibliographicReference {

    public List readBibliographicReference(Integer executionCourseOID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourseOID);
        List result = queryList(BibliographicReference.class, crit);
        return result;
    }
}