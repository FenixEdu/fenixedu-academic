package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class RemoveGradesFromConfirmedMarkSheet extends FenixService {

    public void run(MarkSheet markSheet, List<EnrolmentEvaluation> evaluationsToRemove) {
	markSheet.removeGrades(evaluationsToRemove);
    }

}
