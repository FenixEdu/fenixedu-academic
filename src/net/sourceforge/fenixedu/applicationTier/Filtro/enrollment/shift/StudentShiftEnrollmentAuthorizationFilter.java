package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.shift;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/*
 * 
 * @author Fernanda Quitério 13/Fev/2004
 *  
 */
public class StudentShiftEnrollmentAuthorizationFilter extends Filtro {

    //	private static String DEGREE_LEEC_CODE = new String("LEEC");

    public StudentShiftEnrollmentAuthorizationFilter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasProvilege(id, request.getServiceParameters().parametersArray());
        if ((id == null) || (id.getRoles() == null) || (!containsRole(id.getRoles()))
                || (messageException != null)) {
            throw new NotAuthorizedFilterException(messageException);
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.STUDENT);
        roles.add(infoRole);

        return roles;
    }

    private List getRoleList(Collection roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /**
     * @param id
     * @param argumentos
     * @return null if authorized string with message if not authorized
     */
    private String hasProvilege(IUserView id, Object[] arguments) {
        List roles = getRoleList(id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        InfoStudent infoStudent = (InfoStudent) arguments[0];
        //		Integer executionCourseIdToAttend = (Integer) arguments[1];

        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IStudent student = (IStudent) sp.getIPersistentStudent().readByOID(Student.class,
                    infoStudent.getIdInternal());

            Integer studentNumber = student.getNumber();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
            if (studentCurricularPlan == null) {
                studentCurricularPlan = persistentStudentCurricularPlan
                        .readActiveByStudentNumberAndDegreeType(studentNumber, DegreeType.MASTER_DEGREE);
            }

        } catch (Exception e) {
            return "noAuthorization";
        }

        if (studentCurricularPlan == null || studentCurricularPlan.getStudent() == null) {
            return "noAuthorization";
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.STUDENT);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            try {
                if (!id.getUtilizador().equals(
                        studentCurricularPlan.getStudent().getPerson().getUsername())) {
                    return "noAuthorization";
                }

                //				if (verifyStudentLEEC(studentCurricularPlan))
                //				{
                //					if(!findEnrollmentForAttend(studentCurricularPlan,
                // executionCourseIdToAttend)){
                //						return "noAuthorization";
                //					}
                //				}
            } catch (Exception e) {
                return "noAuthorization";
            }
            return null;
        }
        return "noAuthorization";
    }

    //	private boolean findEnrollmentForAttend(
    //		IStudentCurricularPlan studentCurricularPlan,
    //		Integer executionCourseIdToAttend)
    //		throws ExcepcaoPersistencia
    //	{
    //		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
    //		IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
    //		IExecutionCourse executionCourse =
    // findExecutionCourse(executionCourseIdToAttend, sp);
    //		
    //		if(executionCourse == null) {
    //			return false;
    //		}
    //		// checks if there is an enrollment for this attend
    //		Iterator iterCurricularCourses =
    // executionCourse.getAssociatedCurricularCourses().iterator();
    //		while (iterCurricularCourses.hasNext())
    //		{
    //			ICurricularCourse curricularCourseElem = (ICurricularCourse)
    // iterCurricularCourses.next();
    //
    //			IEnrolment enrollment =
    //				persistentEnrolment.readByStudentCurricularPlanAndCurricularCourse(
    //					studentCurricularPlan,
    //					curricularCourseElem);
    //			if (enrollment != null)
    //			{
    //				return true;
    //			}
    //		}
    //		return false;
    //	}

    //	private IExecutionCourse findExecutionCourse(Integer
    // executionCourseIdToAttend, ISuportePersistente sp) throws
    // ExcepcaoPersistencia
    //	{
    //		IPersistentExecutionCourse persistentExecutionCourse =
    // sp.getIPersistentExecutionCourse();
    //
    //		IExecutionCourse executionCourse =
    //			(IExecutionCourse) persistentExecutionCourse.readByOID(
    //				ExecutionCourse.class,
    //				executionCourseIdToAttend);
    //		return executionCourse;
    //	}
    //
    //	private boolean verifyStudentLEEC(IStudentCurricularPlan
    // studentCurricularPlan)
    //		throws ExcepcaoPersistencia
    //	{
    //		String degreeCode = null;
    //		if (studentCurricularPlan.getDegreeCurricularPlan() != null
    //			&& studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null)
    //		{
    //			degreeCode =
    // studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla();
    //		}
    //
    //		return DEGREE_LEEC_CODE.equals(degreeCode);
    //	}
}