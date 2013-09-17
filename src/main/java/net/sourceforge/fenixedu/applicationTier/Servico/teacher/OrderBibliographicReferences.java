package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * Changes the presentation order of all the bibliographic references passed to
 * match the order they have in the given list.
 * 
 * @author cfgi
 */
public class OrderBibliographicReferences {

    @Atomic
    public static void run(ExecutionCourse executionCourse, List<BibliographicReference> references) {
        check(RolePredicates.TEACHER_PREDICATE);
        executionCourse.setBibliographicReferencesOrder(references);
    }
}