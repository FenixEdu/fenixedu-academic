/*
 * Created on 4/Ago/2003
 *
 */
package ServidorAplicacao.Servico.publico;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ExecutionCourseSiteComponentBuilder;
import ServidorAplicacao.Factory.GroupSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

    public Object run(ISiteComponent commonComponent,
            ISiteComponent bodyComponent, Integer infoSiteCode,
            //Integer executionCourseCode,
            //Integer sectionIndex,
            //Integer curricularCourseId,
            Integer groupPropertiesCode, Integer code)
            throws FenixServiceException {

        ExecutionCourseSiteView executionCourseSiteView = null;

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = null;
            if (infoSiteCode != null) {
                site = (ISite) persistentSite.readByOID(Site.class,
                        infoSiteCode);
                if (site == null) {
                    throw new NonExistingServiceException();
                }
            }
            GroupSiteComponentBuilder componentBuilder = GroupSiteComponentBuilder
                    .getInstance();
            bodyComponent = componentBuilder.getComponent(bodyComponent, site
                    .getExecutionCourse().getIdInternal(), groupPropertiesCode,
                    code);
            ExecutionCourseSiteComponentBuilder componentBuilder2 = ExecutionCourseSiteComponentBuilder
                    .getInstance();

            commonComponent = componentBuilder2.getComponent(commonComponent,
                    site, null, null, null);

            executionCourseSiteView = new ExecutionCourseSiteView(
                    commonComponent, bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return executionCourseSiteView;
    }
}