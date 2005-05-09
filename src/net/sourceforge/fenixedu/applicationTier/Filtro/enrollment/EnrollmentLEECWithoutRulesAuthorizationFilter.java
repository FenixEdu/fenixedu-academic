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
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class EnrollmentLEECWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {
    private static String DEGREE_LEEC_CODE = new String("LEEC");

    private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;

    protected Collection getNeededRoles() {
        List roles = new ArrayList();

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
            ISuportePersistente sp = null;
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            //verify if the degree type is LICENCIATURA_OBJ
            if (!verifyDegreeTypeIsNonMaster(arguments)) {
                return new String("error.degree.type");
            }

            //verify if the student to enroll is a non master degree student
            if (!verifyStudentNonMasterDegree(arguments, sp)) {
                return new String("error.student.degree.nonMaster");
            }

            //verify if the student to enroll is a LEEC degree student
            if (!verifyStudentLEEC(arguments, sp)) {
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

    private boolean verifyStudentNonMasterDegree(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        boolean isNonMaster = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                IPersistentStudent persistentStudent = sp.getIPersistentStudent();
                IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                        DEGREE_TYPE);
                if (student != null) {
                    isNonMaster = true; //non master student
                }
            }
        }

        return isNonMaster;
    }

    //with student number and degree type
    private boolean verifyStudentLEEC(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        boolean isLEEC = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                        .getIStudentCurricularPlanPersistente();

                IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
                studentCurricularPlan = persistentStudentCurricularPlan
                        .readActiveByStudentNumberAndDegreeType(studentNumber, DEGREE_TYPE);
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