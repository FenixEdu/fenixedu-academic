package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.TeacherAdministrationSiteView;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 *  
 */
public class TeacherAdministrationSiteComponentService implements IService {

    public TeacherAdministrationSiteComponentService() {

    }

    public Object run(Integer infoExecutionCourseCode, ISiteComponent commonComponent,
            ISiteComponent bodyComponent, Integer infoSiteCode, Object obj1, Object obj2)
            throws FenixServiceException {
        TeacherAdministrationSiteView siteView = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            ISite site = null;
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);
            site = persistentSite.readByExecutionCourse(executionCourse);

            TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                    .getInstance();
            commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
            bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent, obj1,
                    obj2);

            siteView = new TeacherAdministrationSiteView(commonComponent, bodyComponent);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return siteView;
    }
}