/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorApresentacao.Action.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteBasicCurricularCourses;
import DataBeans.InfoSiteCurricularCourses;
import DataBeans.InfoSiteDegreeCurricularPlans;
import DataBeans.InfoSiteSCDegrees;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 * 23/Jul/2003
 * fenix-head
 * ServidorApresentacao.Action.scientificCouncil
 * 
 */
public class CurricularCourseManagerDA extends FenixDispatchAction {

	public ActionForward prepareSelectDegree(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);
			ISiteComponent component = new InfoSiteSCDegrees();
			readSiteView(request, userView, null, null, null, component);
			return mapping.findForward("selectDegree");
		} else
			throw new FenixActionException();
		//			 nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward showDegreeCurricularPlans(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			String degreeIdString = (String) request.getParameter("index");

			Integer degreeId = new Integer(degreeIdString);
			ISiteComponent component = new InfoSiteDegreeCurricularPlans();
			readSiteView(request, userView, degreeId, null, null, component);
			return mapping.findForward("showDegreeCurricularPlans");
		} else
			throw new FenixActionException();
		//			 nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward showCurricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			String degreeCurricularPlanIdString =
				(String) request.getParameter("index");
			Integer degreeCurricularPlanId =
				new Integer(degreeCurricularPlanIdString);

			ISiteComponent component = new InfoSiteCurricularCourses();
			readSiteView(
				request,
				userView,
				null,
				null,
				degreeCurricularPlanId,
				component);
			return mapping.findForward("showCurricularCourses");
		} else
			throw new FenixActionException();
		//			 nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward showBasicCurricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			String degreeCurricularPlanIdString =
				(String) request.getParameter("index");
			Integer degreeCurricularPlanId =
				new Integer(degreeCurricularPlanIdString);

			ISiteComponent component = new InfoSiteBasicCurricularCourses();
			readSiteView(
				request,
				userView,
				null,
				null,
				degreeCurricularPlanId,
				component);
			return mapping.findForward("showCurricularCourses");
		} else
			throw new FenixActionException();
		//			 nao ocorre... pedido passa pelo filtro Autorizacao

	}

	private void readSiteView(
		HttpServletRequest request,
		IUserView userView,
		Integer degreeId,
		Integer curricularYear,
		Integer degreeCurricularPlanId,
		ISiteComponent component)
		throws FenixActionException {
		Object[] args =
			{ component, degreeId, curricularYear, degreeCurricularPlanId };
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ScientificCouncilComponentService",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("siteView", siteView);
	}

}
