/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditTeacherInformationAuthorizationFilter extends AuthorizationByRoleFilter {

    public EditTeacherInformationAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType())))
                    || (id == null)
                    || (id.getRoleTypes() == null)
                    || (!argumentsBelongToTeacher(id, (InfoServiceProviderRegime) arguments[0],
                            (InfoWeeklyOcupation) arguments[1]))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean argumentsBelongToTeacher(IUserView id,
            InfoServiceProviderRegime infoServiceProviderRegime, InfoWeeklyOcupation infoWeeklyOcupation) {
        try {
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
            Integer teacherId = teacher.getIdInternal();

            if (!infoServiceProviderRegime.getInfoTeacher().getIdInternal().equals(teacherId))
                return false;

            if (!infoWeeklyOcupation.getInfoTeacher().getIdInternal().equals(teacherId))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}