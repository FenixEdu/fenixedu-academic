/*
 * Created on 25/Ago/2003, 18:18:02
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoCaseStudy;
import Dominio.Seminaries.CourseEquivalency;
import Dominio.Seminaries.ICaseStudy;
import Dominio.Seminaries.ICourseEquivalency;
import Dominio.Seminaries.ITheme;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;

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