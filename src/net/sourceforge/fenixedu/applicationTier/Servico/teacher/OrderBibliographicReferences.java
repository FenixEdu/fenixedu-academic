package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * Changes the presentation order of all the bibliographic references passed to match 
 * the order they have in the given list.
 * 
 * @author cfgi
 */
public class OrderBibliographicReferences extends Service {

    public void run(ExecutionCourse executionCourse, List<BibliographicReference> references) {
        executionCourse.setBibliographicReferencesOrder(references);
    }
}
