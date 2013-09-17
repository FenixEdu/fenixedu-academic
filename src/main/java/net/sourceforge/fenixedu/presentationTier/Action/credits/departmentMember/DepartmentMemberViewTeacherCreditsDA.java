package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ViewTeacherCreditsDA;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/credits", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showTeacherCredits", path = "/credits/showTeacherCredits.jsp", tileProperties = @Tile(
                title = "private.department.credits")),
        @Forward(name = "showPastTeacherCredits", path = "/credits/showPastTeacherCredits.jsp", tileProperties = @Tile(
                title = "private.department.credits")),
        @Forward(name = "showAnnualTeacherCredits", path = "/credits/showAnnualTeacherCredits.jsp", tileProperties = @Tile(
                title = "private.department.credits")),
        @Forward(name = "showTeacherCreditsManagementFunctions", path = "/credits/showTeacherCreditsManagementFunctions.jsp",
                tileProperties = @Tile(title = "private.department.credits")) })
public class DepartmentMemberViewTeacherCreditsDA extends ViewTeacherCreditsDA {

    @Override
    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        final IUserView userView = UserView.getUser();
        if (userView.getPerson().getTeacher() != null) {
            TeacherCreditsBean teacherBean = new TeacherCreditsBean(userView.getPerson().getTeacher());
            teacherBean.prepareAnnualTeachingCredits(RoleType.DEPARTMENT_MEMBER);
            request.setAttribute("teacherBean", teacherBean);
        }
        return mapping.findForward("showTeacherCredits");
    }

    @Override
    public ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        return viewAnnualTeachingCredits(mapping, form, request, response, RoleType.DEPARTMENT_MEMBER);
    }

    public ActionForward lockTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOid"));
        TeacherService teacherService = TeacherService.getTeacherService(teacher, executionSemester);
        teacherService.lockTeacherCredits();
        request.setAttribute("teacherOid", teacher.getExternalId());
        request.setAttribute("executionYearOid", executionSemester.getExecutionYear().getExternalId());
        return viewAnnualTeachingCredits(mapping, form, request, response, RoleType.DEPARTMENT_MEMBER);
    }

    public ActionForward showTeacherManagementFunctions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        final IUserView userView = UserView.getUser();
        if (userView.getPerson().getTeacher() != null) {
            List<PersonFunction> personFunctions =
                    new ArrayList<PersonFunction>(userView.getPerson().getPersonFunctions(
                            AccountabilityTypeEnum.MANAGEMENT_FUNCTION));
            Collections.sort(personFunctions, new ReverseComparator(new BeanComparator("beginDate")));
            request.setAttribute("personFunctions", personFunctions);
        }
        return mapping.findForward("showTeacherCreditsManagementFunctions");
    }
}