/*
 * Created on 19/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão
 * 
 */
public class EnrollmentLEECWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {
    private static String DEGREE_LEEC_CODE = new String("LEEC");

    private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;

    protected Collection getNeededRoles() {
        List roles = new ArrayList();
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)));
        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            // verify if the degree type is LICENCIATURA_OBJ
            if (!verifyDegreeTypeIsNonMaster(arguments)) {
                return new String("error.degree.type");
            }

            // verify if the student to enroll is a non master degree student
            if (!verifyStudentNonMasterDegree(arguments)) {
                return new String("error.student.degree.nonMaster");
            }

            // verify if the student to enroll is a LEEC degree student
            if (!verifyStudentLEEC(arguments)) {
                return new String("error.student.degree.nonMaster");
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyDegreeTypeIsNonMaster(Object[] arguments) {
        boolean isNonMaster = false;

        if (arguments != null && arguments[1] != null) {
            isNonMaster = DEGREE_TYPE.equals(arguments[1]);
        }

        return isNonMaster;
    }

    private boolean verifyStudentNonMasterDegree(Object[] arguments) throws ExcepcaoPersistencia {
        boolean isNonMaster = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                Student student = Student.readStudentByNumberAndDegreeType(studentNumber, DEGREE_TYPE);
                if (student != null) {
                    isNonMaster = true; // non master student
                }
            }
        }

        return isNonMaster;
    }

    // with student number and degree type
    private boolean verifyStudentLEEC(Object[] arguments) throws ExcepcaoPersistencia {
        boolean isLEEC = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                Student student = Student.readStudentByNumberAndDegreeType(studentNumber, DEGREE_TYPE);
                StudentCurricularPlan studentCurricularPlan = null;
                if(student != null) {
                	studentCurricularPlan = student.getActiveStudentCurricularPlan();
                }
                if (studentCurricularPlan != null
                        && studentCurricularPlan.getDegreeCurricularPlan() != null
                        && studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null) {
                    String degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                            .getSigla();
                    isLEEC = DEGREE_LEEC_CODE.equals(degreeCode);
                }
            }
        }

        return isLEEC;
    }
}
