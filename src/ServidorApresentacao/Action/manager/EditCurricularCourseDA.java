/*
 * Created on 18/Ago/2003
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
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author lmac1
 */
public class EditCurricularCourseDA extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm dynaForm = (DynaActionForm) form;

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

		InfoCurricularCourse oldInfoCurricularCourse = null;

		Object args[] = { curricularCourseId };
		GestorServicos manager = GestorServicos.manager();

		try {
			oldInfoCurricularCourse = (InfoCurricularCourse) manager.executar(userView, "ReadCurricularCourse", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCP"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		dynaForm.set("name", oldInfoCurricularCourse.getName());
		dynaForm.set("code", oldInfoCurricularCourse.getCode());

		if (oldInfoCurricularCourse.getType() != null)
			dynaForm.set("type", oldInfoCurricularCourse.getType().toString());

		if (oldInfoCurricularCourse.getMandatory() != null)
			dynaForm.set("mandatory", oldInfoCurricularCourse.getMandatory().toString());

		if (oldInfoCurricularCourse.getBasic() != null)
			dynaForm.set("basic", oldInfoCurricularCourse.getBasic().toString());

		return mapping.findForward("editCurricularCourse");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm dynaForm = (DynaValidatorForm) form;

		Integer oldCurricularCourseId = new Integer(request.getParameter("curricularCourseId"));

		InfoCurricularCourse newInfoCurricularCourse = new InfoCurricularCourse();

		String name = (String) dynaForm.get("name");
		String code = (String) dynaForm.get("code");
		String typeString = (String) dynaForm.get("type");
		String mandatoryString = (String) dynaForm.get("mandatory");
		String basicString = (String) dynaForm.get("basic");

		newInfoCurricularCourse.setName(name);
		newInfoCurricularCourse.setCode(code);
		newInfoCurricularCourse.setIdInternal(oldCurricularCourseId);

		if (typeString.compareTo("") != 0) {
			CurricularCourseType type = new CurricularCourseType(new Integer(typeString));
			newInfoCurricularCourse.setType(type);
		}

		if (mandatoryString.compareTo("") != 0) {
			Boolean mandatory = new Boolean(mandatoryString);
			newInfoCurricularCourse.setMandatory(mandatory);
		}

		if (basicString.compareTo("") != 0) {
			Boolean basic = new Boolean(basicString);
			newInfoCurricularCourse.setBasic(basic);
		}

		Object args[] = { newInfoCurricularCourse };
		GestorServicos manager = GestorServicos.manager();
		
		try {
			manager.executar(userView, "EditCurricularCourse", args);
		} catch (NonExistingServiceException ex) {
							throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCP"));
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e.getMessage(), e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		return mapping.findForward("readDegreeCP");
	}
}