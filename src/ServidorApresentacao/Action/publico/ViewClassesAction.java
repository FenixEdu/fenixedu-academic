package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author João Mota
 */
public class ViewClassesAction extends FenixAction {

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(true);

		GestorServicos gestor = GestorServicos.manager();
		InfoClass infoClass = new InfoClass();

		InfoExecutionPeriod infoExecutionPeriod =
			RequestUtils.getExecutionPeriodFromRequest(request);
			
		String executionDegreeName = (String) request.getAttribute("degreeInitials");
		String nameDegreeCurricularPlan =(String) request.getAttribute("nameDegreeCurricularPlan");
			
		
		
		Object[] args1 =
			{
				infoExecutionPeriod.getInfoExecutionYear(),
				executionDegreeName,
				nameDegreeCurricularPlan
				 };
		InfoExecutionDegree infoExecutionDegree;
		try {
			infoExecutionDegree =
				(InfoExecutionDegree) ServiceUtils.executeService(
					null,
					"ReadExecutionDegreesByExecutionYearAndDegreeInitials",
					args1);
		} catch (FenixServiceException e1) {
			throw new FenixActionException(e1);
		}
		Integer curricularYear =
			 (Integer) request.getAttribute("curYear");

		infoClass.setAnoCurricular(curricularYear);

		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
		
		Object argsSelectClasses[] = { infoClass };
		List infoClasses;
		try {
			infoClasses =
				(List) gestor.executar(
					null,
					"SelectClasses",
					argsSelectClasses);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("publico.infoClasses", infoClasses);

		return mapping.findForward("Sucess");

	}

}
