/*
 * Created on 25/Ago/2003, 18:18:02
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 25/Ago/2003, 18:18:02
 * 
 */
public class GetCaseStudiesByEquivalencyID {

    protected List run(String equivalencyID) {
        List<InfoCaseStudy> infoCases = new LinkedList<InfoCaseStudy>();

        CourseEquivalency equivalency = FenixFramework.getDomainObject(equivalencyID);
        List<CaseStudy> cases = new LinkedList<CaseStudy>();
        Collection themes = equivalency.getThemes();

        for (Iterator iterator = themes.iterator(); iterator.hasNext();) {
            Theme theme = (Theme) iterator.next();
            cases.addAll(theme.getCaseStudies());
        }

        for (Object element : cases) {
            CaseStudy caseStudy = (CaseStudy) element;
            infoCases.add(InfoCaseStudy.newInfoFromDomain(caseStudy));
        }

        return infoCases;
    }

    // Service Invokers migrated from Berserk

    private static final GetCaseStudiesByEquivalencyID serviceInstance = new GetCaseStudiesByEquivalencyID();

    @Atomic
    public static List runGetCaseStudiesByEquivalencyID(String equivalencyID) throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(equivalencyID);
    }

}