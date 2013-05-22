/*
 * Created on 3/Set/2003, 16:21:47
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
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 3/Set/2003, 16:21:47
 * 
 */
public class GetAllThemes extends FenixService {

    protected List run() throws BDException {
        List seminariesInfo = new LinkedList();

        List themes = Theme.getAllThemes();
        for (Iterator iterator = themes.iterator(); iterator.hasNext();) {

            InfoTheme infoTheme = InfoTheme.newInfoFromDomain((Theme) iterator.next());

            seminariesInfo.add(infoTheme);
        }

        return seminariesInfo;
    }

    // Service Invokers migrated from Berserk

    private static final GetAllThemes serviceInstance = new GetAllThemes();

    @Service
    public static List runGetAllThemes() throws BDException  , NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run();
    }

}