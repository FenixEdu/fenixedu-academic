/*
 * Created on 26/Ago/2003, 13:35:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 26/Ago/2003, 13:35:57
 * 
 */
public class CandidacyOwnershipFilter extends Filtro {
    public CandidacyOwnershipFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk
     * .ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    @Override
    public void execute(ServiceRequest request) throws Exception {
        Integer candidacyID = (Integer) request.getServiceParameters().parametersArray()[0];

        Registration registration = Registration.readByUsername(AccessControl.getUserView().getUtilizador());
        SeminaryCandidacy candidacy = RootDomainObject.getInstance().readSeminaryCandidacyByOID(candidacyID);
        //
        if ((candidacy != null) && (candidacy.getStudent().getIdInternal().intValue() != registration.getIdInternal().intValue())) {
            throw new NotAuthorizedException();
        }

    }
}