/*
 * Created on 18/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.CurricularCourseType;

/**
 * @author lmac1
 */
public class EditCurricularCourseDA extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm dynaForm = (DynaActionForm) form;

		
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

		InfoCurricularCourse oldInfoCurricularCourse = null;

		Object args[] = { curricularCourseId };
		

		try {
			oldInfoCurricularCourse = (InfoCurricularCourse) ServiceUtils.executeService(userView, "ReadCurricularCourse", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCP"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		dynaForm.set("name", oldInfoCurricularCourse.getName());
		dynaForm.set("code", oldInfoCurricularCourse.getCode());

		dynaForm.set("type", oldInfoCurricularCourse.getType().getCurricularCourseType().toString());
		System.out.println("type"+oldInfoCurricularCourse.getType().getCurricularCourseType());

		dynaForm.set("mandatory", oldInfoCurricularCourse.getMandatory().toString());

		dynaForm.set("basic", oldInfoCurricularCourse.getBasic().toString());

		return mapping.findForward("editCurricularCourse");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

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

		CurricularCourseType type = new CurricularCourseType(new Integer(typeString));
		newInfoCurricularCourse.setType(type);

		newInfoCurricularCourse.setMandatory(new Boolean(mandatoryString));
		
		newInfoCurricularCourse.setBasic(new Boolean(basicString));

		Object args[] = { newInfoCurricularCourse };
		
		try {
			ServiceUtils.executeService(userView, "EditCurricularCourse", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCP"));
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("message.manager.existing.curricular.course");
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		return mapping.findForward("readCurricularCourse");
	}
}