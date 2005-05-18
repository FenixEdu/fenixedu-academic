/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 *  
 */
public class ExecutionCourseSiteComponentService implements IService {

    public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent,
            Integer infoSiteCode, Integer infoExecutionCourseCode, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException,
            NonExistingAssociatedCurricularCoursesServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IPersistentSite persistentSite = sp.getIPersistentSite();

        final ISite site;
        if (infoSiteCode != null)
            site = (ISite) persistentSite.readByOID(Site.class, infoSiteCode);
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