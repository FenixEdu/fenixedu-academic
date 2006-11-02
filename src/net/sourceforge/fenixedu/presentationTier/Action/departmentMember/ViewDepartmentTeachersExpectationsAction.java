package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author naat
 * 
 */

public class ViewDepartmentTeachersExpectationsAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);
	Integer currentExecutionYearID = getCurrentExecutionYearID(userView);

	preparePageData(request, userView, form, currentExecutionYearID, null);

	return mapping.findForward("showTeacherExpectations");

    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	preparePageData(request, getUserView(request), form, getExecutionYearID(form, request), getTeacherID(form, request));

	return mapping.findForward("print");

    }

    public ActionForward changeExecutionYear(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	preparePageData(request, getUserView(request), form, getExecutionYearID(form, request), null);

	return mapping.findForward("showTeacherExpectations");

    }

    private Integer getExecutionYearID(ActionForm form, HttpServletRequest request) {
	if (request.getParameter("executionYearID") != null) {
	    return getRequestParameterAsInteger(request, "executionYearID");
	} else {
	    return (Integer) ((DynaActionForm) form).get("executionYearID");
	}
    }

    public ActionForward changeTeacher(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	preparePageData(request, getUserView(request), form, getExecutionYearID(form, request),
		getTeacherID(form, request));

	return mapping.findForward("showTeacherExpectations");

    }

    private Integer getTeacherID(ActionForm form, HttpServletRequest request) {
	Integer teacherID;
	if (request.getParameter("teacherID") != null) {
	    teacherID = getRequestParameterAsInteger(request, "teacherID");
	} else {
	    teacherID = (Integer) ((DynaActionForm) form).get("teacherID");
	}

	if (teacherID == -1) {
	    teacherID = null;
	}

	return teacherID;

    }

    private void preparePageData(HttpServletRequest request, IUserView userView, ActionForm form,
	    Integer executionYearID, Integer teacherID) throws FenixFilterException,
	    FenixServiceException {

	List<LabelValueBean> executionYears = getExecutionYears(userView);
	Integer executionYearToReadData = null;

	if (executionYearID == null && executionYears.size() != 0) {
	    executionYearToReadData = Integer.valueOf(executionYears.get(0).getValue());
	} else {
	    executionYearToReadData = executionYearID;
	}

	List<LabelValueBean> departmentTeachers = getDepartmentTeachers(userView,
		executionYearToReadData);
	List<TeacherPersonalExpectation> teacherPersonalExpectations = searchDepartmentTeachersPersonalExpectations(
		userView, executionYearToReadData, teacherID);

	request.setAttribute("executionYears", executionYears);
	request.setAttribute("departmentTeachers", departmentTeachers);
	request.setAttribute("teacherPersonalExpectations", teacherPersonalExpectations);
	request.setAttribute("executionYearID", executionYearToReadData);

	((DynaActionForm) form).set("executionYearID", executionYearToReadData);

	if (teacherID == null) {
	    ((DynaActionForm) form).set("teacherID", -1);
	} else {
	    ((DynaActionForm) form).set("teacherID", teacherID);
	}

    }

    private Integer getCurrentExecutionYearID(IUserView userView) throws FenixFilterException,
	    FenixServiceException {

	InfoExecutionYear infoExecutionYear;

	infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
		"ReadCurrentExecutionYear", new Object[] {});

	return infoExecutionYear.getIdInternal();

    }

    private List<LabelValueBean> getExecutionYears(IUserView userView) throws FenixFilterException,
	    FenixServiceException {

	List<LabelValueBean> result = new ArrayList<LabelValueBean>();

	List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils.executeService(
		userView, "ReadNotClosedExecutionYears", null);

	for (InfoExecutionYear infoExecutionYear : executionYears) {
	    result.add(new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getIdInternal()
		    .toString()));
	}

	return result;

    }

    private List<LabelValueBean> getDepartmentTeachers(IUserView userView, Integer executionYearID)
	    throws FenixFilterException, FenixServiceException {

	if (executionYearID == null) {
	    return new ArrayList<LabelValueBean>();
	}

	List<LabelValueBean> result = new ArrayList<LabelValueBean>();

	List<Teacher> departmentTeachers = new ArrayList<Teacher>((List<Teacher>) ServiceUtils
		.executeService(userView, "ReadDepartmentTeachersByDepartmentIDAndExecutionYearID",
			new Object[] { getDepartment(userView).getIdInternal(), executionYearID }));

	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("person.name"));

	Collections.sort(departmentTeachers, comparatorChain);

	for (Teacher teacher : departmentTeachers) {
	    result.add(new LabelValueBean(teacher.getPerson().getNome(), teacher.getIdInternal()
		    .toString()));

	}

	return result;
    }

    private Department getDepartment(IUserView userView) {
	return userView.getPerson().getTeacher().getLastWorkingDepartment();

    }

    private List<TeacherPersonalExpectation> searchDepartmentTeachersPersonalExpectations(
	    IUserView userView, Integer executionYearID, Integer teacherID) throws FenixFilterException,
	    FenixServiceException {

	if (executionYearID == null) {
	    return new ArrayList<TeacherPersonalExpectation>();
	}

	List<TeacherPersonalExpectation> result = new ArrayList<TeacherPersonalExpectation>(
		(List<TeacherPersonalExpectation>) ServiceUtils.executeService(userView,
			"SearchDepartmentTeachersExpectationsByExecutionYearIDAndTeacherID",
			new Object[] { getDepartment(userView).getIdInternal(), executionYearID,
				teacherID }));

	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("teacher.person.name"));

	Collections.sort(result, comparatorChain);

	return result;
    }

}