/*
 * Created on 26/Ago/2003, 13:35:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro.Seminaries;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.IStudent;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 13:35:57
 *  
 */
public class CandidacyOwnershipFilter extends Filtro {
    public CandidacyOwnershipFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        Integer candidacyID = (Integer) getServiceCallArguments(request)[0];
        ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
        IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                .getIPersistentSeminaryCandidacy();
        //
        IStudent student = persistenceSupport.getIPersistentStudent().readByUsername(
                getRemoteUser(request).getUtilizador());
        ICandidacy candidacy = (ICandidacy) persistentCandidacy.readByOID(Candidacy.class, candidacyID);
        //
        if ((candidacy != null)
                && (candidacy.getStudent().getIdInternal().intValue() != student.getIdInternal()
                        .intValue()))
            throw new NotAuthorizedException();

    }
}