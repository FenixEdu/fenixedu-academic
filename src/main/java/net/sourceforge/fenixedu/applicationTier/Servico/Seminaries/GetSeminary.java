/*
 * Created on 31/Jul/2003, 19:12:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalenciesWithAll;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 31/Jul/2003, 19:12:41
 * 
 */
public class GetSeminary {

    protected InfoSeminaryWithEquivalencies run(String seminaryID) {
        InfoSeminaryWithEquivalencies infoSeminary = null;

        Seminary seminary = FenixFramework.getDomainObject(seminaryID);
        if (seminary != null) {

            infoSeminary = InfoSeminaryWithEquivalenciesWithAll.newInfoFromDomain(seminary);
        }

        return infoSeminary;
    }

    // Service Invokers migrated from Berserk

    private static final GetSeminary serviceInstance = new GetSeminary();

    @Service
    public static InfoSeminaryWithEquivalencies runGetSeminary(String seminaryID) throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(seminaryID);
    }

}