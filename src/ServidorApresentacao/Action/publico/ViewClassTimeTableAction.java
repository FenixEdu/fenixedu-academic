/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author João Mota
 *
 */
public class ViewClassTimeTableAction extends Action {

	/**
	 * Constructor for ViewClassTimeTableAction.
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(true);
		String className = request.getParameter("className");

		if (className == null)
			return mapping.getInputForward();

		InfoExecutionPeriod infoExecutionPeriod =
			RequestUtils.getExecutionPeriodFromRequest(request);

		System.out.println("infoExecutionPeriod"+infoExecutionPeriod);

		InfoExecutionDegree infoExecutionDegree =
			RequestUtils.getExecutionDegreeFromRequest(
				request,
				infoExecutionPeriod.getInfoExecutionYear());

		
		System.out.println("infoExecutionDegree"+infoExecutionDegree);
		
		InfoClass classView = new InfoClass();
		classView.setInfoExecutionDegree(infoExecutionDegree);
		classView.setInfoExecutionPeriod(infoExecutionPeriod);
		classView.setNome(className);

		Object[] args = { classView };
		List lessons;
		try {
			lessons =
				(List) ServiceUtils.executeService(
					null,
					"LerAulasDeTurma",
					args);
		} catch (FenixServiceException e1) {
			throw new FenixActionException(e1);
		}

		request.setAttribute("class", classView);
		request.setAttribute("lessonList", lessons);

		return mapping.findForward("Sucess");
	}
}
