/*
 * Created on Nov 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExamStudentAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExamStudentAuthorizationFilter() {
        super();
    }

    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || !attendsExamExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean attendsExamExecutionCourse(IUserView id, Object[] argumentos) {
        if (argumentos == null) {
            return false;
        }
        try {
            Integer examId;
            if (argumentos[1] instanceof InfoExam) {
                InfoExam infoExam = (InfoExam) argumentos[1];
                examId = infoExam.getIdInternal();
            } else {
                examId = (Integer) argumentos[1];
            }

            String username = (String) argumentos[0];

            final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            final IPersistentExam persistentExam = sp.getIPersistentExam();
            return persistentExam.isExamOfExecutionCourseTheStudentAttends(examId, username);
        } catch (Exception ex) {
            return false;
        }
    }

}
