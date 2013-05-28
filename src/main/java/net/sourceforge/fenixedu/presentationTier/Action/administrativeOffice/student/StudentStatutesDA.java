package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatute.CreateStudentStatuteFactory;
import net.sourceforge.fenixedu.domain.student.StudentStatute.DeleteStudentStatuteFactory;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/studentStatutes", module = "academicAdministration")
@Forwards({ @Forward(name = "manageStatutes", path = "/academicAdminOffice/manageStatutes.jsp") })
public class StudentStatutesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Student student = AbstractDomainObject.fromExternalId(getIntegerFromRequest(request, "studentId"));
        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = AbstractDomainObject.fromExternalId(request.getParameter("studentOID"));
        request.setAttribute("student", student);
        request.setAttribute("schemaName", request.getParameter("schemaName"));
        request.setAttribute("manageStatuteBean", getRenderedObject());

        return mapping.findForward("manageStatutes");
    }

    public ActionForward seniorStatutePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateStudentStatuteFactory oldManageStatuteBean = getRenderedObject();
        final Student student = oldManageStatuteBean.getStudent();
        final StudentStatuteType statuteType = oldManageStatuteBean.getStatuteType();
        final CreateStudentStatuteFactory manageStatuteBean = new CreateStudentStatuteFactory(student);
        manageStatuteBean.setStatuteType(statuteType);

        RenderUtils.invalidateViewState();

        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", manageStatuteBean);

        if (manageStatuteBean.getStatuteType() == StudentStatuteType.SENIOR) {
            request.setAttribute("schemaName", "student.createSeniorStatute");
        } else {
            request.setAttribute("schemaName", "student.createStatutes");
        }

        return mapping.findForward("manageStatutes");
    }

    public ActionForward addNewStatute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        try {
            // add new statute
            executeFactoryMethod();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        final Student student = ((CreateStudentStatuteFactory) getRenderedObject()).getStudent();
        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");

    }

    public ActionForward deleteStatute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        final StudentStatute studentStatute =
                AbstractDomainObject.fromExternalId(getIntegerFromRequest(request, "statuteId"));
        final Student student = studentStatute.getStudent();

        try {
            // delete statute
            executeFactoryMethod(new DeleteStudentStatuteFactory(studentStatute));
        } catch (DomainException de) {
            addActionMessage(request, de.getMessage());
        }

        request.setAttribute("student", student);
        request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));
        request.setAttribute("schemaName", "student.createStatutes");

        return mapping.findForward("manageStatutes");

    }

}
