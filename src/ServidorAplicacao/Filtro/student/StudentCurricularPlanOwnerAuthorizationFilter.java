/*
 * Created on Nov 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Filtro.student;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AccessControlFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.StudentCurricularPlanIDDomainType;

/**
 * @author André Fernandes / João Brito
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StudentCurricularPlanOwnerAuthorizationFilter extends
        AccessControlFilter
{
    public StudentCurricularPlanOwnerAuthorizationFilter()
    {    
    }
    
    /* (non-Javadoc)
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response)
            throws FilterException, Exception
    {
        IUserView id = (IUserView) request.getRequester();
        String messageException;

        if (id == null || id.getRoles() == null
                || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType()))
        {
            throw new NotAuthorizedFilterException();
        }
        else
        {
            messageException = curricularPlanOwner(id, request.getArguments());
            if (messageException != null)
                throw new NotAuthorizedFilterException(messageException);
        }
    }

    /*
    (String username, StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criterio)
    */
    //devolve null se tudo OK
    // noAuthorization se algum prob
    private String curricularPlanOwner(IUserView id, Object[] arguments)
	{
	    IStudentCurricularPlan studentCurricularPlan;
	    StudentCurricularPlanIDDomainType scpId = (StudentCurricularPlanIDDomainType)arguments[1];
	    Integer studentCurricularPlanID;
	    
	    
	    // permitir ler se o ID nao tiver sido especificado
	    if (scpId.isAll() || scpId.isNewest())
	        return null;
	    else
	        studentCurricularPlanID = scpId.getId();
	    
	    
	    try {
	        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
	                .getIStudentCurricularPlanPersistente();
	
	        studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readByOID(
	                StudentCurricularPlan.class, studentCurricularPlanID);
	        if (studentCurricularPlan == null)
	        {
	            return "noAuthorization";
	        }
	        if (!studentCurricularPlan.getStudent().getPerson().getUsername().equals(id.getUtilizador())) 
	        {
	            return "noAuthorization";
	        }
	    }
	    catch (Exception exception)
	    {
	        exception.printStackTrace();
	        return "noAuthorization";
	    }
	    return null;
	}
	
	protected RoleType getRoleType()
	{
	    return RoleType.STUDENT;
	}
}