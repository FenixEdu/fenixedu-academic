package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

public class EnrollmentWithoutRulesAuthorizationFilter extends Filtro {

    public static final EnrollmentWithoutRulesAuthorizationFilter instance = new EnrollmentWithoutRulesAuthorizationFilter();

    private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;

    private static DegreeType MASTER_DEGREE_TYPE = DegreeType.MASTER_DEGREE;

    private static DegreeType DFA_TYPE = DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        final List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        return roles;
    }

    protected String hasPrevilege(User id, Object registration, DegreeType degreeType) {
        if (id.getPerson().hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {

            if (!(checkDegreeType(degreeType, MASTER_DEGREE_TYPE) || checkDegreeType(degreeType, DFA_TYPE))) {
                return "error.masterDegree.type";
            }

            if (!(checkStudentType(registration, MASTER_DEGREE_TYPE) || checkStudentType(registration, DFA_TYPE))) {
                return "error.student.degree.master";
            }
        }

        if (id.getPerson().hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                || id.getPerson().hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {

            if (!checkDegreeType(degreeType, DEGREE_TYPE)) {
                return "error.degree.type";
            }

            if (!checkStudentType(registration, DEGREE_TYPE)) {
                return "error.student.degree.nonMaster";
            }
        }
        return null;
    }

    private boolean checkDegreeType(DegreeType toCheck, DegreeType degreeType) {
        return (toCheck != null && degreeType.equals(toCheck));
    }

    private boolean checkStudentType(Object object, DegreeType degreeType) {
        DegreeType type = null;
        if (object instanceof Registration) {
            Registration registration = (Registration) object;
            type = registration.getDegreeType();
        }
        if (object instanceof StudentCurricularPlan) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;
            type = studentCurricularPlan.getDegreeType();
        }

        return type != null ? type.equals(degreeType) : false;
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