/*
 * Created on Nov 14, 2003
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

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
    public void execute(ServiceRequest request, ServiceResponse response)
            throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            if ((id == null)
                    || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(),
                            getRoleType())
                    || !attendsExamExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean attendsExamExecutionCourse(IUserView id, Object[] argumentos) {
        ISuportePersistente sp;
        IExam exam = null;
        InfoExam infoExam = null;

        if (argumentos == null) {
            return false;
        }
        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentExam persistentExam = sp.getIPersistentExam();

            if (argumentos[1] instanceof InfoExam) {
                infoExam = (InfoExam) argumentos[1];
                exam = Cloner.copyInfoExam2IExam(infoExam);
            } else {
                exam = (IExam) persistentExam.readByOID(Exam.class,
                        (Integer) argumentos[1]);
            }

            String username = (String) argumentos[0];
            return persistentExam.isExamOfExecutionCourseTheStudentAttends(exam
                    .getIdInternal(), username);
        } catch (Exception ex) {
            return false;
        }

    }

}