package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class EditCustomizationOptions implements IService {

    public EditCustomizationOptions() {
    }

    public boolean run(Integer infoExecutionCourseCode, InfoSite infoSiteNew)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            ISite siteOld = null;

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);
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