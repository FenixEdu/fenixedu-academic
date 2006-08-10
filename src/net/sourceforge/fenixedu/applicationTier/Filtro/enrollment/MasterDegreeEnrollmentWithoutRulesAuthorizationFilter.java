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
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author David Santos in Mar 1, 2004
 */

public class MasterDegreeEnrollmentWithoutRulesAuthorizationFilter extends
        AuthorizationByManyRolesFilter {
    private static DegreeType DEGREE_TYPE = DegreeType.MASTER_DEGREE;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            if (!verifyDegreeTypeIsMasterDegree(arguments)) {
                return new String("error.degree.type");
            }

            if (!verifyStudentIsFromMasterDegree(arguments)) {
                return new String("error.student.degree.nonMaster");
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyDegreeTypeIsMasterDegree(Object[] arguments) {
        boolean isNonMaster = false;

        if (arguments != null && arguments[1] != null) {
            isNonMaster = DEGREE_TYPE.equals(arguments[1]);
        }

        return isNonMaster;
    }

    private boolean verifyStudentIsFromMasterDegree(Object[] arguments) throws ExcepcaoPersistencia {
        boolean isFromMasterDegree = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, DEGREE_TYPE);
                if (student != null) {
                    isFromMasterDegree = true;
                }
            }
        }

        return isFromMasterDegree;
    }
}