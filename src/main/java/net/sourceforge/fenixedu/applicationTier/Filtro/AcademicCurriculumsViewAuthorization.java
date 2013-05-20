package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

public class AcademicCurriculumsViewAuthorization {

    public final static AcademicCurriculumsViewAuthorization instance = new AcademicCurriculumsViewAuthorization();

    final public void execute(Integer degreeCurricularPlanID, AcademicInterval academicInterval) throws FilterException,
            Exception {
        if (!AcademicPredicates.MANAGE_MARKSHEETS.evaluate(AccessControl.getUserView().getPerson())) {
            throw new NotAuthorizedFilterException();
        }
    }

}
