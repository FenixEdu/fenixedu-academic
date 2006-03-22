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
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

/**
 * @author nmgo
 */
public class EnrolmentImprovmentAuthorization extends AuthorizationByManyRolesFilter {

    private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;
    
    protected Collection getNeededRoles() {
        List<InfoRole> roles = new ArrayList<InfoRole>();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);

        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            List roles = getRoleList(id.getRoles());

            if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                    || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                //verify if the user is employee
                if (!verifyEmployee(id)) {
                    return "noAuthorization";
                }

                //verify if the student to enroll is a non master degree student
                if (!verifyStudentType(arguments, DEGREE_TYPE)) {
                    return "error.student.degree.nonMaster";
                }
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyStudentType(Object[] arguments, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        boolean isRightType = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = (Integer) arguments[0];
            if (studentNumber != null) {
                IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
                Student student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                        degreeType);
                if (student != null) {
                    isRightType = true; // right student curricular plan
                }
            }
        }

        return isRightType;
    }
    
    private boolean verifyEmployee(IUserView id) throws ExcepcaoPersistencia{
        
        if(id != null && id.getPerson() != null && id.getPerson().getEmployee() != null){
            return true;
        }
        
        return false;
    }

}
