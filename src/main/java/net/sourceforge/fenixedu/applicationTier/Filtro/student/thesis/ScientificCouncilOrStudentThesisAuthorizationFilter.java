package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

public class ScientificCouncilOrStudentThesisAuthorizationFilter extends StudentThesisAuthorizationFilter {

    public static final ScientificCouncilOrStudentThesisAuthorizationFilter instance =
            new ScientificCouncilOrStudentThesisAuthorizationFilter();

    @Override
    public void execute(Thesis thesis) throws NotAuthorizedException {
        final User userView = Authenticate.getUser();
        if (!userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
            super.execute(thesis);
        }
    }

}
