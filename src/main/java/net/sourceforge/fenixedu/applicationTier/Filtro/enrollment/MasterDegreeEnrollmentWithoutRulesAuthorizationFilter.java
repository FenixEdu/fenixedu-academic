/*
 * Created on 19/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author David Santos in Mar 1, 2004
 */

public class MasterDegreeEnrollmentWithoutRulesAuthorizationFilter extends Filtro {

    public static final MasterDegreeEnrollmentWithoutRulesAuthorizationFilter instance =
            new MasterDegreeEnrollmentWithoutRulesAuthorizationFilter();
    private static DegreeType DEGREE_TYPE = DegreeType.MASTER_DEGREE;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        return roles;
    }

    protected String hasPrevilege(User id, Object registration, DegreeType degreeType) {
        try {
            if (!verifyDegreeTypeIsMasterDegree(degreeType)) {
                return "error.degree.type";
            }

            if (!verifyStudentIsFromMasterDegree(registration)) {
                return "error.student.degree.nonMaster";
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyDegreeTypeIsMasterDegree(DegreeType degreeType) {
        boolean isNonMaster = false;

        if (degreeType != null) {
            isNonMaster = DEGREE_TYPE.equals(degreeType);
        }

        return isNonMaster;
    }

    private boolean verifyStudentIsFromMasterDegree(Object object) {
        DegreeType degreeType = null;
        if (object instanceof Registration) {
            Registration registration = (Registration) object;
            degreeType = registration.getDegreeType();
        }
        if (object instanceof StudentCurricularPlan) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;
            degreeType = studentCurricularPlan.getDegreeType();
        }

        return (degreeType != null && (degreeType.equals(DegreeType.MASTER_DEGREE) || degreeType
                .equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)));
    }

    public void execute(Object registration, DegreeType degreeType) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        String messageException = hasPrevilege(id, registration, degreeType);

        if ((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && messageException != null) || (id == null)
                || (id.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException(messageException);
        }
    }
}