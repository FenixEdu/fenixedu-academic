/*
 * Created on 6/Mai/2003
 *
 *
 */
package ServidorAplicacao.Servico.publico;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.Factory.ExecutionCourseSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ExecutionCourseSiteComponentService implements IService {

	

	public ExecutionCourseSiteComponentService() {

	}

	

	public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent, Integer infoSiteCode, Integer infoExecutionCourseCode, Integer sectionIndex, Integer curricularCourseId)
		throws FenixServiceException, NonExistingAssociatedCurricularCoursesServiceException {
		ExecutionCourseSiteView siteView = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			
			ISite site = null;
		
			if (infoSiteCode != null) {

				site = (ISite) persistentSite.readByOId(new Site(infoSiteCode), false);
				if (site == null) {
					throw new NonExistingServiceException();
				}
			} else {
				IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(new ExecutionCourse(infoExecutionCourseCode), false);
				if (executionCourse == null) {
					throw new FenixServiceException();
				}
				site = persistentSite.readByExecutionCourse(executionCourse);
			}
			
			if (site != null) {
				ExecutionCourseSiteComponentBuilder componentBuilder = ExecutionCourseSiteComponentBuilder.getInstance();
				commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
				bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent, sectionIndex, curricularCourseId);
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
