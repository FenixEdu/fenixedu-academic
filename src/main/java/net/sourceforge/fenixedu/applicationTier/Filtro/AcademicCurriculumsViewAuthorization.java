package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

import org.fenixedu.bennu.core.security.Authenticate;

public class AcademicCurriculumsViewAuthorization {

    public final static AcademicCurriculumsViewAuthorization instance = new AcademicCurriculumsViewAuthorization();

    final public void execute() throws NotAuthorizedException {
        if (!AcademicPredicates.MANAGE_MARKSHEETS.evaluate(Authenticate.getUser().getPerson())) {
            throw new NotAuthorizedException();
        }
    }

}
