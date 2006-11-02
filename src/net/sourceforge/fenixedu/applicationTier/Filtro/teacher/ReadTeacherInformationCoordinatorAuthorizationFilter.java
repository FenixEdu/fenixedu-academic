/*
 * Created on Dec 16, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadTeacherInformationCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(ServiceRequest arg0, ServiceResponse arg1) throws FilterException, Exception {
        IUserView id = (IUserView) arg0.getRequester();
        Object[] arguments = arg0.getArguments();
        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType())))
                || (id == null) || (id.getRoleTypes() == null)
                || !verifyCondition(id, (String) arguments[0])) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected boolean verifyCondition(IUserView id, String user) {
        final Person person = id.getPerson();
        final Teacher teacher = Teacher.readTeacherByUsername(user);

        List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllCoordinatedByTeacher(person);

        List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
        ExecutionYear executionDegressExecutionYearID = (!degreeCurricularPlans.isEmpty()) ? executionDegrees
                .get(0).getExecutionYear()
                : null;

        List<Professorship> professorships = Professorship.readByDegreeCurricularPlansAndExecutionYear(
                degreeCurricularPlans, executionDegressExecutionYearID);
        for (final Professorship professorship : professorships) {
            if (professorship.getTeacher() == teacher) {
                return true;
            }
        }
        return false;
    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans(
            final List<ExecutionDegree> executionDegrees) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan());
        }
        return result;
    }
}