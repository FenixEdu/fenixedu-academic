package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
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

		InfoShift infoShiftOld = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

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
		}

		request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourseNew);

		request.setAttribute(SessionConstants.SHIFT, infoShiftNew);

		return prepareEditShift(mapping, form, request, response);
	}

}