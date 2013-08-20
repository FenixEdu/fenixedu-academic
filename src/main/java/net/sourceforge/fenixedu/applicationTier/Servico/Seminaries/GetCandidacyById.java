/*
 * Created on 26/Ago/2003, 14:50:16
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries.CandidacyAccessFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyWithCaseStudyChoices;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 26/Ago/2003, 14:50:16
 * 
 */
public class GetCandidacyById {

    protected InfoCandidacy run(String id) {
        InfoCandidacy infoCandidacy = null;

        SeminaryCandidacy candidacy = AbstractDomainObject.fromExternalId(id);
        infoCandidacy = InfoCandidacyWithCaseStudyChoices.newInfoFromDomain(candidacy);

        return infoCandidacy;
    }

    // Service Invokers migrated from Berserk

    private static final GetCandidacyById serviceInstance = new GetCandidacyById();

    @Service
    public static InfoCandidacy runGetCandidacyById(String id) throws NotAuthorizedException {
        CandidacyAccessFilter.instance.execute(id);
        return serviceInstance.run(id);
    }

}