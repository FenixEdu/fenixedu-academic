/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoEvaluation;
import DataBeans.InfoExecutionCourse;
import ServidorAplicacao.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ViewEvaluationAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		//get executionCourse from request
		InfoExecutionCourse infoExecutionCourse =
			RequestUtils.getExecutionCourseFromRequest(request);
		
		//execute action main service
		Object[] args = { infoExecutionCourse };
		InfoEvaluation infoEvaluation = null;
		try {
			 infoEvaluation =
				(InfoEvaluation) ServiceUtils.executeService(
					null,
					"ReadEvaluation",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
				
				
		//place in request everything needed	
		if (infoEvaluation!=null){				
		request.setAttribute("evaluationElements",infoEvaluation.getEvaluationElements());
		}	
		//find forward			
		return mapping.findForward("Sucess");
	}

}
