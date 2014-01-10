/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditTeacherInformationAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final EditTeacherInformationAuthorizationFilter instance = new EditTeacherInformationAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(InfoServiceProviderRegime infoServiceProviderRegime, InfoWeeklyOcupation infoWeeklyOcupation,
            List<InfoOrientation> infoOrientations, List<InfoPublicationsNumber> infoPublicationsNumbers)
            throws NotAuthorizedException {
        User id = Authenticate.getUser();
        try {
            if (((id != null && id.getPerson().getPersonRolesSet() != null && !id.getPerson().hasRole(getRoleType())))
                    || (id == null) || (id.getPerson().getPersonRolesSet() == null)
                    || (!argumentsBelongToTeacher(id, infoServiceProviderRegime, infoWeeklyOcupation))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean argumentsBelongToTeacher(User id, InfoServiceProviderRegime infoServiceProviderRegime,
            InfoWeeklyOcupation infoWeeklyOcupation) {
        final Person person = id.getPerson();
        final Teacher teacher = person != null ? person.getTeacher() : null;
        String teacherId = teacher.getExternalId();

        if (!infoServiceProviderRegime.getInfoTeacher().getExternalId().equals(teacherId)) {
            return false;
        }

        if (!infoWeeklyOcupation.getInfoTeacher().getExternalId().equals(teacherId)) {
            return false;
        }
        return true;
    }
}