/*
 * Created on Dec 16, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadTeacherInformationCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ReadTeacherInformationCoordinatorAuthorizationFilter instance = new ReadTeacherInformationCoordinatorAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(String user, String argExecutionYear) throws FilterException, Exception {
        IUserView id = AccessControl.getUserView();
        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType()))) || (id == null)
                || (id.getRoleTypes() == null) || !verifyCondition(id, user)) {
            throw new NotAuthorizedException();
        }
    }

    protected boolean verifyCondition(IUserView id, String user) {
        final Person person = id.getPerson();

        List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllCoordinatedByTeacher(person);

        List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
        ExecutionYear executionDegressExecutionYearID =
                (!degreeCurricularPlans.isEmpty()) ? executionDegrees.get(0).getExecutionYear() : null;

        List<Professorship> professorships =
                Professorship.readByDegreeCurricularPlansAndExecutionYear(degreeCurricularPlans, executionDegressExecutionYearID);
        for (final Professorship professorship : professorships) {
            if (professorship.getPerson() == person) {
                return true;
            }
        }
        return false;
    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans(final List<ExecutionDegree> executionDegrees) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan());
        }
        return result;
    }
}