/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public class ExecutionCourseSiteComponentService extends Service {

    public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent,
            Integer infoSiteCode, Integer infoExecutionCourseCode, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException,
            NonExistingAssociatedCurricularCoursesServiceException, ExcepcaoPersistencia {
        IPersistentSite persistentSite = persistentSupport.getIPersistentSite();

        final Site site;
        if (infoSiteCode != null)
            site = (Site) persistentObject.readByOID(Site.class, infoSiteCode);
        else
            site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);

        if (site == null) {
            throw new NonExistingServiceException();
        }

        ExecutionCourseSiteComponentBuilder componentBuilder = ExecutionCourseSiteComponentBuilder
                .getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent,
                sectionIndex, curricularCourseId);
        return new ExecutionCourseSiteView(commonComponent, bodyComponent);
    }
}