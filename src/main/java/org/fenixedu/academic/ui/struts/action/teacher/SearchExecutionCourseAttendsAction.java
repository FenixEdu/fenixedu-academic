/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.teacher;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Attends.StudentAttendsStateType;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.accessControl.SearchDegreeStudentsGroup;
import org.fenixedu.academic.dto.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseBaseAction;
import org.fenixedu.academic.util.CollectionPager;
import org.fenixedu.academic.util.WorkingStudentSelectionType;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.groups.NamedGroup;
import org.fenixedu.messaging.core.ui.MessageBean;
import org.fenixedu.messaging.core.ui.MessagingUtils;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    new SearchExecutionCourseAttendsBean(FenixFramework.getDomainObject(executionCourseID));
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
            List<StudentAttendsStateType> list = new ArrayList<>();
            for (String attendsState : attendsStates.split(":")) {
                list.add(StudentAttendsStateType.valueOf(attendsState));
            }
            searchExecutionCourseAttendsBean.setAttendsStates(list);
        }

        String workingStudentTypes = request.getParameter("workingStudentTypes");
        if (workingStudentTypes != null) {
            List<WorkingStudentSelectionType> list = new ArrayList<>();
            for (String workingStudentType : workingStudentTypes.split(":")) {
                list.add(WorkingStudentSelectionType.valueOf(workingStudentType));
            }
            searchExecutionCourseAttendsBean.setWorkingStudentTypes(list);
        }

        String degreeCurricularPlans = request.getParameter("degreeCurricularPlans");
        if (degreeCurricularPlans != null) {
            List<DegreeCurricularPlan> list = new ArrayList<>();
            for (String degreeCurricularPlan : degreeCurricularPlans.split(":")) {
                list.add(FenixFramework.getDomainObject(degreeCurricularPlan));
            }
            searchExecutionCourseAttendsBean.setDegreeCurricularPlans(list);
        }

        String shifts = request.getParameter("shifts");
        if (shifts != null) {
            List<Shift> list = new ArrayList<>();
            for (String shift : shifts.split(":")) {
                if (!StringUtils.isEmpty(shift)) {
                    list.add(FenixFramework.getDomainObject(shift));
                }
            }
            searchExecutionCourseAttendsBean.setShifts(list);
        }

        return searchExecutionCourseAttendsBean;
    }

    private void prepareAttendsCollectionPages(HttpServletRequest request,
            SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean, ExecutionCourse executionCourse) {
        Collection<Attends> executionCourseAttends = searchExecutionCourseAttendsBean.getAttendsResult();
        List<Attends> listExecutionCourseAttends = new ArrayList<>(executionCourseAttends);
        listExecutionCourseAttends.sort(Attends.COMPARATOR_BY_STUDENT_NUMBER);
        final CollectionPager<Attends> pager = new CollectionPager<>(listExecutionCourseAttends, 50);
        request.setAttribute("numberOfPages", (listExecutionCourseAttends.size() / 50) + 1);

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);

        SearchExecutionCourseAttendsBean attendsPagesBean = new SearchExecutionCourseAttendsBean(executionCourse);

        executionCourse.searchAttends(attendsPagesBean);

        Map<Integer, Integer> enrolmentsNumberMap = new HashMap<>();
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
            HttpServletResponse response) throws IOException {
        MessageBean messageBean = new MessageBean();

        SearchExecutionCourseAttendsBean bean = getRenderedObject("mailViewState");
        SearchDegreeStudentsGroup degreeStudentsGroup =
                SearchDegreeStudentsGroup.parse((String) getFromRequestOrForm(request, (DynaActionForm) form, "searchGroup"));

        if (bean != null) {
            NamedGroup attendsGroup = new NamedGroup(new LocalizedString(I18N.getLocale(),bean.getLabel()), bean.getAttendsGroup());
            messageBean.setLockedSender(bean.getExecutionCourse().getSender());
            messageBean.addAdHocRecipient(attendsGroup);
            messageBean.selectRecipient(attendsGroup);
        }
        else {

            NamedGroup degreeStudents = new NamedGroup(new LocalizedString(I18N.getLocale(),degreeStudentsGroup.getLabel()), degreeStudentsGroup.getUserGroup());

            String executionDegreeId = (String) getFromRequestOrForm(request, (DynaActionForm) form, "executionDegreeId");
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            messageBean.setLockedSender(executionDegree.getDegree().getSender());
            messageBean.addAdHocRecipient(degreeStudents);
            messageBean.selectRecipient(degreeStudents);
        }

        return MessagingUtils.redirectToNewMessage(request, response, messageBean);
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
