/*
 * Created on 26/Ago/2003, 13:35:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

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
        ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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