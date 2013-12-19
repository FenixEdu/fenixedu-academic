package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.CreateTSDCurricularCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.CreateTSDCurricularCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.DeleteTSDCurricularCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.SetTSDCourseType;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

public class TSDCourseAction extends FenixDispatchAction {

    public ActionForward prepareForTSDCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
        TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

        initializeVariables(userView.getPerson(), dynaForm, tsdProcess.getCurrentTSDProcessPhase());
        return loadTSDCourses(mapping, form, request, response);
    }

    public ActionForward loadTeacherServiceDistribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;

        dynaForm.set("competenceCourse", null);
        dynaForm.set("tsdCurricularCourse", null);
        dynaForm.set("tsdCurricularCourseGroup", null);

        dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());

        return loadTSDCourses(mapping, form, request, response);
    }

    public ActionForward loadCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDCourse selectedTSDCourse = getSelectedTSDCourse(userView, dynaForm, null);

        TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);
        ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

        dynaForm.set("tsdCourseType", tsd.getTSDCourseType(selectedTSDCourse, selectedExecutionPeriod).toString());
        dynaForm.set("tsdCurricularCourse", null);
        dynaForm.set("tsdCurricularCourseGroup", null);

        return loadTSDCourses(mapping, form, request, response);
    }

    public ActionForward loadExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;

        dynaForm.set("competenceCourse", null);
        dynaForm.set("tsdCurricularCourse", null);
        dynaForm.set("tsdCurricularCourseGroup", null);

        dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());

        return loadTSDCourses(mapping, form, request, response);
    }

    public ActionForward setTSDCourseType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        CompetenceCourse selectedCompetenceCourse = getSelectedTSDCourse(userView, dynaForm, null).getCompetenceCourse();
        TSDCourseType selectedTSDCourseType = getSelectedTSDCourseType(dynaForm);
        ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
        TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);

        SetTSDCourseType.runSetTSDCourseType(selectedCompetenceCourse.getExternalId(), tsd.getExternalId(),
                selectedExecutionPeriod.getExternalId(), selectedTSDCourseType.toString());

        dynaForm.set("tsdCurricularCourse", null);
        dynaForm.set("tsdCurricularCourseGroup", null);

        return loadTSDCourses(mapping, form, request, response);
    }

    public ActionForward prepareForTSDCurricularCourseGroupCreation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDProcessPhase currentTSDProcessPhase = getTSDProcess(userView, dynaForm).getCurrentTSDProcessPhase();
        CompetenceCourse selectedCompetenceCourse = getSelectedTSDCourse(userView, dynaForm, null).getCompetenceCourse();
        TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
        ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

        createDefaultTSDCurricularCourses(userView, selectedTeacherServiceDistribution, selectedCompetenceCourse,
                currentTSDProcessPhase, selectedExecutionPeriod, false);

        List<TSDCurricularCourse> availableTSDCurricularCourseToGroupList =
                selectedTeacherServiceDistribution.getTSDCurricularCoursesWithoutTSDCourseGroup(selectedCompetenceCourse,
                        selectedExecutionPeriod);

        request.setAttribute("availableTSDCurricularCourseToGroupList", availableTSDCurricularCourseToGroupList);
        request.setAttribute("tsdProcess", getTSDProcess(userView, dynaForm));

        return mapping.findForward("tsdCurricularCourseGroupCreationForm");
    }

    public ActionForward createTSDCurricularCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);
        String[] tsdCurricularCourseToGroupArray = obtainAssociatedTSDCurricularCoursesFromDynaActionForm(dynaForm);

        if (tsdCurricularCourseToGroupArray.length > 0) {
            TSDCurricularCourseGroup tsdCurricularCourseGroup =
                    CreateTSDCurricularCourseGroup.runCreateTSDCurricularCourseGroup(tsd.getExternalId(),
                            tsdCurricularCourseToGroupArray);

            dynaForm.set("tsdCurricularCourseGroup", tsdCurricularCourseGroup.getExternalId());
        }

        return loadTSDCourses(mapping, form, request, response);
    }

    private String[] obtainAssociatedTSDCurricularCoursesFromDynaActionForm(DynaActionForm dynaForm) {
        return (String[]) dynaForm.get("tsdCurricularCourseArray");
    }

    public ActionForward deleteTSDCurricularCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = getSelectedTSDCurricularCourseGroup(dynaForm, null);

        DeleteTSDCurricularCourseGroup.runDeleteTSDCurricularCourseGroup(selectedTSDCurricularCourseGroup.getExternalId());

        dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());
        dynaForm.set("tsdCurricularCourseGroup", null);

        return loadTSDCourses(mapping, form, request, response);
    }

    public ActionForward loadTSDCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
        TSDProcessPhase currentTSDProcessPhase = getTSDProcess(userView, dynaForm).getCurrentTSDProcessPhase();
        List<ExecutionSemester> executionPeriodList = getOrderedExecutionPeriods(userView, dynaForm);
        ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, executionPeriodList);

        List<TSDCourse> activeTSDCourseList = getActiveTSDCourseList(selectedTeacherServiceDistribution, selectedExecutionPeriod);

        TSDCourseType selectedTSDCourseType = getSelectedTSDCourseType(dynaForm);

        if (!activeTSDCourseList.isEmpty()) {
            Collections.sort(activeTSDCourseList, new BeanComparator("name"));

            TSDCourse selectedTSDCompetenceCourse = getSelectedTSDCourse(userView, dynaForm, activeTSDCourseList);

            if (selectedTSDCourseType == TSDCourseType.NOT_DETERMINED) {
                selectedTSDCourseType =
                        selectedTeacherServiceDistribution.getTSDCourseType(selectedTSDCompetenceCourse, selectedExecutionPeriod);
            }

            List<TSDCurricularCourse> tsdCurricularCourseList = null;
            TSDCurricularCourse selectedTSDCurricularCourse = null;
            List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList = null;
            TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = null;

            if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
                createDefaultTSDCurricularCourses(userView, selectedTeacherServiceDistribution,
                        selectedTSDCompetenceCourse.getCompetenceCourse(), currentTSDProcessPhase, selectedExecutionPeriod, true);

                tsdCurricularCourseList =
                        selectedTeacherServiceDistribution.getTSDCurricularCourses(
                                selectedTSDCompetenceCourse.getCompetenceCourse(), selectedExecutionPeriod);
                selectedTSDCurricularCourse = getSelectedTSDCurricularCourse(dynaForm, tsdCurricularCourseList);

            } else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
                createDefaultTSDCurricularCourses(userView, selectedTeacherServiceDistribution,
                        selectedTSDCompetenceCourse.getCompetenceCourse(), currentTSDProcessPhase, selectedExecutionPeriod, false);

                tsdCurricularCourseGroupList =
                        selectedTeacherServiceDistribution.getTSDCurricularCourseGroups(
                                selectedTSDCompetenceCourse.getCompetenceCourse(), selectedExecutionPeriod);
                selectedTSDCurricularCourseGroup = getSelectedTSDCurricularCourseGroup(dynaForm, tsdCurricularCourseGroupList);
            }

            fillFormAndRequest(request, dynaForm, currentTSDProcessPhase, selectedTeacherServiceDistribution,
                    selectedTSDCourseType, activeTSDCourseList, selectedTSDCompetenceCourse, tsdCurricularCourseList,
                    selectedTSDCurricularCourse, tsdCurricularCourseGroupList, selectedTSDCurricularCourseGroup,
                    selectedExecutionPeriod);
        } else {
            request.setAttribute("notAvailableCompetenceCourses", true);
        }

        List<TeacherServiceDistributionDTOEntry> tsdOptionEntryList =
                TeacherServiceDistributionDTOEntry.getTeacherServiceDistributionOptionEntriesForPerson(currentTSDProcessPhase,
                        userView.getPerson(), false, true);

        request.setAttribute("tsdProcess", getTSDProcess(userView, dynaForm));
        request.setAttribute("tsdOptionEntryList", tsdOptionEntryList);
        request.setAttribute("executionPeriodList", executionPeriodList);
        return mapping.findForward("tsdCourseForm");
    }

    private void createDefaultTSDCurricularCourses(User userView, TeacherServiceDistribution tsd,
            CompetenceCourse competenceCourse, TSDProcessPhase currentTSDProcessPhase, ExecutionSemester selectedExecutionPeriod,
            Boolean activateCourses) throws FenixServiceException {
        CreateTSDCurricularCourses.runCreateTSDCurricularCourses(tsd.getExternalId(), competenceCourse.getExternalId(),
                currentTSDProcessPhase.getExternalId(), selectedExecutionPeriod.getExternalId(), activateCourses);
    }

    private List<ExecutionSemester> getOrderedExecutionPeriods(User userView, DynaActionForm dynaForm)
            throws FenixServiceException {
        List<ExecutionSemester> executionPeriodList =
                new ArrayList<ExecutionSemester>(getTSDProcess(userView, dynaForm).getExecutionPeriods());
        Collections.sort(executionPeriodList, new BeanComparator("semester"));
        return executionPeriodList;
    }

    private void fillFormAndRequest(HttpServletRequest request, DynaActionForm dynaForm, TSDProcessPhase tsdProcessPhase,
            TeacherServiceDistribution selectedTeacherServiceDistribution, TSDCourseType selectedTSDCourseType,
            List<TSDCourse> tsdCourseList, TSDCourse selectedTSDCompetenceCourse,
            List<TSDCurricularCourse> tsdCurricularCourseList, TSDCurricularCourse selectedTSDCurricularCourse,
            List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList,
            TSDCurricularCourseGroup selectedTSDCurricularCourseGroup, ExecutionSemester selectedExecutionPeriod) {

        request.setAttribute("selectedTSD", selectedTeacherServiceDistribution);
        request.setAttribute("competenceCourseList", tsdCourseList);
        request.setAttribute("selectedCompetenceCourse", selectedTSDCompetenceCourse);
        request.setAttribute("tsdCurricularCourseList", tsdCurricularCourseList);
        request.setAttribute("selectedCurricularCourse", selectedTSDCurricularCourse);
        request.setAttribute("tsdCurricularCourseGroupList", tsdCurricularCourseGroupList);
        request.setAttribute("selectedTSDCurricularCourseGroup", selectedTSDCurricularCourseGroup);
        request.setAttribute("selectedTSDCourseType", selectedTSDCourseType.toString());

        dynaForm.set("tsdCourseType", selectedTSDCourseType.toString());
        dynaForm.set("competenceCourse", selectedTSDCompetenceCourse.getExternalId());

        if (selectedTSDCurricularCourse != null) {
            dynaForm.set("tsdCurricularCourse", selectedTSDCurricularCourse.getExternalId());
        }
        if (selectedTSDCurricularCourseGroup != null) {
            dynaForm.set("tsdCurricularCourseGroup", selectedTSDCurricularCourseGroup.getExternalId());
        }

        TSDCourse selectedTSDCourse = null;
        if (selectedTSDCourseType == TSDCourseType.COMPETENCE_COURSE_VALUATION) {
            selectedTSDCourse = selectedTSDCompetenceCourse;
        } else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
            selectedTSDCourse = selectedTSDCurricularCourse;
        } else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
            selectedTSDCourse = selectedTSDCurricularCourseGroup;

            request.setAttribute(
                    "availableTSDCurricularCoursesToGroup",
                    selectedTeacherServiceDistribution.getTSDCurricularCoursesWithoutTSDCourseGroup(
                            selectedTSDCompetenceCourse.getCompetenceCourse(), selectedExecutionPeriod));
        }

        if (selectedTSDCourse == null) {
            request.setAttribute("tsdCourseNotSelected", true);
        } else {
            request.setAttribute("selectedTSDCourse", selectedTSDCourse);
        }

        if (selectedTSDCompetenceCourse instanceof TSDVirtualCourseGroup) {
            request.setAttribute("tsdVirtualCourseGroup", true);
        }

    }

    private TSDCurricularCourseGroup getSelectedTSDCurricularCourseGroup(DynaActionForm dynaForm,
            List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList) throws FenixServiceException {
        TSDCurricularCourseGroup selectedTSDCurricularCourseGroup =
                FenixFramework.getDomainObject((String) dynaForm.get("tsdCurricularCourseGroup"));

        if (selectedTSDCurricularCourseGroup == null) {
            if (tsdCurricularCourseGroupList != null && !tsdCurricularCourseGroupList.isEmpty()) {
                return tsdCurricularCourseGroupList.iterator().next();
            } else {
                return null;
            }
        } else {
            return selectedTSDCurricularCourseGroup;
        }
    }

    private TSDCurricularCourse getSelectedTSDCurricularCourse(DynaActionForm dynaForm,
            List<TSDCurricularCourse> tsdCurricularCourseList) throws FenixServiceException {
        TSDCurricularCourse selectedTSDCurricularCourse =
                (TSDCurricularCourse) FenixFramework.getDomainObject((String) dynaForm.get("tsdCurricularCourse"));

        if (selectedTSDCurricularCourse == null) {
            if (tsdCurricularCourseList != null && !tsdCurricularCourseList.isEmpty()) {
                return tsdCurricularCourseList.iterator().next();
            } else {
                return null;
            }
        } else {
            return selectedTSDCurricularCourse;
        }
    }

    private TSDCourse getSelectedTSDCourse(User userView, DynaActionForm dynaForm, List<TSDCourse> competenceCourseList)
            throws FenixServiceException {
        TSDCourse selectedTSDCourse = FenixFramework.getDomainObject((String) dynaForm.get("competenceCourse"));

        if (selectedTSDCourse == null) {
            if (competenceCourseList != null && !competenceCourseList.isEmpty()) {
                return competenceCourseList.iterator().next();
            } else {
                return null;
            }
        } else {
            return selectedTSDCourse;
        }
    }

    private TSDCourseType getSelectedTSDCourseType(DynaActionForm dynaForm) {
        return ((String) dynaForm.get("tsdCourseType") == null) ? TSDCourseType.NOT_DETERMINED : TSDCourseType
                .valueOf((String) dynaForm.get("tsdCourseType"));
    }

    private List<TSDCourse> getActiveTSDCourseList(TeacherServiceDistribution selectedTeacherServiceDistribution,
            ExecutionSemester executionSemester) {
        return new ArrayList<TSDCourse>(
                selectedTeacherServiceDistribution.getTSDCompetenceAndVirtualCoursesByExecutionPeriod(executionSemester));
    }

    private void initializeVariables(Person person, DynaActionForm dynaForm, TSDProcessPhase phase) {
        List<TeacherServiceDistributionDTOEntry> tsdOptionEntryList =
                TeacherServiceDistributionDTOEntry
                        .getTeacherServiceDistributionOptionEntriesForPerson(phase, person, false, true);

        dynaForm.set("tsd", tsdOptionEntryList.iterator().next().getTeacherServiceDistribution().getExternalId());
        dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());
        dynaForm.set("competenceCourse", null);
        dynaForm.set("tsdCurricularCourse", null);
        dynaForm.set("tsdCurricularCourseGroup", null);
    }

    private String getFromRequestAndSetOnFormTSDProcessId(HttpServletRequest request, DynaActionForm dynaForm) {
        dynaForm.set("tsdProcess", request.getParameter("tsdProcess"));
        return request.getParameter("tsdProcess");
    }

    private TeacherServiceDistribution getSelectedTeacherServiceDistribution(DynaActionForm dynaForm)
            throws FenixServiceException {
        return FenixFramework.getDomainObject((String) dynaForm.get("tsd"));
    }

    private TSDProcess getTSDProcess(User userView, DynaActionForm dynaForm) {
        return FenixFramework.getDomainObject((String) dynaForm.get("tsdProcess"));
    }

    private ExecutionSemester getSelectedExecutionPeriod(User userView, DynaActionForm dynaForm,
            List<ExecutionSemester> executionPeriodList) throws FenixServiceException {
        ExecutionSemester selectedExecutionPeriod = FenixFramework.getDomainObject((String) dynaForm.get("executionPeriod"));

        if (selectedExecutionPeriod == null) {
            if (executionPeriodList != null && executionPeriodList.size() > 0) {
                return executionPeriodList.iterator().next();
            } else {
                return null;
            }
        }

        return selectedExecutionPeriod;
    }

    public ActionForward prepareLinkForTSDCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();

        DynaActionForm dynaForm = (DynaActionForm) form;
        getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
        TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

        initializeVariables(userView.getPerson(), dynaForm, tsdProcess.getCurrentTSDProcessPhase());

        String selectedTSDCourseId = request.getParameter("tsdCourse");
        TSDCourse tsdCourse = FenixFramework.getDomainObject(selectedTSDCourseId);

        if (tsdCourse instanceof TSDCurricularCourse) {
            dynaForm.set("tsdCurricularCourse", tsdCourse.getExternalId());

        } else if (tsdCourse instanceof TSDCurricularCourseGroup) {
            dynaForm.set("tsdCurricularCourseGroup", tsdCourse.getExternalId());

        }

        dynaForm.set("tsd", request.getParameter("tsd"));
        TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);

        ExecutionSemester period = tsdCourse.getExecutionPeriod();
        dynaForm.set("executionPeriod", period.getExternalId());

        if (tsdCourse instanceof TSDCompetenceCourse || tsdCourse instanceof TSDVirtualCourseGroup) {
            dynaForm.set("competenceCourse", tsdCourse.getExternalId());
        } else {
            dynaForm.set("competenceCourse",
                    tsd.getTSDCompetenceCourse(tsdCourse.getCompetenceCourse(), tsdCourse.getExecutionPeriod()).getExternalId());
        }

        if (request.getParameter("notTSDCourseViewLink") == null) {
            dynaForm.set("tsdCourseViewLink", selectedTSDCourseId.toString());
        }

        return loadTSDCourses(mapping, form, request, response);
    }
}
