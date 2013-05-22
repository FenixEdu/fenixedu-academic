/*
 * Created on 3/Set/2003, 15:10:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 3/Set/2003, 15:10:43
 * 
 */
public class GetAllModalities extends FenixService {

    protected List run() throws BDException {
        List infoCases = new LinkedList();

        List cases = Modality.getAllModalities();

        for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
            Modality modality = (Modality) iterator.next();

            infoCases.add(InfoModality.newInfoFromDomain(modality));
        }

        return infoCases;
    }

    // Service Invokers migrated from Berserk

    private static final GetAllModalities serviceInstance = new GetAllModalities();

    @Service
    public static List runGetAllModalities() throws BDException  , NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run();
    }

}