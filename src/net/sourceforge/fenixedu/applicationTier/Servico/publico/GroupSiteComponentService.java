/*
 * Created on 4/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Factory.GroupSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 *  
 */

public class GroupSiteComponentService implements IServico {

    private static GroupSiteComponentService _servico = new GroupSiteComponentService();

    /**
     * The actor of this class.
     */

    private GroupSiteComponentService() {
    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "GroupSiteComponentService";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static GroupSiteComponentService getService() {
        return _servico;
    }

    public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent,
            Integer infoSiteCode,
            //Integer executionCourseCode,
            //Integer sectionIndex,
            //Integer curricularCourseId,
            Integer groupPropertiesCode, Integer code, Integer shiftCode, Integer value) throws FenixServiceException {

        ExecutionCourseSiteView executionCourseSiteView = null;

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = null;
            if (infoSiteCode != null) {
                site = (ISite) persistentSite.readByOID(Site.class, infoSiteCode);
                if (site == null) {
                    throw new NonExistingServiceException();
                }
            }
            GroupSiteComponentBuilder componentBuilder = GroupSiteComponentBuilder.getInstance();
            bodyComponent = componentBuilder.getComponent(bodyComponent, site.getExecutionCourse()
                    .getIdInternal(), groupPropertiesCode, code, shiftCode, value);
            ExecutionCourseSiteComponentBuilder componentBuilder2 = ExecutionCourseSiteComponentBuilder
                    .getInstance();

            commonComponent = componentBuilder2.getComponent(commonComponent, site, null, null, null);

            executionCourseSiteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return executionCourseSiteView;
    }
}