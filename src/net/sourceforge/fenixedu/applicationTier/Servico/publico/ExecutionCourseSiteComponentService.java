/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 *  
 */
public class ExecutionCourseSiteComponentService implements IService {

    public ExecutionCourseSiteComponentService() {

    }

    public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent,
            Integer infoSiteCode, Integer infoExecutionCourseCode, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException,
            NonExistingAssociatedCurricularCoursesServiceException {
        ExecutionCourseSiteView siteView = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            ISite site = null;

            if (infoSiteCode != null) {

                site = (ISite) persistentSite.readByOID(Site.class, infoSiteCode);
                if (site == null) {
                    throw new NonExistingServiceException();
                }
            } else {
                IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class, infoExecutionCourseCode);
                if (executionCourse == null) {
                    throw new FenixServiceException();
                }
                site = persistentSite.readByExecutionCourse(executionCourse);
            }

            if (site != null) {
                ExecutionCourseSiteComponentBuilder componentBuilder = ExecutionCourseSiteComponentBuilder
                        .getInstance();
                commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
                bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent,
                        sectionIndex, curricularCourseId);
                siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        } catch (Exception e) {

            throw new FenixServiceException(e);
        }

        return siteView;
    }
}