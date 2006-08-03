package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class EnrollmentWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {

	private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;

	private static DegreeType MASTER_DEGREE_TYPE = DegreeType.MASTER_DEGREE;

	protected Collection getNeededRoles() {
		final List<InfoRole> roles = new ArrayList<InfoRole>();

		roles.add(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE));
		roles.add(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER));
		roles.add(new InfoRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE));

		return roles;
	}

	protected String hasPrevilege(IUserView id, Object[] arguments) {

		try {
			final List<RoleType> roles = getRoleList(id.getRoles());

			if (roles.contains(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {

				if (!checkDegreeType(arguments, MASTER_DEGREE_TYPE)) {
					return new String("error.masterDegree.type");
				}

				if (!checkStudentType(arguments, MASTER_DEGREE_TYPE)) {
					return new String("error.student.degree.master");
				}
			}

			if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
					|| roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {

				if (!checkDegreeType(arguments, DEGREE_TYPE)) {
					return new String("error.degree.type");
				}

				if (!checkStudentType(arguments, DEGREE_TYPE)) {
					return new String("error.student.degree.nonMaster");
				}
			}
			return null;

		} catch (Exception exception) {
			exception.printStackTrace();
			return "noAuthorization";
		}
	}

	private boolean checkDegreeType(Object[] args, DegreeType degreeType) {
		return (args != null && args[1] != null && degreeType.equals(args[1]));
	}

	private boolean checkStudentType(Object[] args, DegreeType degreeType) {
		return (args != null && args[0] != null) ? ((Student) args[0]).getDegreeType().equals(degreeType) : false;
	}
}