package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteExemption {

    @Atomic
    public static void run(final Exemption exemption) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        exemption.delete();
    }

}