/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.domain.accessControl.SearchDegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.util.email.CoordinatorSender;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/searchECAttends", module = "teacher", functionality = ManageExecutionCourseDA.class)
public class SearchExecutionCourseAttendsAction extends ExecutionCourseBaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Integer objectCode =
        // Integer.valueOf(request.getParameter("objectCode"));
        ExecutionCourse executionCourse = getExecutionCourse(request);
        // ExecutionCourse executionCourse =
        // FenixFramework.getDomainObject(objectCode);
        SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean = readSearchBean(request, executionCourse);

        executionCourse.searchAttends(searchExecutionCourseAttendsBean);
        request.setAttribute("searchBean", searchExecutionCourseAttendsBean);
        request.setAttribute("executionCourse", searchExecutionCourseAttendsBean.getExecutionCourse());

        prepareAttendsCollectionPages(request, searchExecutionCourseAttendsBean, executionCourse);

        return forward(request, "/teacher/viewAttendsSearch.jsp");
    }

    private SearchExecutionCourseAttendsBean readSearchBean(HttpServletRequest request, ExecutionCourse executionCourse) {
        String executionCourseID = request.getParameter("executionCourse");
        SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean;
        if (executionCourseID != null) {
            searchExecutionCourseAttendsBean =
                    new SearchExecutionCourseAttendsBean(FenixFramework.<ExecutionCourse> getDomainObject(executionCourseID));
        } else {
            searchExecutionCourseAttendsBean = new SearchExecutionCourseAttendsBean(executionCourse);
        }
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
                list.add(FenixFramework.<DegreeCurricularPlan> getDomainObject(degreeCurricularPlan));
            }
            searchExecutionCourseAttendsBean.setDegreeCurricularPlans(list);
        }

        String shifts = request.getParameter("shifts");
        if (shifts != null) {
            List<Shift> list = new ArrayList<Shift>();
            for (String shift : shifts.split(":")) {
                if (!StringUtils.isEmpty(shift)) {
                    list.add(FenixFramework.<Shift> getDomainObject(shift));
                }
            }
            searchExecutionCourseAttendsBean.setShifts(list);
        }

        return searchExecutionCourseAttendsBean;
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
            SearchDegreeStudentsGroup degreeStudentsGroup =
                    SearchDegreeStudentsGroup.parse((String) getFromRequestOrForm(request, (DynaActionForm) form, "searchGroup"));
            label = degreeStudentsGroup.getLabel();
            String executionDegreeId = (String) getFromRequestOrForm(request, (DynaActionForm) form, "executionDegreeId");
            studentsGroup = degreeStudentsGroup.getUserGroup();
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
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

        return forward(request, "/teacher/viewAttendsSearch.jsp");
    }

}
