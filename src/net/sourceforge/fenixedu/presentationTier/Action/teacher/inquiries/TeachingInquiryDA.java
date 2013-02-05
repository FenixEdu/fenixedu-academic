/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiriesCourseResultBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ViewTeacherInquiryPublicResults;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/teachingInquiry", module = "teacher")
@Forwards({
        @Forward(name = "inquiryPrePage", path = "/teacher/inquiries/inquiryPrePage.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp",
                title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "inquiryResultsResume", path = "/teacher/inquiries/inquiryResultsResume.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp",
                title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "inquiriesClosed", path = "/teacher/inquiries/inquiriesClosed.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp",
                title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "inquiryUnavailable", path = "/teacher/inquiries/inquiryUnavailable.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp",
                title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "teacherInquiry", path = "/teacher/inquiries/teacherInquiry.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp",
                title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "showCourseInquiryResult", path = "/inquiries/showCourseInquiryResult.jsp", useTile = false,
                tileProperties = @Tile(title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "showCourseInquiryResult_v2", path = "/inquiries/showCourseInquiryResult_v2.jsp", useTile = false,
                tileProperties = @Tile(title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "showTeachingInquiryResult", path = "/inquiries/showTeachingInquiryResult.jsp", useTile = false,
                tileProperties = @Tile(title = "private.teacher.qucreportsandresults.teachers")),
        @Forward(name = "showTeachingInquiryResult_v2", path = "/inquiries/showTeachingInquiryResult_v2.jsp", useTile = false,
                tileProperties = @Tile(title = "private.teacher.qucreportsandresults.teachers")) })
public class TeachingInquiryDA extends FenixDispatchAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
        Professorship professorship = getProfessorship(executionCourse);

        ExecutionSemester oldQucExecutionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2009/2010");
        if (!executionCourse.getExecutionPeriod().isAfter(oldQucExecutionSemester)) {
            if (executionCourse.getExecutionPeriod().getTeachingInquiryResponsePeriod() == null) {
                return actionMapping.findForward("inquiriesClosed");
            }

            if (!executionCourse.isAvailableForInquiry()) {
                return actionMapping.findForward("inquiryUnavailable");
            }

            request.setAttribute("professorship", professorship);
            request.setAttribute("studentInquiriesCourseResults", populateStudentInquiriesCourseResults(professorship));
            request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());

            return actionMapping.findForward("inquiryPrePage");
        }

        TeacherInquiryTemplate inquiryTemplate =
                TeacherInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        // if the inquiry doesn't exist or is from the execution period right
        // before the current one and isn't open and has no results,
        // that means that is not to answer and has no data to see
        if (inquiryTemplate == null
                || (inquiryTemplate.getExecutionPeriod().getNextExecutionPeriod().isCurrent() && !inquiryTemplate.isOpen() && !inquiryTemplate
                        .getExecutionPeriod().hasAnyInquiryResults())) {
            return actionMapping.findForward("inquiriesClosed");
        } else if (!inquiryTemplate.isOpen()) {
            request.setAttribute("readMode", "readMode");
        }

        if (!professorship.getPerson().hasToAnswerTeacherInquiry(professorship)) {
            return actionMapping.findForward("inquiryUnavailable");
        }

        List<TeacherShiftTypeGroupsResumeResult> teacherResults = new ArrayList<TeacherShiftTypeGroupsResumeResult>();

        InquiryResponseState finalState = getFilledState(professorship, inquiryTemplate, teacherResults);
        if (professorship.isResponsibleFor()) {
            InquiryResponseState regentFilledState =
                    RegentInquiryDA.getFilledState(executionCourse, professorship,
                            RegentInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod()));
            if (!InquiryResponseState.COMPLETE.equals(regentFilledState)) {
                request.setAttribute("regentCompletionState", regentFilledState.getLocalizedName());
            }
        }

        request.setAttribute("isComplete", InquiryResponseState.COMPLETE.equals(finalState));
        request.setAttribute("completionState", finalState.getLocalizedName());
        Collections.sort(teacherResults, new BeanComparator("shiftType"));

        List<CurricularCourseResumeResult> coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
        for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
            CurricularCourseResumeResult courseResumeResult =
                    new CurricularCourseResumeResult(executionCourse, executionDegree, "label.inquiry.degree", executionDegree
                            .getDegree().getSigla(), null, null, false, false, false, false, true);
            if (courseResumeResult.getResultBlocks().size() > 1) {
                coursesResultResume.add(courseResumeResult);
            }
        }
        Collections.sort(coursesResultResume, new BeanComparator("firstPresentationName"));

        request.setAttribute("professorship", professorship);
        request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());
        request.setAttribute("teacherResults", teacherResults);
        request.setAttribute("coursesResultResume", coursesResultResume);

        ViewTeacherInquiryPublicResults.setTeacherScaleColorException(executionCourse.getExecutionPeriod(), request);
        return actionMapping.findForward("inquiryResultsResume");
    }

    static InquiryResponseState getFilledState(Professorship professorship, TeacherInquiryTemplate inquiryTemplate,
            List<TeacherShiftTypeGroupsResumeResult> teacherResults) {
        List<InquiryResult> professorshipResults = professorship.getInquiryResults();
        InquiryResponseState finalState = InquiryResponseState.COMPLETE;
        if (professorship.hasInquiryTeacherAnswer()
                && professorship.getInquiryTeacherAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)) {
            finalState = InquiryResponseState.PARTIALLY_FILLED;
        }
        if (!professorshipResults.isEmpty()) {
            for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                List<InquiryResult> teacherShiftResults = professorship.getInquiryResults(shiftType);
                if (!teacherShiftResults.isEmpty()) {
                    TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult =
                            new TeacherShiftTypeGroupsResumeResult(professorship, shiftType, ResultPersonCategory.TEACHER,
                                    "label.inquiry.shiftType", RenderUtils.getEnumString(shiftType), false);
                    InquiryResponseState completionStateType = teacherShiftTypeGroupsResumeResult.getCompletionStateType();
                    finalState = finalState.compareTo(completionStateType) > 0 ? finalState : completionStateType;
                    teacherResults.add(teacherShiftTypeGroupsResumeResult);
                }
            }
        } else if (!professorship.hasInquiryTeacherAnswer()) {
            finalState = InquiryResponseState.EMPTY;
        } else if (professorship.getInquiryTeacherAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)) {
            finalState = InquiryResponseState.PARTIALLY_FILLED;
        }
        return finalState;
    }

    private static Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
        Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
        for (InquiryResult inquiryResult : professorshipResults) {
            shiftTypes.add(inquiryResult.getShiftType());
        }
        return shiftTypes;
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Professorship professorship = AbstractDomainObject.fromExternalId(getFromRequest(request, "professorshipOID").toString());
        TeacherInquiryTemplate teacherInquiryTemplate = TeacherInquiryTemplate.getCurrentTemplate();

        TeacherInquiryBean teacherInquiryBean = new TeacherInquiryBean(teacherInquiryTemplate, professorship);

        request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("teacherInquiryBean", teacherInquiryBean);

        return actionMapping.findForward("teacherInquiry");
    }

    public ActionForward showDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionCourse executionCourse =
                AbstractDomainObject.fromExternalId(getFromRequest(request, "executionCourseOID").toString());
        ExecutionDegree executionDegree =
                AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID").toString());

        DelegateInquiryTemplate delegateInquiryTemplate =
                DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        InquiryDelegateAnswer inquiryDelegateAnswer = null;
        for (InquiryDelegateAnswer delegateAnswer : executionCourse.getInquiryDelegatesAnswers()) {
            if (delegateAnswer.getExecutionDegree() == executionDegree) {
                inquiryDelegateAnswer = delegateAnswer;
                break;
            }
        }

        Set<InquiryBlockDTO> delegateInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocks()) {
            delegateInquiryBlocks.add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
        }

        Integer year = inquiryDelegateAnswer != null ? inquiryDelegateAnswer.getDelegate().getCurricularYear().getYear() : null;
        request.setAttribute("year", year);
        request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("delegateInquiryBlocks", delegateInquiryBlocks);
        return actionMapping.findForward("delegateInquiry");
    }

    public ActionForward postBackTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final TeacherInquiryBean teacherInquiryBean = getRenderedObject("teacherInquiryBean");
        teacherInquiryBean.setGroupsVisibility();

        request.setAttribute("executionPeriod", teacherInquiryBean.getProfessorship().getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
        request.setAttribute("teacherInquiryBean", teacherInquiryBean);

        RenderUtils.invalidateViewState();
        return actionMapping.findForward("teacherInquiry");
    }

    public ActionForward saveChanges(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final TeacherInquiryBean teacherInquiryBean = getRenderedObject("teacherInquiryBean");

        String validationResult = teacherInquiryBean.validateInquiry();
        if (!Boolean.valueOf(validationResult)) {
            RenderUtils.invalidateViewState();
            addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);

            request.setAttribute("teacherInquiryBean", teacherInquiryBean);
            request.setAttribute("executionPeriod", teacherInquiryBean.getProfessorship().getExecutionCourse()
                    .getExecutionPeriod());
            request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
            return actionMapping.findForward("teacherInquiry");
        }

        RenderUtils.invalidateViewState("teacherInquiryBean");
        teacherInquiryBean.saveChanges(getUserView(request).getPerson(), ResultPersonCategory.TEACHER);

        request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
        request.setAttribute("updated", "true");
        return showInquiriesPrePage(actionMapping, actionForm, request, response);
    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
        ExecutionCourse executionCourse =
                rootDomainObject.readExecutionCourseByOID(getIntegerFromRequest(request, "executionCourseID"));
        if (executionCourse == null) {
            return (ExecutionCourse) request.getAttribute("executionCourse");
        }
        request.setAttribute("executionCourse", executionCourse);
        return executionCourse;
    }

    private Professorship getProfessorship(ExecutionCourse executionCourse) {
        return AccessControl.getPerson().getProfessorshipByExecutionCourse(executionCourse);
    }

    public ActionForward showInquiryCourseResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StudentInquiriesCourseResult courseResult =
                RootDomainObject.getInstance().readStudentInquiriesCourseResultByOID(
                        Integer.valueOf(getFromRequest(request, "resultId").toString()));
        final Person loggedPerson = AccessControl.getPerson();
        if (!loggedPerson.isPedagogicalCouncilMember() && loggedPerson.getPersonRole(RoleType.GEP) == null
                && loggedPerson.getPersonRole(RoleType.DEPARTMENT_MEMBER) == null
                && !loggedPerson.hasProfessorshipForExecutionCourse(courseResult.getExecutionCourse())
                && courseResult.getExecutionDegree().getCoordinatorByTeacher(loggedPerson) == null) {
            return null;
        }
        request.setAttribute("inquiryResult", courseResult);
        return actionMapping.findForward(getCourseInquiryResultTemplate(courseResult));
    }

    private static String getCourseInquiryResultTemplate(final StudentInquiriesCourseResult courseResult) {
        final ExecutionSemester executionPeriod = courseResult.getExecutionCourse().getExecutionPeriod();
        if (executionPeriod.getSemester() == 2 && executionPeriod.getYear().equals("2007/2008")) {
            return "showCourseInquiryResult";
        }
        return "showCourseInquiryResult_v2";
    }

    public ActionForward showInquiryTeachingResult(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final StudentInquiriesTeachingResult teachingResult =
                RootDomainObject.getInstance().readStudentInquiriesTeachingResultByOID(
                        Integer.valueOf(getFromRequest(request, "resultId").toString()));
        final Person loggedPerson = AccessControl.getPerson();
        if (!loggedPerson.isPedagogicalCouncilMember() && loggedPerson.getPersonRole(RoleType.GEP) == null
                && loggedPerson.getPersonRole(RoleType.DEPARTMENT_MEMBER) == null
                && teachingResult.getProfessorship().getPerson() != loggedPerson
                && loggedPerson.isResponsibleFor(teachingResult.getProfessorship().getExecutionCourse()) == null
                && teachingResult.getExecutionDegree().getCoordinatorByTeacher(loggedPerson) == null) {
            return null;
        }
        request.setAttribute("inquiryResult", teachingResult);
        return actionMapping.findForward(getTeachingInquiryResultTemplate(teachingResult));
    }

    private static String getTeachingInquiryResultTemplate(final StudentInquiriesTeachingResult teachingResult) {
        final ExecutionSemester executionPeriod = teachingResult.getProfessorship().getExecutionCourse().getExecutionPeriod();
        if (executionPeriod.getSemester() == 2 && executionPeriod.getYear().equals("2007/2008")) {
            return "showTeachingInquiryResult";
        }
        return "showTeachingInquiryResult_v2";
    }

    private Collection<StudentInquiriesCourseResultBean> populateStudentInquiriesCourseResults(final Professorship professorship) {
        Map<ExecutionDegree, StudentInquiriesCourseResultBean> courseResultsMap =
                new HashMap<ExecutionDegree, StudentInquiriesCourseResultBean>();
        for (StudentInquiriesCourseResult studentInquiriesCourseResult : professorship.getExecutionCourse()
                .getStudentInquiriesCourseResults()) {
            courseResultsMap.put(studentInquiriesCourseResult.getExecutionDegree(), new StudentInquiriesCourseResultBean(
                    studentInquiriesCourseResult));
        }

        for (Professorship otherTeacherProfessorship : professorship.isResponsibleFor() ? professorship.getExecutionCourse()
                .getProfessorships() : Collections.singletonList(professorship)) {
            for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
                    .getStudentInquiriesTeachingResults()) {
                if (studentInquiriesTeachingResult.getInternalDegreeDisclosure()) {
                    courseResultsMap.get(studentInquiriesTeachingResult.getExecutionDegree()).addStudentInquiriesTeachingResult(
                            studentInquiriesTeachingResult);
                }
            }
        }

        return courseResultsMap.values();
    }
}
