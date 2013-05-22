/*
 * Created on 5/Nov/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author João Mota
 * 
 */
public class CurrentDegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final CurrentDegreeCoordinatorAuthorizationFilter instance = new CurrentDegreeCoordinatorAuthorizationFilter();

    public CurrentDegreeCoordinatorAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(Integer infoExecutionDegreeId, Integer oldCurriculumId, Integer curricularCourseCode,
            InfoCurriculum newInfoCurriculum, String username, String language) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !isCoordinatorOfCurrentExecutionDegree(id, infoExecutionDegreeId)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isCoordinatorOfCurrentExecutionDegree(IUserView id, Integer infoExecutionDegreeId) {
        boolean result = false;
        if (infoExecutionDegreeId == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();

            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegreeId);
            ExecutionYear executionYear = executionDegree.getExecutionYear();

            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            result = (coordinator != null) && executionYear.isCurrent();

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}