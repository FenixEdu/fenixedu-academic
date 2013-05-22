package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ScientificCouncilOrStudentThesisAuthorizationFilter extends StudentThesisAuthorizationFilter {

    public static final ScientificCouncilOrStudentThesisAuthorizationFilter instance =
            new ScientificCouncilOrStudentThesisAuthorizationFilter();

    @Override
    public void execute(Thesis thesis) throws NotAuthorizedException {
        final IUserView userView = AccessControl.getUserView();
        if (!userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
            super.execute(thesis);
        }
    }

}
