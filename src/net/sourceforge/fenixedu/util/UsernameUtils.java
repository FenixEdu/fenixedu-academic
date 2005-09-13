/**
 * @author Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 */
package net.sourceforge.fenixedu.util;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * 
 * This class is responsible for handling all complex operations done to the
 * username
 * 
 * @author - Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 * 
 */
public class UsernameUtils<T> extends FenixUtil {

	/**
	 * This method is used to determine what should be the person's current
	 * username. Note - this method is NOT resposible for actually removing the
	 * role but or setting the new username.
	 * 
	 * @param person
	 *            person for whom the username is being determined
	 * @param rolesImportance
	 *            list of the precedency in roles regarding username.Note the
	 *            1st element is the most important role and the last is the
	 *            least important.
	 * @return a string representing what should be the person's username
	 */
	public static String updateUsername(IPerson person,
			List<RoleType> rolesImportance) {

		IRole mostImportantRole = getMostImportantRole(person.getPersonRoles(),
				rolesImportance);

		if (mostImportantRole == null) {
			return person.getUsername();
		}
		return generateNewUsername(person.getUsername(), mostImportantRole
				.getRoleType(), person);

	}

	private static String generateNewUsername(String oldUsername,
			RoleType roleType, IPerson person) {
		if (oldUsername.startsWith("INA")
				|| roleType.equals(RoleType.MASTER_DEGREE_CANDIDATE)) {
			return oldUsername;
		}

		char firstLetter = 'T';
		if (roleType.equals(RoleType.TEACHER)) {
			firstLetter = 'D';
		} else if (roleType.equals(RoleType.EMPLOYEE))
			firstLetter = 'F';
		else if (roleType.equals(RoleType.STUDENT)) {
			firstLetter = 'L';
			// Verify if it is a Master student
			for (IStudent student : person.getStudents()) {
				if (student.getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
					firstLetter = 'M';
					break;
				}
			}
		} else if (roleType.equals(RoleType.GRANT_OWNER))
			firstLetter = 'B';

		return oldUsername.replace(oldUsername.charAt(0), firstLetter);

	} /*
		 * Given a list of roles returns the most important role
		 */

	private static IRole getMostImportantRole(Collection<IRole> roles,
			List<RoleType> rolesImportance) {
		for (RoleType roleType : rolesImportance) {
			for (IRole role : roles) {
				if (role.getRoleType().equals(roleType)) {
					return role;
				}
			}
		}
		return null;
	}

}
