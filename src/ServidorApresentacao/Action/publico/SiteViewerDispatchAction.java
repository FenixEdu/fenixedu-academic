package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
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
import DataBeans.comparators.ComparatorByLessonTypeForInfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
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
			}  finally {
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
//					System.out.println("publico.infoShifts não é vazio... :)");
					Collections.sort(infoShifts, new ComparatorByLessonTypeForInfoShiftWithAssociatedInfoClassesAndInfoLessons());
					session.setAttribute("publico.infoShifts", infoShifts);
				} else {
//					System.out.println("publico.infoShifts é vazio... :(");
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
			
			if (infoLessons != null){
				session.setAttribute(SessionConstants.LESSON_LIST_ATT, infoLessons);}
			
			//start reading Gesdis related info
//read site
			GestorServicos manager = GestorServicos.manager();
			InfoSite site=null;
			Object[] args2 = {infoExecCourse};
			site = (InfoSite) manager.executar(userView,"ReadSite",args2);
			session.setAttribute(SessionConstants.INFO_SITE,site);
//read Sections			
		//this is just a test:
		List sections=new ArrayList();
		InfoSection section1 = new InfoSection(
		"Seccao1deTFCI",
		new Integer(0),
		site,
		null);
		InfoSection section2 = new InfoSection(
		"Seccao1deTFCI",
		new Integer(0),
		site,
		null);
		InfoSection section3 = new InfoSection(
		"Seccao1deTFCI",
		new Integer(0),
		site,
		null);
		InfoSection section4 = new InfoSection(
		"Seccao1deTFCI",
		new Integer(0),
		site,
		null);
		sections.add(section1);
		sections.add(section2);
		sections.add(section3);
		sections.add(section4);
		List sections1=new ArrayList();
				sections1.add(section1);
				sections1.add(section2);
				sections1.add(section3);
				sections1.add(section4);
		sections.add(sections1);		
		session.setAttribute(SessionConstants.SECTIONS,sections);
		
//read responsible

		Object[] args3 = {infoExecCourse};
		List teacherList = null; 
		teacherList = (List) manager.executar(userView,"ReadTeachersByExecutionCourseResponsibility",args3);
		session.setAttribute(SessionConstants.RESPONSIBLE_TEACHERS_LIST,teacherList);	
//			Read last Anouncement
			 Object args1[] = new Object[1];
					  args1[0] = site;

					  InfoAnnouncement lastAnnouncement = null;		
					 
					  try {
						  lastAnnouncement = (InfoAnnouncement) manager.executar(userView, "ReadLastAnnouncement", args1);
						  session.setAttribute(SessionConstants.LAST_ANNOUNCEMENT, lastAnnouncement);
					  } catch (FenixServiceException fenixServiceException){
						  throw new FenixActionException(fenixServiceException.getMessage());
					  }

			return mapping.findForward("executionCourseViewer");
		} else {
			throw new Exception();
		}
	}

}
