package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.EditarTurno.InvalidNewShiftExecutionCourse;
import ServidorAplicacao.Servico.sop.EditarTurno.InvalidNewShiftType;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;
import Util.TipoAula;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageShiftDA
	extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward prepareEditShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		InfoShift infoShiftToEdit =
			(InfoShift) request.getAttribute(SessionConstants.SHIFT);
		System.out.println(
			"###### infoShiftToEdit.getInfoClasses().size()= "
				+ infoShiftToEdit.getInfoClasses().size());

		/* Fill out form to be edited with shifts original values */
		DynaActionForm editShiftForm = (DynaActionForm) form;
		//if (editShiftForm.get("courseInitials") == null) {
		editShiftForm.set(
			"courseInitials",
			infoShiftToEdit.getInfoDisciplinaExecucao().getSigla());
		//}
		//if (editShiftForm.get("nome") == null) {
		editShiftForm.set("nome", infoShiftToEdit.getNome());
		//}
		//if (editShiftForm.get("tipoAula") == null) {
		editShiftForm.set("tipoAula", infoShiftToEdit.getTipo().getTipo());
		//}

		/* Place list of execution courses in request */
		SessionUtils.getExecutionCourses(request);

		/* Place label list of types of shifts/lessons in request */
		RequestUtils.setLessonTypes(request);

		return mapping.findForward("EditShift");
	}

	public ActionForward editShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm editShiftForm = (DynaActionForm) form;

		InfoShift infoShiftOld =
			(InfoShift) request.getAttribute(SessionConstants.SHIFT);

		InfoShift infoShiftNew = new InfoShift();
		infoShiftNew.setIdInternal(infoShiftOld.getIdInternal());
		infoShiftNew.setAvailabilityFinal(infoShiftOld.getAvailabilityFinal());
		InfoExecutionCourse infoExecutionCourseNew =
			RequestUtils.getExecutionCourseBySigla(
				request,
				(String) editShiftForm.get("courseInitials"));
		infoShiftNew.setInfoDisciplinaExecucao(infoExecutionCourseNew);
		infoShiftNew.setInfoLessons(infoShiftOld.getInfoLessons());
		infoShiftNew.setLotacao(infoShiftOld.getLotacao());
		infoShiftNew.setNome((String) editShiftForm.get("nome"));
		infoShiftNew.setTipo(
			new TipoAula((Integer) editShiftForm.get("tipoAula")));

		Object argsCriarTurno[] = { infoShiftOld, infoShiftNew };
		try {
			infoShiftNew =
				(InfoShift) ServiceUtils.executeService(
					userView,
					"EditarTurno",
					argsCriarTurno);
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException("O Turno", ex);
		} catch (InvalidNewShiftType e1) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"errors.exception.invalid.newShiftType",
				new ActionError("errors.exception.invalid.newShiftType"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		} catch (InvalidNewShiftExecutionCourse e2) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"errors.exception.invalid.newExecutionCourse",
				new ActionError("errors.exception.invalid.newExecutionCourse"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

		request.setAttribute(
			SessionConstants.EXECUTION_COURSE,
			infoExecutionCourseNew);

		request.setAttribute(SessionConstants.SHIFT, infoShiftNew);

		return prepareEditShift(mapping, form, request, response);
	}

	public ActionForward removeClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ContextUtils.setClassContext(request);

		IUserView userView = SessionUtils.getUserView(request);

		InfoClass infoClass =
			(InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

		Integer shiftOID =
			new Integer(request.getParameter(SessionConstants.SHIFT_OID));

		Object[] args = { shiftOID };
		InfoShift infoShift = null;
		try {
			infoShift =
				(InfoShift) ServiceUtils.executeService(
					userView,
					"ReadShiftByOID",
					args);
			System.out.println("shift= " + infoShift);
		} catch (FenixServiceException e) {
			throw new FenixActionException();
		}

		Object argsRemove[] = { infoShift, infoClass };
		ServiceUtils.executeService(userView, "RemoverTurno", argsRemove);
		
		return prepareEditShift(mapping, form, request, response);
	}

}