package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoRoom;
import DataBeans.InfoSite;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteRoomTimeTable;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteTimetable;
import DataBeans.RoomKey;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
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
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		Integer infoExecutionCourseCode = new Integer(objectCodeString);

		readSiteView(
			request,
			firstPageComponent,
			infoExecutionCourseCode,
			null);
		return mapping.findForward("sucess");
	}

	public ActionForward announcements(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announcementsComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward objectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent objectivesComponent = new InfoSiteObjectives();
		readSiteView(request, objectivesComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward program(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent programComponent = new InfoSiteProgram();
		readSiteView(request, programComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward evaluationMethod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent evaluationComponent = new InfoEvaluationMethod();
		readSiteView(request, evaluationComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward bibliography(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent bibliographyComponent = new InfoSiteBibliography();
		readSiteView(request, bibliographyComponent, null, null);
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
		readSiteView(request, curricularCoursesComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward timeTable(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent timeTableComponent = new InfoSiteTimetable();
		readSiteView(request, timeTableComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward shifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent shiftsComponent = new InfoSiteShifts();
		readSiteView(request, shiftsComponent, null, null);
		return mapping.findForward("sucess");
	}

	public ActionForward evaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent evaluationComponent = new InfoSiteEvaluation();
		readSiteView(request, evaluationComponent, null, null);
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
		readSiteView(request, sectionComponent, null, sectionIndex);

		return mapping.findForward("sucess");
	}

	private void readSiteView(
		HttpServletRequest request,
		ISiteComponent firstPageComponent,
		Integer infoExecutionCourseCode,
		Integer sectionIndex)
		throws FenixActionException {
		InfoSite infoSite = null;
		Integer objectCode = null;
		if (infoExecutionCourseCode == null) {
			String objectCodeString = request.getParameter("objectCode");
			if (objectCodeString == null) {
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
				sectionIndex };

		try {
			ExecutionCourseSiteView siteView =
				(ExecutionCourseSiteView) ServiceUtils.executeService(
					null,
					"ExecutionCourseSiteComponentService",
					args);

			if (infoExecutionCourseCode != null) {
				request.setAttribute(
					"objectCode",
					((InfoSiteFirstPage) siteView.getComponent())
						.getSiteIdInternal());
			} else {
				request.setAttribute("objectCode", objectCode);
			}
			request.setAttribute("siteView", siteView);
			request.setAttribute(
				"executionCourseCode",
				((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse()
					.getIdInternal());
			request.setAttribute(
				"executionPeriodCode",
				((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse()
					.getInfoExecutionPeriod()
					.getIdInternal());
			if (siteView.getComponent() instanceof InfoSiteSection) {
				request.setAttribute(
					"infoSection",
					((InfoSiteSection) siteView.getComponent()).getSection());
			}
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("A disciplina",e);
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

		String roomName = (String) request.getParameter("roomName");
		if (roomName == null) {
			roomName = (String) request.getAttribute("roomName");
		}

		RoomKey roomKey = null;
		InfoRoom infoRoom = null;
		List lessons = null;

		if (roomName != null) {

			roomKey = new RoomKey(roomName);

			Integer objectCode = null;
			String objectCodeString = request.getParameter("objectCode");
			if (objectCodeString == null) {
				objectCodeString = (String) request.getAttribute("objectCode");
			}
			objectCode = new Integer(objectCodeString);

			ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();

			Object[] args = { bodyComponent, roomKey, objectCode };

			try {
				SiteView siteView =
					(SiteView) ServiceUtils.executeService(
						null,
						"RoomSiteComponentService",
						args);

				request.setAttribute("objectCode", objectCode);
				request.setAttribute("siteView", siteView);

			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException(e);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			return mapping.findForward("roomViewer");
		} else {
			throw new FenixActionException();
		}
	}
}