package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author nmgo
 */
public class EnrolmentImprovmentAuthorization extends AuthorizationByManyRolesFilter {

    private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        if (id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                || id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
            // verify if the user is employee
            if (!verifyEmployee(id)) {
                return "noAuthorization";
            }

            // verify if the student to enroll is a non master degree
            // student
            if (!verifyStudentType(arguments, DEGREE_TYPE)) {
                return "error.student.degree.nonMaster";
            }
        }

        return null;
    }

    private boolean verifyStudentType(Object[] arguments, DegreeType degreeType) {
        boolean isRightType = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = (Integer) arguments[0];
            if (studentNumber != null) {
                Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber,
                        degreeType);
                if (registration != null) {
                    isRightType = true; // right student curricular plan
                }
            }
        }

        return isRightType;
    }

    private boolean verifyEmployee(IUserView id) {
        return id != null && id.getPerson() != null && id.getPerson().getEmployee() != null;
    }

}
