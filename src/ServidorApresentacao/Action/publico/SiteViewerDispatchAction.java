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

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoEvaluation;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoSite;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteExam;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteRoomTimeTable;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteTimetable;
import DataBeans.RoomKey;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

public class SiteViewerDispatchAction extends FenixDispatchAction {

	public ActionForward firstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent firstPageComponent = new InfoSiteFirstPage();
		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString==null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		Integer infoExecutionCourseCode = new Integer(objectCodeString);
		
		readSiteView(request, firstPageComponent,infoExecutionCourseCode,null);
		return mapping.findForward("sucess");
	}

	public ActionForward announcements(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announcementsComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward objectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent objectivesComponent = new InfoSiteObjectives();
		readSiteView(request, objectivesComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward program(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent programComponent = new InfoSiteProgram();
		readSiteView(request, programComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward evaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent evaluationComponent = new InfoEvaluation();
		readSiteView(request, evaluationComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward bibliography(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent bibliographyComponent = new InfoSiteBibliography();
		readSiteView(request, bibliographyComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward curricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent curricularCoursesComponent =
			new InfoSiteAssociatedCurricularCourses();
		readSiteView(request, curricularCoursesComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward timeTable(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent timeTableComponent = new InfoSiteTimetable();
		readSiteView(request, timeTableComponent,null,null);
		return mapping.findForward("sucess");
	}

	public ActionForward shifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent shiftsComponent = new InfoSiteShifts();
		readSiteView(request, shiftsComponent,null,null);
		return mapping.findForward("sucess");
	}
	
	public ActionForward exams(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {

			ISiteComponent examComponent = new InfoSiteExam();
			readSiteView(request, examComponent,null,null);
			return mapping.findForward("sucess");
		}
	
	public ActionForward section(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {

			String indexString = (String) request.getParameter("index");						
			Integer sectionIndex = new Integer(indexString);
			
			ISiteComponent sectionComponent = new InfoSiteSection();
			readSiteView(request, sectionComponent,null,sectionIndex);
			
			return mapping.findForward("sucess");
		}

	private void readSiteView(
		HttpServletRequest request,
		ISiteComponent firstPageComponent,
		Integer infoExecutionCourseCode,
		Integer sectionIndex
		)
		throws FenixActionException {
			InfoSite infoSite =null;
			Integer objectCode = null;
		if (infoExecutionCourseCode == null) {
			String objectCodeString = request.getParameter("objectCode");
			if (objectCodeString==null) {
				objectCodeString = (String) request.getAttribute("objectCode");
				
			}
		 objectCode = new Integer(objectCodeString);
		 
		} 
		
		ISiteComponent commonComponent = new InfoSiteCommon();

		Object[] args =
			{
				commonComponent,
				firstPageComponent,
				objectCode,
				infoExecutionCourseCode,
				sectionIndex
				 };

		try {
			ExecutionCourseSiteView siteView =
				(ExecutionCourseSiteView) ServiceUtils.executeService(
					null,
					"ExecutionCourseSiteComponentService",
					args);
		
		if(infoExecutionCourseCode != null){
			request.setAttribute("objectCode",((InfoSiteFirstPage)siteView.getComponent()).getSiteIdInternal());
		} else {
			request.setAttribute("objectCode",objectCode);
		}
			request.setAttribute("siteView", siteView);
			request.setAttribute("executionCourseCode",((InfoSiteCommon)siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
			if (siteView.getComponent() instanceof InfoSiteSection) {			
			request.setAttribute("infoSection", ((InfoSiteSection)siteView.getComponent()).getSection());}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
	}



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
			/*
			
			ISiteComponent roomTimeTableComponent = new InfoSiteRoomTimeTable();
			
			
			
			*/
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
