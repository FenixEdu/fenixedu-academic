package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSite;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 */
public class EditCustomizationOptions implements IService {

    public EditCustomizationOptions() {
    }

    public boolean run(Integer infoExecutionCourseCode, InfoSite infoSiteNew)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            ISite siteOld = null;

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, infoExecutionCourseCode);
            siteOld = persistentSite.readByExecutionCourse(executionCourse);

            persistentSite.simpleLockWrite(siteOld);

            siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
            siteOld.setMail(infoSiteNew.getMail());
            siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
            siteOld.setIntroduction(infoSiteNew.getIntroduction());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }
}