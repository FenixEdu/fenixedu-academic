/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import DataBeans.ISiteComponent;
import DataBeans.SiteView;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ScientificCouncilComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author João Mota
 *
 * 23/Jul/2003
 * fenix-head
 * ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class SetBasicCurricularCoursesService implements IServico{

	private static SetBasicCurricularCoursesService _servico =
		new SetBasicCurricularCoursesService();

	/**
	  * The actor of this class.
	  **/

	private SetBasicCurricularCoursesService() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "setBasicCurricularCourses";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static SetBasicCurricularCoursesService getService() {
		return _servico;
	}

	public SiteView run(ISiteComponent bodyComponent,Integer degreeId, Integer curricularYear,Integer degreeCurricularPlanId) throws FenixServiceException {
			
			
			ScientificCouncilComponentBuilder componentBuilder = ScientificCouncilComponentBuilder.getInstance();
			bodyComponent = componentBuilder.getComponent(bodyComponent, degreeId,  curricularYear,degreeCurricularPlanId);
			SiteView siteView = new SiteView();
			siteView.setComponent(bodyComponent);	


		return siteView;
	}

}
