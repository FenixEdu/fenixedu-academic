package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrollmentWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {

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

    protected String hasPrevilege(IUserView id, Object[] arguments) {
	if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {

	    if (!(checkDegreeType(arguments, MASTER_DEGREE_TYPE) || checkDegreeType(arguments, DFA_TYPE))) {
		return new String("error.masterDegree.type");
	    }

	    if (!(checkStudentType(arguments, MASTER_DEGREE_TYPE) || checkStudentType(arguments,
		    DFA_TYPE))) {
		return new String("error.student.degree.master");
	    }
	}

	if (id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
		|| id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {

	    if (!checkDegreeType(arguments, DEGREE_TYPE)) {
		return new String("error.degree.type");
	    }

	    if (!checkStudentType(arguments, DEGREE_TYPE)) {
		return new String("error.student.degree.nonMaster");
	    }
	}
	return null;
    }

    private boolean checkDegreeType(Object[] args, DegreeType degreeType) {
	return (args != null && args[1] != null && degreeType.equals(args[1]));
    }

    private boolean checkStudentType(Object[] args, DegreeType degreeType) {
	Object object = args[0];
	DegreeType type = null;
	if(object instanceof Registration) {
	    Registration registration = (Registration) object;
	    type = registration.getDegreeType();
	}
	if(object instanceof StudentCurricularPlan) {
	    StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;
	    type = studentCurricularPlan.getDegreeType();
	}
	
	return type != null ? type.equals(
		degreeType) : false;
    }
}