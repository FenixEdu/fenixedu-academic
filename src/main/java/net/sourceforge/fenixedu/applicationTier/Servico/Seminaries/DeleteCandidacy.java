/*
 * Created on 25/Ago/2003, 15:09:58
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 25/Ago/2003, 15:09:58
 * 
 */
public class DeleteCandidacy extends FenixService {

    protected void run(Integer id) throws BDException {
        SeminaryCandidacy candidacy = rootDomainObject.readSeminaryCandidacyByOID(id);
        candidacy.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteCandidacy serviceInstance = new DeleteCandidacy();

    @Service
    public static void runDeleteCandidacy(Integer id) throws BDException, NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        serviceInstance.run(id);
    }

}