/*
 * Created on 3/Set/2003, 14:22:08
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
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixframework.Atomic;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 3/Set/2003, 14:22:08
 * 
 */
public class GetAllCasesStudy {

    protected List run() throws BDException {
        List infoCases = new LinkedList();

        List cases = CaseStudy.getAllCaseStudies();

        for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
            CaseStudy caseStudy = (CaseStudy) iterator.next();

            infoCases.add(InfoCaseStudy.newInfoFromDomain(caseStudy));
        }

        return infoCases;
    }

    // Service Invokers migrated from Berserk

    private static final GetAllCasesStudy serviceInstance = new GetAllCasesStudy();

    @Atomic
    public static List runGetAllCasesStudy() throws BDException, NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run();
    }

}