package net.sourceforge.fenixedu.presentationTier.Action.teacher.credits;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.NonRegularTeachingServiceBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.NonRegularTeachingService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForTeacherCE;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/manageNonRegularTeachingService", module = "teacher")
@Forwards({
	@Forward(name = "showNonRegularTeachingService", path = "/teacher/credits/showNonRegularTeachingService.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
	@Forward(name = "editNonRegularTeachingService", path = "/teacher/credits/editNonRegularTeachingService.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")) })
public class ManageNonRegularTeachingServiceDA extends FenixDispatchAction {

    public ActionForward showNonRegularTeachingService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Person person = ((IUserView) UserView.getUser()).getPerson();

	ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	request.setAttribute("professorships", person.getProfessorshipsByExecutionSemester(executionSemester));

	TeacherCreditsFillingForTeacherCE teacherCE = executionSemester.getTeacherCreditsFillingForTeacherPeriod();
	Interval interval = new Interval(teacherCE.getBegin(), teacherCE.getEnd().plusDays(1));
	if (interval.containsNow()) {
	    request.setAttribute("canEdit", true);
	}

	return mapping.findForward("showNonRegularTeachingService");
    }

    public ActionForward prepareEditNonRegularTeachingService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Professorship professorship = getDomainObject(request, "professorshipOID");

	List<NonRegularTeachingServiceBean> nonRegularTeachingServiceBean = new ArrayList<NonRegularTeachingServiceBean>();
	for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
	    nonRegularTeachingServiceBean.add(new NonRegularTeachingServiceBean(shift, professorship));
	}
	request.setAttribute("professorship", professorship);
	request.setAttribute("nonRegularTeachingServiceBean", nonRegularTeachingServiceBean);

	return mapping.findForward("editNonRegularTeachingService");
    }

    public ActionForward editNonRegularTeachingService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	List<NonRegularTeachingServiceBean> nonRegularTeachingServiceBeans = getRenderedObject();
	for (NonRegularTeachingServiceBean nonRegularTeachingServiceBean : nonRegularTeachingServiceBeans) {
	    try {
		NonRegularTeachingService.createOrEdit(nonRegularTeachingServiceBean.getProfessorship(),
			nonRegularTeachingServiceBean.getShift(), nonRegularTeachingServiceBean.getPercentage());
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage());
		request.setAttribute("professorship", nonRegularTeachingServiceBean.getProfessorship());
		request.setAttribute("nonRegularTeachingServiceBean", nonRegularTeachingServiceBeans);
		return mapping.findForward("editNonRegularTeachingService");
	    }
	}
	return showNonRegularTeachingService(mapping, form, request, response);
    }

}