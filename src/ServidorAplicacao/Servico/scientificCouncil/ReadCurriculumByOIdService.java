/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import DataBeans.InfoCurriculum;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.Curriculum;
import Dominio.ICurriculum;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class ReadCurriculumByOIdService implements IServico {

    private static ReadCurriculumByOIdService _servico = new ReadCurriculumByOIdService();

    /**
     * The actor of this class.
     */

    private ReadCurriculumByOIdService() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadCurriculumByOIdService";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ReadCurriculumByOIdService getService() {
        return _servico;
    }

    public SiteView run(Integer curriculumId) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurriculum persistentCurriculum = sp
                    .getIPersistentCurriculum();
            ICurriculum curriculum = (ICurriculum) persistentCurriculum
                    .readByOID(Curriculum.class, curriculumId);
            InfoCurriculum infoCurriculum = Cloner
                    .copyICurriculum2InfoCurriculum(curriculum);
            SiteView siteView = new SiteView(infoCurriculum);

            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}