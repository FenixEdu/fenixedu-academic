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

import DataBeans.InfoAnnouncement;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoSite;
import DataBeans.RoomKey;
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

		HttpSession session = request.getSession(true);
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
					(
						InfoExecutionPeriod) RequestUtils
							.getExecutionPeriodFromRequest(
						request);

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

		HttpSession session = request.getSession(true);

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

		HttpSession session = request.getSession(true);
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
			infoExecPeriod =
				RequestUtils.getExecutionPeriodFromRequest(request);

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

				RequestUtils.setExecutionCourseToRequest(
					request,
					infoExecCourse);

			}

			getAssociatedCurricularCourses(request, gestor, infoExecCourse);

			//start reading Gesdis related info
			//read site
			GestorServicos manager = GestorServicos.manager();

			InfoSite site = getSite(request, gestor, infoExecCourse);

			//read Sections			
			RequestUtils.setSectionsToRequest(request, site);

			getTeachers(request, gestor, infoExecCourse);

			getLastAnnouncement(request, gestor, site);

			return mapping.findForward("executionCourseViewer");
		} else {
			throw new Exception();
		}
	}

	private InfoSite getSite(
		HttpServletRequest request,
		GestorServicos manager,
		InfoExecutionCourse infoExecCourse)
		throws FenixServiceException {
		Object[] args2 = { infoExecCourse };
		InfoSite site = (InfoSite) manager.executar(null, "ReadSite", args2);
		RequestUtils.setSiteFirstPageToRequest(request, site);
		return site;
	}

	private void getAssociatedCurricularCourses(
		HttpServletRequest request,
		GestorServicos manager,
		InfoExecutionCourse infoExecCourse)
		throws FenixServiceException {
		// Read associated curricular courses to display curricular course information.
		Object argsReadCurricularCourseListOfExecutionCourse[] =
			{ infoExecCourse };

		List infoCurricularCourses =
			(List) manager.executar(
				null,
				"ReadCurricularCourseListOfExecutionCourse",
				argsReadCurricularCourseListOfExecutionCourse);

		if (infoCurricularCourses != null
			&& !infoCurricularCourses.isEmpty()) {
			request.setAttribute(
				"publico.infoCurricularCourses",
				infoCurricularCourses);
		}
	}

	private void getLastAnnouncement(
		HttpServletRequest request,
		GestorServicos manager,
		InfoSite site)
		throws FenixActionException {
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

			setAnnouncementToRequest(request, lastAnnouncement);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
	}

	private void setAnnouncementToRequest(
		HttpServletRequest request,
		InfoAnnouncement lastAnnouncement) {
		request.setAttribute("lastAnnouncement", lastAnnouncement);
	}

	private void getTeachers(
		HttpServletRequest request,
		GestorServicos manager,
		InfoExecutionCourse infoExecCourse)
		throws FenixServiceException {
		//read responsible

		Object[] args3 = { infoExecCourse };
		List teacherList = null;
		teacherList =
			(List) manager.executar(
				null,
				"ReadTeachersByExecutionCourseResponsibility",
				args3);
		
		//read	lecturing teachers
		Object[] args4 = { infoExecCourse };
		List lecturingTeacherList = null;
		lecturingTeacherList =
			(List) manager.executar(
				null,
				"ReadTeachersByExecutionCourseProfessorship",
				args4);
		
		setTeachersToRequest(request, teacherList, lecturingTeacherList);
	}

	private void setTeachersToRequest(
		HttpServletRequest request,
		List teacherList,
		List lecturingTeacherList) {
		if (teacherList != null) {
						request.setAttribute("resTeacherList", teacherList);
					}
		if (lecturingTeacherList != null) {
			lecturingTeacherList.removeAll(teacherList);
			request.setAttribute("lecTeacherList", lecturingTeacherList);
		}
	}

}
