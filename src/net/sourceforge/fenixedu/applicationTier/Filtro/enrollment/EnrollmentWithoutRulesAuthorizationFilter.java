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
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class EnrollmentWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {

    private static TipoCurso DEGREE_TYPE = TipoCurso.LICENCIATURA_OBJ;

    private static TipoCurso MASTER_DEGREE_TYPE = TipoCurso.MESTRADO_OBJ;

    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            ISuportePersistente sp = null;
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            List roles = getRoleList(id.getRoles());

            if (roles.contains(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {

                //verify if the degree type is MASTER_DEGREE_OBJ
                if (!verifyDegreeType(arguments, MASTER_DEGREE_TYPE)) {
                    return new String("error.masterDegree.type");
                }
                //verify if the student to enroll is a master degree student
                if (!verifyStudentType(arguments, sp, MASTER_DEGREE_TYPE)) {
                    return new String("error.student.degree.master");
                }
            }

            if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                    || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                //verify if the degree type is LICENCIATURA_OBJ
                if (!verifyDegreeType(arguments, DEGREE_TYPE)) {
                    return new String("error.degree.type");
                }

                //verify if the student to enroll is a non master degree
                // student
                if (!verifyStudentType(arguments, sp, DEGREE_TYPE)) {
                    return new String("error.student.degree.nonMaster");
                }
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyDegreeType(Object[] arguments, TipoCurso degreeType) {
        boolean isEqual = false;

        if (arguments != null && arguments[1] != null) {
            isEqual = degreeType.equals(arguments[1]);
        }

        return isEqual;
    }

    private boolean verifyStudentType(Object[] arguments, ISuportePersistente sp, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        boolean isRightType = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                IPersistentStudent persistentStudent = sp.getIPersistentStudent();
                IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                        degreeType);
                if (student != null) {
                    isRightType = true; // right student curricular plan
                }
            }
        }

        return isRightType;
    }

}