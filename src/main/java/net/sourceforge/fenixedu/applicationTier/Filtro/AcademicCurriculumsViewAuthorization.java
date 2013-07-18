package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

public class AcademicCurriculumsViewAuthorization {

    public final static AcademicCurriculumsViewAuthorization instance = new AcademicCurriculumsViewAuthorization();

    final public void execute() throws NotAuthorizedException {
        if (!AcademicPredicates.MANAGE_MARKSHEETS.evaluate(AccessControl.getUserView().getPerson())) {
            throw new NotAuthorizedException();
        }
    }

}
