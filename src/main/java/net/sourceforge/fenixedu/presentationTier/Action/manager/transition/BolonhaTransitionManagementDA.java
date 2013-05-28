package net.sourceforge.fenixedu.presentationTier.Action.manager.transition;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/bolonhaTransitionManagement", attribute = "bolonhaTransitionManagementForm",
        formBean = "bolonhaTransitionManagementForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseStudent", path = "/manager/transition/bolonha/chooseStudent.jsp"),
        @Forward(name = "showStudentCurricularPlan", path = "/manager/transition/bolonha/showStudentCurricularPlan.jsp"),
        @Forward(name = "chooseRegistration", path = "/manager/transition/bolonha/chooseRegistration.jsp") })
public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

    public ActionForward prepareChooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studentNumberBean", new StudentNumberBean());
        return mapping.findForward("chooseStudent");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentNumberBean studentNumberBean = (StudentNumberBean) getObjectFromViewState("student-number-bean");
        request.setAttribute("studentId", Student.readStudentByNumber(studentNumberBean.getNumber()).getExternalId());

        return prepare(mapping, form, request, response);
    }

    @Override
    protected List<Registration> getRegistrations(final HttpServletRequest request) {
        final Student student = getStudent(request);
        return student != null ? student.getTransitionRegistrations() : Collections.<Registration> emptyList();
    }

    private Student getStudent(final HttpServletRequest request) {
        return rootDomainObject.readStudentByOID(getIntegerFromRequest(request, "studentId"));
    }

}
