package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseAction extends FenixContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			super.execute(mapping, form, request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		InfoExecutionCourse executionCourse = new InfoExecutionCourse();

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
				
		InfoExecutionDegree infoExecutionDegree =
			RequestUtils.getExecutionDegreeFromRequest(
				request,
				infoExecutionPeriod.getInfoExecutionYear());

		executionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

		Integer curricularYear = (Integer) request.getAttribute("curYear");

		Object argsSelectExecutionCourse[] =
			{ infoExecutionDegree, infoExecutionPeriod, curricularYear };

		List infoExecutionCourses;
		try {
			infoExecutionCourses =
				(List) ServiceManagerServiceFactory.executeService(
					null,
					"SelectExecutionCourse",
					argsSelectExecutionCourse);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(infoExecutionCourses,new BeanComparator("nome"));
		request.setAttribute("exeCourseList", infoExecutionCourses);
		return mapping.findForward("sucess");
	}

}
