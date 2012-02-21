package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.SearchDegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.util.email.CoordinatorSender;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchExecutionCourseAttendsAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// Integer objectCode =
	// Integer.valueOf(request.getParameter("objectCode"));
	ExecutionCourse executionCourse = getDomainObject(request, "objectCode");
	// ExecutionCourse executionCourse =
	// rootDomainObject.readExecutionCourseByOID(objectCode);
	SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean = readSearchBean(request, executionCourse);

	executionCourse.searchAttends(searchExecutionCourseAttendsBean);
	request.setAttribute("searchBean", searchExecutionCourseAttendsBean);
	request.setAttribute("executionCourse", searchExecutionCourseAttendsBean.getExecutionCourse());

	prepareAttendsCollectionPages(request, searchExecutionCourseAttendsBean, executionCourse);

	return mapping.findForward("search");
    }

    private SearchExecutionCourseAttendsBean readSearchBean(HttpServletRequest request, ExecutionCourse executionCourse) {
	String executionCourseID = request.getParameter("executionCourse");
	Integer executionCourseIDInteger = (executionCourseID == null) ? null : Integer.parseInt(executionCourseID);
	if (executionCourseIDInteger != null) {
	    SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean = new SearchExecutionCourseAttendsBean(
		    rootDomainObject.readExecutionCourseByOID(executionCourseIDInteger));

	    String viewPhoto = request.getParameter("viewPhoto");
	    if (viewPhoto != null && viewPhoto.equalsIgnoreCase("true")) {
		searchExecutionCourseAttendsBean.setViewPhoto(true);
	    } else {
		searchExecutionCourseAttendsBean.setViewPhoto(false);
	    }

	    String attendsStates = request.getParameter("attendsStates");
	    if (attendsStates != null) {
		List<StudentAttendsStateType> list = new ArrayList<StudentAttendsStateType>();
		for (String attendsState : attendsStates.split(":")) {
		    list.add(StudentAttendsStateType.valueOf(attendsState));
		}
		searchExecutionCourseAttendsBean.setAttendsStates(list);
	    }

	    String workingStudentTypes = request.getParameter("workingStudentTypes");
	    if (workingStudentTypes != null) {
		List<WorkingStudentSelectionType> list = new ArrayList<WorkingStudentSelectionType>();
		for (String workingStudentType : workingStudentTypes.split(":")) {
		    list.add(WorkingStudentSelectionType.valueOf(workingStudentType));
		}
		searchExecutionCourseAttendsBean.setWorkingStudentTypes(list);
	    }

	    String degreeCurricularPlans = request.getParameter("degreeCurricularPlans");
	    if (degreeCurricularPlans != null) {
		List<DegreeCurricularPlan> list = new ArrayList<DegreeCurricularPlan>();
		for (String degreeCurricularPlan : degreeCurricularPlans.split(":")) {
		    list.add(rootDomainObject.readDegreeCurricularPlanByOID(Integer.parseInt(degreeCurricularPlan)));
		}
		searchExecutionCourseAttendsBean.setDegreeCurricularPlans(list);
	    }

	    String shifts = request.getParameter("shifts");
	    if (shifts != null) {
		List<Shift> list = new ArrayList<Shift>();
		for (String shift : shifts.split(":")) {
		    list.add(rootDomainObject.readShiftByOID(Integer.parseInt(shift)));
		}
		searchExecutionCourseAttendsBean.setShifts(list);
	    }

	    return searchExecutionCourseAttendsBean;
	} else {
	    return new SearchExecutionCourseAttendsBean(executionCourse);
	}
    }

    private void prepareAttendsCollectionPages(HttpServletRequest request,
	    SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean, ExecutionCourse executionCourse) {
	Collection<Attends> executionCourseAttends = searchExecutionCourseAttendsBean.getAttendsResult();
	List<Attends> listExecutionCourseAttends = new ArrayList<Attends>(executionCourseAttends);
	Collections.sort(listExecutionCourseAttends, Attends.COMPARATOR_BY_STUDENT_NUMBER);
	final CollectionPager<Attends> pager = new CollectionPager<Attends>(listExecutionCourseAttends, 50);
	request.setAttribute("numberOfPages", (listExecutionCourseAttends.size() / 50) + 1);

	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
	request.setAttribute("pageNumber", page);

	SearchExecutionCourseAttendsBean attendsPagesBean = new SearchExecutionCourseAttendsBean(executionCourse);

	executionCourse.searchAttends(attendsPagesBean);

	Map<Integer, Integer> enrolmentsNumberMap = new HashMap<Integer, Integer>();
	for (Attends attends : pager.getCollection()) {
	    executionCourse.addAttendsToEnrolmentNumberMap(attends, enrolmentsNumberMap);
	}
	attendsPagesBean.setEnrolmentsNumberMap(enrolmentsNumberMap);
	attendsPagesBean.setAttendsResult(pager.getPage(page));
	if (searchExecutionCourseAttendsBean.getViewPhoto()) {
	    attendsPagesBean.setViewPhoto(true);
	}
	request.setAttribute("attendsPagesBean", attendsPagesBean);
    }

    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ExecutionCourse executionCourse;
	Group studentsGroup = null;
	String label;
	Sender sender;
	SearchExecutionCourseAttendsBean bean = getRenderedObject("mailViewState");
	if (bean != null) {
	    executionCourse = bean.getExecutionCourse();
	    studentsGroup = bean.getAttendsGroup();
	    label = bean.getLabel();
	    sender = ExecutionCourseSender.newInstance(executionCourse);
	} else {
	    SearchDegreeStudentsGroup degreeStudentsGroup = (SearchDegreeStudentsGroup) Group
		    .fromStringinHex((String) getFromRequestOrForm(request, (DynaActionForm) form, "searchGroup"));
	    label = degreeStudentsGroup.getLabel();
	    String executionDegreeId = (String) getFromRequestOrForm(request, (DynaActionForm) form, "executionDegreeId");
	    studentsGroup = degreeStudentsGroup;
	    ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(executionDegreeId));
	    sender = CoordinatorSender.newInstance(executionDegree.getDegree());
	}

	Recipient recipient = Recipient.newInstance(label, studentsGroup);
	return EmailsDA.sendEmail(request, sender, recipient);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("objectCode", request.getAttribute("objectCode"));

	SearchExecutionCourseAttendsBean bean = getRenderedObject();
	RenderUtils.invalidateViewState();
	bean.getExecutionCourse().searchAttends(bean);

	request.setAttribute("searchBean", bean);
	request.setAttribute("executionCourse", bean.getExecutionCourse());

	prepareAttendsCollectionPages(request, bean, bean.getExecutionCourse());

	return mapping.findForward("search");
    }

}
