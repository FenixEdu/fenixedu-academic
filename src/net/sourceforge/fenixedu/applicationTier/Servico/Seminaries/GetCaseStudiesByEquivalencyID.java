/*
 * Created on 25/Ago/2003, 18:18:02
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.ICourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.ITheme;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Ago/2003, 18:18:02
 *  
 */
public class GetCaseStudiesByEquivalencyID implements IService {

    /**
     * The actor of this class.
     */
    public GetCaseStudiesByEquivalencyID() {
    }

    public List run(Integer equivalencyID) throws BDException {
        List infoCases = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCurricularCourseEquivalency persistentEquivalency = persistenceSupport
                    .getIPersistentSeminaryCurricularCourseEquivalency();
            //
            ICourseEquivalency equivalency = (ICourseEquivalency) persistentEquivalency.readByOID(
                    CourseEquivalency.class, equivalencyID);
            //
            List cases = new LinkedList();
            List themes = equivalency.getThemes();

            for (Iterator iterator = themes.iterator(); iterator.hasNext();) {
                ITheme theme = (ITheme) iterator.next();
                cases.addAll(theme.getCaseStudies());
            }

            for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
                ICaseStudy caseStudy = (ICaseStudy) iterator.next();
                infoCases.add(InfoCaseStudy.newInfoFromDomain(caseStudy));
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve mutiple case studies from the database", ex);
        }
        return infoCases;
    }
}