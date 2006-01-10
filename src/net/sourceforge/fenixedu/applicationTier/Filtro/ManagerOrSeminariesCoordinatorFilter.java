/*
 * Created on 8/Set/2003, 14:37:23
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 8/Set/2003, 14:37:23
 *  
 */
public class ManagerOrSeminariesCoordinatorFilter extends Filtro {
    public ManagerOrSeminariesCoordinatorFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Integer SCPIDINternal = (Integer) request.getServiceParameters().getParameter(1);
        
        boolean seminaryCandidate = false;
        if(SCPIDINternal != null){
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }
                
        if (((id != null && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType1()) && !(AuthorizationUtils
                .containsRole(id.getRoles(), getRoleType2()) && seminaryCandidate)))
                || (id == null) || (id.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }
    
    public boolean doesThisSCPBelongToASeminaryCandidate(Integer SCPIDInternal)
    {
        boolean result = false;
        try
        {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            StudentCurricularPlan scp = (StudentCurricularPlan)sp.getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,SCPIDInternal);
            List candidacies = sp.getIPersistentSeminaryCandidacy().readByStudentID(scp.getStudent().getIdInternal());
            if (candidacies != null && candidacies.size() > 0)
                result = true;
        }
        catch (ExcepcaoPersistencia e)
        {
           // thats ok, lets say this SCP does <br>NOT</br> belong to a candidate
        }
        
        
        return result;
    }

    protected RoleType getRoleType1() {
        return RoleType.MANAGER;
    }

    protected RoleType getRoleType2() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}