/*
 * Created on 26/Ago/2003, 13:35:57
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro.Seminaries;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.IStudent;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import Util.RoleType;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 13:35:57
 *  
 */
public class CandidacyAccessFilter extends Filtro
{
    public CandidacyAccessFilter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
                    Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        if ((!this.checkCandidacyOwnership(id, argumentos))
                        && (!this.checkCoordinatorRole(id, argumentos)))
                        throw new NotAuthorizedException();

    }

    boolean checkCoordinatorRole(IUserView id, Object[] arguments) throws Exception
    {
        boolean result = true;
//        Collection roles = id.getRoles();
//        Iterator iter = roles.iterator();
//        while (iter.hasNext())
//        {
//            InfoRole role = (InfoRole) iter.next();
//        }
        if (((id != null && id.getRoles() != null && !AuthorizationUtils.containsRole(
                        id.getRoles(), getRoleType())))
                        || (id == null) || (id.getRoles() == null))
        {
            result = false;
        }
        return result;
    }

    boolean checkCandidacyOwnership(IUserView id, Object[] arguments) throws Exception
    {
        boolean result = true;
        Integer candidacyID = (Integer) arguments[0];
        ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
        IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                        .getIPersistentSeminaryCandidacy();
        //
        IStudent student = persistenceSupport.getIPersistentStudent().readByUsername(id.getUtilizador());
        if (student != null)
        {
            ICandidacy candidacy = (ICandidacy) persistentCandidacy.readByOID(
                            Candidacy.class, candidacyID);
            //
            if ((candidacy != null)
                            && (candidacy.getStudentIdInternal().intValue() != student.getIdInternal()
                                            .intValue())) result = false;
        }
        else
        {
            result = false;
        }
        return result;
    }

    private RoleType getRoleType()
    {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}
