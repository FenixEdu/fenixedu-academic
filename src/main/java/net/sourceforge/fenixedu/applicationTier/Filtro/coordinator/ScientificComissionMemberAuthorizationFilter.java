package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ScientificComissionMemberAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ScientificComissionMemberAuthorizationFilter instance =
            new ScientificComissionMemberAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis, Integer mark) throws NotAuthorizedException {
        super.execute();

        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        if (!degreeCurricularPlan.getDegree().isMemberOfCurrentScientificCommission(person)) {
            throw new NotAuthorizedException("degree.scientificCommission.notMember");
        }
    }

}
