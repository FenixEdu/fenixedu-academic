/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum = (ICurriculum) persistentCurriculum.readByOID(Curriculum.class,
                    curriculumId);
            InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
            SiteView siteView = new SiteView(infoCurriculum);

            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}