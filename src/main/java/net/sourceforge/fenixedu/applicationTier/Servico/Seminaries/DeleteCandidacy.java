/*
 * Created on 25/Ago/2003, 15:09:58
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 25/Ago/2003, 15:09:58
 * 
 */
public class DeleteCandidacy {

    protected void run(String id) {
        SeminaryCandidacy candidacy = FenixFramework.getDomainObject(id);
        candidacy.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteCandidacy serviceInstance = new DeleteCandidacy();

    @Service
    public static void runDeleteCandidacy(String id) throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        serviceInstance.run(id);
    }

}