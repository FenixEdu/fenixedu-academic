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
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

public class SiteViewerDispatchAction extends FenixDispatchAction {

	public ActionForward roomViewer(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		String roomName = (String) request.getParameter("roomName");
		ActionErrors errors = new ActionErrors();
		RoomKey roomKey = null;
		InfoRoom infoRoom = null;
		List lessons = null;

		if (roomName != null) {
			roomKey = new RoomKey(roomName);

			GestorServicos gestor = GestorServicos.manager();

			Object args[] = new Object[1];
			args[0] = roomKey;

			try {
				infoRoom = (InfoRoom) gestor.executar(null, "LerSala", args);
			} catch (FenixServiceException nee) {
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(nee.getMessage()));
			} finally {
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

			if (infoRoom != null) {
				InfoExecutionPeriod infoExecutionPeriod = 
					(InfoExecutionPeriod) RequestUtils.getExecutionPeriodFromRequest(request);

				Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

				lessons =
					(List) ServiceUtils.executeService(
						null,
						"LerAulasDeSalaEmSemestre",
						argsReadLessons);
			}

			if ((infoRoom != null) && (lessons != null)) {
				
				request.setAttribute("lessonList", lessons);
				request.setAttribute("publico.infoRoom", infoRoom);
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

		DynaActionForm courseForm = (DynaActionForm) form;

		return executionCourseViewerSelectedByInitials(
			mapping,
			form,
			request,
			response,
			(String) courseForm.get("courseInitials"));

	}

	public ActionForward executionCourseViewer(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		String exeCourseCode = null;

		exeCourseCode = (String) request.getParameter("exeCourseCode");
		if (exeCourseCode == null) {
			exeCourseCode = (String) request.getParameter("exeCode");
		}

		return executionCourseViewerSelectedByInitials(
			mapping,
			form,
			request,
			response,
			exeCourseCode);
	}

	public ActionForward executionCourseViewerSelectedByInitials(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response,
		String exeCourseCode)
		throws Exception {

		HttpSession session = request.getSession(true);
		
		ActionErrors errors = new ActionErrors();
		InfoExecutionPeriod infoExecPeriod = null;
		InfoExecutionCourse infoExecCourse = null;

		if (session != null) {
			infoExecPeriod = RequestUtils.getExecutionPeriodFromRequest(request);
				
			// TODO: Isto não faz nada, pois não?
			if (infoExecPeriod == null) {
				infoExecPeriod =
					RequestUtils.getExecutionPeriodFromRequest(request);
			}

			GestorServicos gestor = GestorServicos.manager();

			Object args[] = { infoExecPeriod, exeCourseCode };

			try {
				infoExecCourse =
					(InfoExecutionCourse) gestor.executar(
						null,
						"ReadExecutionCourse",
						args);
			} catch (FenixServiceException nee) {
				errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError(nee.getMessage()));
			} finally {
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

			if (infoExecCourse != null) {
				RequestUtils.setExecutionCourseToRequest(request,infoExecCourse);
			}

			// Read associated curricular courses to display curricular course information.
			Object argsReadCurricularCourseListOfExecutionCourse[] =
				{ infoExecCourse };
				
			List infoCurricularCourses =
				(List) gestor.executar(
					null,
					"ReadCurricularCourseListOfExecutionCourse",
					argsReadCurricularCourseListOfExecutionCourse);
			
			
			if (infoCurricularCourses != null
				&& !infoCurricularCourses.isEmpty()) {
				request.setAttribute(
					"publico.infoCurricularCourses",
					infoCurricularCourses);
			}

			//start reading Gesdis related info
			//read site
			GestorServicos manager = GestorServicos.manager();
			InfoSite site = null;
			Object[] args2 = { infoExecCourse };
			site = (InfoSite) manager.executar(null, "ReadSite", args2);
			RequestUtils.setSiteFirstPageToRequest(request,site);
			
			//read Sections			
			RequestUtils.setSectionsToRequest(request,site);

			//read responsible

			Object[] args3 = { infoExecCourse };
			List teacherList = null;
			teacherList =
				(List) manager.executar(
					null,
					"ReadTeachersByExecutionCourseResponsibility",
					args3);

			if (teacherList != null) {

				request.setAttribute(
					"resTeacherList",
					teacherList);
			}

			//read	lecturing teachers
			Object[] args4 = { infoExecCourse };
			List lecturingTeacherList = null;
			lecturingTeacherList =
				(List) manager.executar(
					null,
					"ReadTeachersByExecutionCourseProfessorship",
					args4);

			if (lecturingTeacherList != null) {
				lecturingTeacherList.removeAll(teacherList);
				request.setAttribute(
					"lecTeacherList",
					lecturingTeacherList);
			}

			//			Read last Anouncement
			Object args1[] = new Object[1];
			args1[0] = site;

			InfoAnnouncement lastAnnouncement = null;

			try {
				lastAnnouncement =
					(InfoAnnouncement) manager.executar(
						null,
						"ReadLastAnnouncement",
						args1);
						
				request.setAttribute(
					"lastAnnouncement",
					lastAnnouncement);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}

			return mapping.findForward("executionCourseViewer");
		} else {
			throw new Exception();
		}
	}

}
