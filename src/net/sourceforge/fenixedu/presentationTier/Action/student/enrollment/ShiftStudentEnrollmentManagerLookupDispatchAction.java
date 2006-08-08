package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalLookupDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ShiftStudentEnrollmentManagerLookupDispatchAction extends TransactionalLookupDispatchAction {

	public ActionForward addCourses(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixTransactionException,
			FenixFilterException {

		super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

		checkParameter(request);

		final IUserView userView = getUserView(request);
		final DynaActionForm form = (DynaActionForm) actionForm;
		final Integer executionCourseId = (Integer) form.get("wantedCourse");

		try {
			ServiceManagerServiceFactory.executeService(userView, "WriteStudentAttendingCourse",
					new Object[] { getStudent(userView), executionCourseId });

		} catch (NotAuthorizedException exception) {
			addActionMessage(request, "error.attend.curricularCourse.impossibleToEnroll");
			return mapping.getInputForward();

		} catch (DomainException e) {
			addActionMessage(request, e.getMessage(), e.getArgs());
			return mapping.getInputForward();

		} catch (FenixServiceException exception) {
			addActionMessage(request, "errors.impossible.operation");
			return mapping.getInputForward();
		}

		return mapping.findForward("prepareShiftEnrollment");
	}

	public ActionForward removeCourses(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixTransactionException,
			FenixFilterException {

		super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

		checkParameter(request);

		final DynaActionForm form = (DynaActionForm) actionForm;
		final Integer executionCourseId = (Integer) form.get("removedCourse");
		if (executionCourseId == null) {
			return mapping.findForward("prepareShiftEnrollment");
		}

		final IUserView userView = getUserView(request);
		final Student student = getStudent(userView);

		try {
			ServiceManagerServiceFactory.executeService(userView, "DeleteStudentAttendingCourse",
					new Object[] { student, executionCourseId });

		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			addActionMessage(request, "errors.impossible.operation");
			return mapping.getInputForward();
		}

		return mapping.findForward("prepareShiftEnrollment");
	}

	public ActionForward proceedToShiftEnrolment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		checkParameter(request);
		final Integer classIdSelected = readClassSelected(request);

		final IUserView userView = getUserView(request);
		final Student student = getStudent(userView);
		request.setAttribute("studentId", student.getIdInternal());

		final ExecutionCourse executionCourse = getExecutionCourse(request);
		final List<SchoolClass> schoolClassesToEnrol = readStudentSchoolClassesToEnrolUsingExecutionCourse(
				request, student, executionCourse);
		request.setAttribute("schoolClassesToEnrol", schoolClassesToEnrol);

		if (schoolClassesToEnrol.isEmpty()) {
			return mapping.findForward("prepareShiftEnrollment");
		}

		
		final SchoolClass schoolClass = setSelectedSchoolClass(request, classIdSelected, schoolClassesToEnrol);

		// ****************************************************************************************
		// TODO: remove after create RenderTimeTableTag
		// ****************************************************************************************

		Object[] args1 = { student, schoolClass, executionCourse };
		List infoClasslessons = (List) ServiceManagerServiceFactory.executeService(userView,
				"ReadClassTimeTableByStudent", args1);

		int endTime = getEndTime(infoClasslessons);
		request.setAttribute("infoClasslessons", infoClasslessons);
		request.setAttribute("infoClasslessonsEndTime", new Integer(endTime));

		List infoLessons = (List) ServiceManagerServiceFactory.executeService(userView,
				"ReadStudentTimeTable", new Object[] { student });

		int endTime2 = getEndTime(infoLessons);
		request.setAttribute("infoLessons", infoLessons);
		request.setAttribute("infoLessonsEndTime", new Integer(endTime2));

		return mapping.findForward("showShiftsToEnroll");
	}

	private SchoolClass setSelectedSchoolClass(HttpServletRequest request, final Integer classIdSelected,
			final List<SchoolClass> schoolClassesToEnrol) {
		
		final SchoolClass schoolClass;
		if (classIdSelected != null) {
			schoolClass = searchSchoolClassFrom(schoolClassesToEnrol, classIdSelected);
		} else { // select first from list
			schoolClass = schoolClassesToEnrol.get(0);
		}
		request.setAttribute("selectedSchoolClass", schoolClass);
		
		return schoolClass;
	}

	private SchoolClass searchSchoolClassFrom(final List<SchoolClass> schoolClassesToEnrol,
			final Integer classId) {
		return (SchoolClass) CollectionUtils.find(schoolClassesToEnrol, new Predicate() {
			public boolean evaluate(Object object) {
				return ((SchoolClass) object).getIdInternal().equals(classId);
			}
		});
	}

	private List<SchoolClass> readStudentSchoolClassesToEnrolUsingExecutionCourse(
			HttpServletRequest request, final Student student, final ExecutionCourse executionCourse) {
		
		final List<SchoolClass> schoolClassesToEnrol = new ArrayList<SchoolClass>();
		if (executionCourse != null) {
			request.setAttribute("executionCourse", executionCourse);
			schoolClassesToEnrol.addAll(student.getSchoolClassesToEnrolBy(executionCourse));

		} else {
			schoolClassesToEnrol.addAll(student.getSchoolClassesToEnrol());
		}
		
		Collections.sort(schoolClassesToEnrol, new BeanComparator("nome"));
		return schoolClassesToEnrol;
	}

	private ExecutionCourse getExecutionCourse(HttpServletRequest request) {
		if (!StringUtils.isEmpty(request.getParameter("executionCourseID"))) {
			return rootDomainObject.readExecutionCourseByOID(Integer.valueOf(request
					.getParameter("executionCourseID")));
		} else {
			return null;
		}
	}

	private int getEndTime(List infoLessons) {
		int endTime = 0;
		for (Iterator iter = infoLessons.iterator(); iter.hasNext();) {
			InfoLesson infoLesson = (InfoLesson) iter.next();
			Calendar end = infoLesson.getFim();
			int tempEnd = end.get(Calendar.HOUR_OF_DAY);
			if (end.get(Calendar.MINUTE) > 0) {
				tempEnd = tempEnd + 1;
			}
			if (endTime < tempEnd) {
				endTime = tempEnd;
			}
		}
		return endTime;
	}

	private void checkParameter(HttpServletRequest request) {
		final String selectCourses = request.getParameter("selectCourses");
		if (selectCourses != null) {
			request.setAttribute("selectCourses", selectCourses);
		}
	}

	private Integer readClassSelected(HttpServletRequest request) {
		String classIdSelectedString = request.getParameter("classId");
		Integer classIdSelected = null;
		if (classIdSelectedString != null) {
			classIdSelected = Integer.valueOf(classIdSelectedString);
		} else {
			classIdSelected = (Integer) request.getAttribute("classId");
		}
		return classIdSelected;
	}

	public ActionForward exitEnrollment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("studentFirstPage");
	}

	public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("prepareEnrollmentViewWarning");
	}

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.addCourse", "addCourses");
		map.put("button.removeCourse", "removeCourses");
		map.put("button.continue.enrolment", "prepareStartViewWarning");
		map.put("button.exit.shift.enrollment", "exitEnrollment");
		map.put("label.class", "proceedToShiftEnrolment");
		map.put("link.shift.enrolement.edit", "proceedToShiftEnrolment");
		map.put("button.clean", "proceedToShiftEnrolment");
		return map;
	}

	private Student getStudent(final IUserView userView) {
		return userView.getPerson().getStudentByUsername();
	}
}