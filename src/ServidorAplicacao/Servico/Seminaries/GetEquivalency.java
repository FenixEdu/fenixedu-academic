/*
 * Created on 4/Ago/2003, 13:05:42
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.Seminaries.InfoTheme;
import Dominio.Seminaries.CourseEquivalency;
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
 * Created at 4/Ago/2003, 13:05:42
 *  
 */
public class GetEquivalency implements IService {

    public GetEquivalency() {
    }

    public InfoEquivalency run(Integer equivalencyID) throws BDException {
        InfoEquivalency infoEquivalency = null;
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCurricularCourseEquivalency persistentEquivalency = persistenceSupport
                    .getIPersistentSeminaryCurricularCourseEquivalency();
            ICourseEquivalency equivalency = (ICourseEquivalency) persistentEquivalency.readByOID(
                    CourseEquivalency.class, equivalencyID);
            if (equivalency != null) {
                infoEquivalency = InfoEquivalency.newInfoFromDomain(equivalency);
                infoEquivalency.setThemes((List) CollectionUtils.collect(equivalency.getThemes(),
                        new Transformer() {

                            public Object transform(Object arg0) {

                                return InfoTheme.newInfoFromDomain((ITheme) arg0);
                            }
                        }));
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve an seminary/curricular course equivalency from the database",
                    ex);
        }
        return infoEquivalency;
    }
}