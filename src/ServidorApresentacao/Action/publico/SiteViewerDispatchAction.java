package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorApresentacao.Action.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class SiteViewerDispatchAction extends FenixDispatchAction {

	public ActionForward roomViewer(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);

		HttpSession session = request.getSession(false);
		String roomName = (String) request.getParameter("roomName");
		ActionErrors errors = new ActionErrors();
		RoomKey roomKey = null;
		InfoRoom infoRoom = null;
		List lessons = null;

		if( (session != null) && (roomName != null) ) {
			roomKey = new RoomKey(roomName);

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();
			
			Object args[] = new Object[1];
			args[0] = roomKey;

			try {
				infoRoom = (InfoRoom) gestor.executar(userView, "LerSala", args);
			} catch(FenixServiceException nee) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
			} catch(ExcepcaoInexistente ein) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ein.getMessage()));
			} finally {
				if(!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

			if(infoRoom != null) {
				InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);

				Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

				lessons = (List) ServiceUtils.executeService(null, "LerAulasDeSalaEmSemestre", argsReadLessons);
			}

			if((infoRoom != null) && (lessons != null) ) {
				session.removeAttribute(SessionConstants.LESSON_LIST_ATT);
				session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);
				session.setAttribute("publico.infoRoom", infoRoom);
			}
			return mapping.findForward("roomViewer");
		} else {
			throw new Exception();
		}
	}


	public ActionForward executionCourseViewerSelectedFromForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		SessionUtils.validSessionVerification(request, mapping);

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			DynaActionForm courseForm = (DynaActionForm) form;

			return executionCourseViewerSelectedByInitials(mapping, form, request, response, (String) courseForm.get("courseInitials"));
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}


	public ActionForward executionCourseViewer(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);

		HttpSession session = request.getSession(false);
		ActionErrors errors = new ActionErrors();
		InfoExecutionPeriod infoExecPeriod = null;
		InfoExecutionCourse infoExecCourse = null;
		String exeCourseCode = null;

		if(session != null) {
			exeCourseCode = (String) request.getParameter("exeCourseCode");
		}

		return executionCourseViewerSelectedByInitials(mapping, form, request, response, exeCourseCode);
	}


	public ActionForward executionCourseViewerSelectedByInitials(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response,
		String exeCourseCode)
		throws Exception {
		
		SessionUtils.validSessionVerification(request, mapping);
		
		HttpSession session = request.getSession(false);
		ActionErrors errors = new ActionErrors();
		InfoExecutionPeriod infoExecPeriod = null;
		InfoExecutionCourse infoExecCourse = null;

		if(session != null) {
			infoExecPeriod = (InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();

			Object args[] = {infoExecPeriod, exeCourseCode};

			try {
				infoExecCourse = (InfoExecutionCourse) gestor.executar(userView, "ReadExecutionCourse", args);
			} catch(FenixServiceException nee) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
			} finally {
				if(!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

			if(infoExecCourse != null) {
				// Read shifts of execution course.
				// Just the shifts aren't enough... we also need to read the classes
				// associated to the shift and the classes associated with the shift.
				Object argsSelectShifts[] = { infoExecCourse };
				List infoShifts = (List) gestor.executar(null, "SelectExecutionShiftsWithAssociatedLessonsAndClasses", argsSelectShifts);

				if(infoShifts != null && !infoShifts.isEmpty()) {
					System.out.println("publico.infoShifts não é vazio... :)");
					session.setAttribute("publico.infoShifts", infoShifts);
				} else {
					System.out.println("publico.infoShifts é vazio... :(");
				}
				
				session.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, infoExecCourse);
			}
			
			// Read associated curricular courses to dispçlay curricular course information.
			Object argsReadCurricularCourseListOfExecutionCourse[] =
				{ infoExecCourse };
			List infoCurricularCourses =
				(List) gestor.executar(
					null,
					"ReadCurricularCourseListOfExecutionCourse",
					argsReadCurricularCourseListOfExecutionCourse);
			
			if(infoCurricularCourses != null && !infoCurricularCourses.isEmpty()) {
				session.setAttribute("publico.infoCurricularCourses", infoCurricularCourses);
			}

			// Read list of Lessons to show execution course schedule.
			Object argsReadLessonsOfExecutionCours[] = { infoExecCourse };
			List infoLessons =
				(List) gestor.executar(
					null,
					"LerAulasDeDisciplinaExecucao",
					argsReadLessonsOfExecutionCours);
			
			if (infoLessons != null)
				session.setAttribute(SessionConstants.LESSON_LIST_ATT, infoLessons);

			return mapping.findForward("executionCourseViewer");
		} else {
			throw new Exception();
		}
	}

}
