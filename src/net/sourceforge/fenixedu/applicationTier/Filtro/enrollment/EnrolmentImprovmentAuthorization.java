/*
 * Created on Nov 25, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author nmgo
 */
public class EnrolmentImprovmentAuthorization extends AuthorizationByManyRolesFilter {

    
    private static TipoCurso DEGREE_TYPE = TipoCurso.LICENCIATURA_OBJ;
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AccessControlFilter#getNeededRoles()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByManyRolesFilter#hasPrevilege(ServidorAplicacao.IUserView,
     *      java.lang.Object[])
     */
    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            ISuportePersistente sp = null;
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            List roles = getRoleList((List) id.getRoles());

            if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                    || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                //verify if the user is employee
                if (!verifyEmployee(id, sp)) {
                    return "noAuthorization";
                }

                //verify if the student to enroll is a non master degree student
                if (!verifyStudentType(arguments, sp, DEGREE_TYPE)) {
                    return "error.student.degree.nonMaster";
                }
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyStudentType(Object[] arguments, ISuportePersistente sp, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        boolean isRightType = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = (Integer) arguments[0];
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
    
    private boolean verifyEmployee(IUserView id, ISuportePersistente sp) throws ExcepcaoPersistencia{
        
        String user = id.getUtilizador();
        
        if(user != null) {
            IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente(); 
            IPerson pessoa = pessoaPersistente.lerPessoaPorUsername(user);
            if(pessoa != null) {
                IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
                IEmployee employee = persistentEmployee.readByPerson(pessoa);
                if(employee != null) {
                    return true;
                }
            }
        }      
        return false;
    }

}
