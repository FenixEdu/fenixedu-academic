/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author João Mota
 */
public class SummaryManagementToTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final SummaryManagementToTeacherAuthorizationFilter instance =
            new SummaryManagementToTeacherAuthorizationFilter();

    public SummaryManagementToTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(Summary summary, Professorship professorshipLogged) throws NotAuthorizedException {

        try {
            IUserView userViewLogged = AccessControl.getUserView();

            boolean executionCourseResponsibleLogged = professorshipLogged.isResponsibleFor();

            if (userViewLogged == null || userViewLogged.getRoleTypes() == null || professorshipLogged == null) {
                throw new NotAuthorizedException("error.summary.not.authorized");
            }
            if (executionCourseResponsibleLogged
                    && (summary.getProfessorship() != null && (!summary.getProfessorship().equals(professorshipLogged)))) {
                throw new NotAuthorizedException("error.summary.not.authorized");

            } else if (!executionCourseResponsibleLogged
                    && (summary.getProfessorship() == null || (!summary.getProfessorship().equals(professorshipLogged)))) {
                throw new NotAuthorizedException("error.summary.not.authorized");
            }

        } catch (RuntimeException ex) {
            throw new NotAuthorizedException("error.summary.not.authorized");
        }
    }

}
