/*
 * Created on 8/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author lmac1
 */

public class InsertCurricularCourseDispatchAction extends FenixDispatchAction {

	public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		return mapping.findForward("insertCurricularCourse");
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String type = (String) dynaForm.get("type");
		String mandatory = (String) dynaForm.get("mandatory");
		String basic = (String) dynaForm.get("basic");

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);

		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
//		if (basic.compareTo("") != 0)
			infoCurricularCourse.setBasic(new Boolean(basic));
		infoCurricularCourse.setCode((String) dynaForm.get("code"));
		infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
//		if (mandatory.compareTo("") != 0)
			infoCurricularCourse.setMandatory(new Boolean(mandatory));
		infoCurricularCourse.setName((String) dynaForm.get("name"));

		infoCurricularCourse.setType(new CurricularCourseType(new Integer(type)));

		Object args[] = { infoCurricularCourse };

		GestorServicos manager = GestorServicos.manager();

		try {
			manager.executar(userView, "InsertCurricularCourseAtDegreeCurricularPlan", args);

		} catch (ExistingServiceException ex) {
			throw new ExistingActionException(ex.getMessage(), ex);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward("readDegreeCurricularPlan");
	}
}