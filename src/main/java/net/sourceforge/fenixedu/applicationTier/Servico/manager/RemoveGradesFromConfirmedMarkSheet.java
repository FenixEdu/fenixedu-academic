package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;
import pt.ist.fenixframework.Atomic;

public class RemoveGradesFromConfirmedMarkSheet {

    @Atomic
    public static void run(MarkSheet markSheet, List<EnrolmentEvaluation> evaluationsToRemove) {
        markSheet.removeGrades(evaluationsToRemove);
    }

}