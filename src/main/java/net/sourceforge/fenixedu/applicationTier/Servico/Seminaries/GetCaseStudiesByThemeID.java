/*
 * Created on 4/Ago/2003, 18:58:03
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 4/Ago/2003, 18:58:03
 * 
 */
public class GetCaseStudiesByThemeID {

    protected List run(Integer themeID) throws BDException {
        List cases = RootDomainObject.getInstance().readThemeByOID(themeID).getCaseStudies();

        List infoCases = new LinkedList();
        for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
            CaseStudy caseStudy = (CaseStudy) iterator.next();
            infoCases.add(InfoCaseStudy.newInfoFromDomain(caseStudy));
        }

        return infoCases;
    }

    // Service Invokers migrated from Berserk

    private static final GetCaseStudiesByThemeID serviceInstance = new GetCaseStudiesByThemeID();

    @Service
    public static List runGetCaseStudiesByThemeID(Integer themeID) throws BDException, NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(themeID);
    }

}