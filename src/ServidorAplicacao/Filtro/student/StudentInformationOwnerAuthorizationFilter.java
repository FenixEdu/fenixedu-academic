package ServidorAplicacao.Filtro.student;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/*
 * 
 * @author Fernanda Quitério 10/Fev/2004
 *  
 */
public class StudentInformationOwnerAuthorizationFilter extends Filtro {

    public StudentInformationOwnerAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response)
            throws Exception {
        IUserView id = getRemoteUser(request);

        if (id == null
                || id.getRoles() == null
                || !AuthorizationUtils.containsRole(id.getRoles(),
                        getRoleType())
                || !curriculumOwner(id, request.getArguments())) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param objects
     * @return
     */
    private boolean curriculumOwner(IUserView id, Object[] arguments) {
        IStudentCurricularPlan studentCurricularPlan;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                    .readByOID(StudentCurricularPlan.class,
                            (Integer) arguments[1]);
            if (studentCurricularPlan == null) {
                return false;
            }
            if (!studentCurricularPlan.getStudent().getPerson().getUsername()
                    .equals(id.getUtilizador())) {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }
}