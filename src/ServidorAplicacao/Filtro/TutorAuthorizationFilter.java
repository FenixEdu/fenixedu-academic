/*
 * Created on 20/Jan/2005
 *  
 */
package ServidorAplicacao.Filtro;

import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author joaosa and rmalo
 *  
 */
public class TutorAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static TutorAuthorizationFilter instance = new TutorAuthorizationFilter();

    public static Filtro getInstance() {
        return instance;
    }

   
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        if ((id == null) || (id.getRoles() == null)
                || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                || !isTutor(id, argumentos)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean isTutor(IUserView id, Object[] argumentos) {
        if (argumentos == null) {
            return false;
        }
        if(argumentos[0] == null)return false;	
        
        ISuportePersistente sp;
        
        try {
        	sp = SuportePersistenteOJB.getInstance();
            IPersistentTutor persistentTutor = sp.getIPersistentTutor();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            
            ITeacher teacher = persistentTeacher.readTeacherByUsername((String) argumentos[0]);

            List tutorStudents = persistentTutor.readStudentsByTeacher(teacher);
            
            if (tutorStudents == null) {
            	 return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}