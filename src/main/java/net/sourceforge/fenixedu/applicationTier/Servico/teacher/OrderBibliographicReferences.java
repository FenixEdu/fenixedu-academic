package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * Changes the presentation order of all the bibliographic references passed to
 * match the order they have in the given list.
 * 
 * @author cfgi
 */
public class OrderBibliographicReferences {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static void run(ExecutionCourse executionCourse, List<BibliographicReference> references) {
        executionCourse.setBibliographicReferencesOrder(references);
    }
}