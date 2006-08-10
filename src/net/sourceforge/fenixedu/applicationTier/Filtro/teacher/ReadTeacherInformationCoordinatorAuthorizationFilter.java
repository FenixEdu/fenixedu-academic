/*
 * Created on Dec 16, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest arg0, ServiceResponse arg1) throws FilterException, Exception {
        IUserView id = (IUserView) arg0.getRequester();
        Object[] arguments = arg0.getArguments();
        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType())))
                || (id == null)
                || (id.getRoleTypes() == null)
                || !verifyCondition(id, (String) arguments[0])) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected boolean verifyCondition(IUserView id, String user) {
        try {
            Teacher teacher = Teacher.readTeacherByUsername(user);
            Teacher coordinator = Teacher.readTeacherByUsername(id.getUtilizador());

            List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllCoordinatedByTeacher(coordinator);
            
            List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
            ExecutionYear executionDegressExecutionYearID = (!degreeCurricularPlans.isEmpty()) ? executionDegrees
                    .get(0).getExecutionYear()
                    : null;

            List professorships = Professorship.readByDegreeCurricularPlansAndExecutionYear(
                    degreeCurricularPlans, executionDegressExecutionYearID);
            Iterator iter = professorships.iterator();
            while (iter.hasNext()) {
                Professorship professorship = (Professorship) iter.next();
                if (professorship.getTeacher().equals(teacher))
                    return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
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