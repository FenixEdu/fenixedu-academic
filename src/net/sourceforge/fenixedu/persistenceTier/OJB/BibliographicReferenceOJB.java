package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author EP 15
 * @author João Mota
 */
public class BibliographicReferenceOJB extends PersistentObjectOJB implements
        IPersistentBibliographicReference {

    public void delete(IBibliographicReference bibliographicReference) throws ExcepcaoPersistencia {
        super.delete(bibliographicReference);
    }

    public IBibliographicReference readBibliographicReference(IExecutionCourse executionCourse,
            String title, String authors, String reference, String year) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());
        crit.addEqualTo("title", title);
        crit.addEqualTo("authors", authors);
        crit.addEqualTo("reference", reference);
        crit.addEqualTo("year", year);
        return (IBibliographicReference) queryObject(BibliographicReference.class, crit);

    }

    public List readBibliographicReference(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourse.getIdInternal());
        List result = queryList(BibliographicReference.class, crit);
        return result;

    }
}