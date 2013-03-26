package net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.personal.ingression.data;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.student.PersonalIngressionData;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/personalIngressionDataViewer", module = "gep")
@Forwards({
        @Forward(name = "chooseStudent", path = "/gep/student/candidacy/personal/ingression/data/chooseStudent.jsp",
                tileProperties = @Tile(title = "private.gep.raidesdata.raidesdatabystudent")),
        @Forward(name = "viewStudent", path = "/gep/student/candidacy/personal/ingression/data/viewStudent.jsp"),
        @Forward(name = "viewStudentCandidacy", path = "/gep/student/candidacy/personal/ingression/data/viewStudentCandidacy.jsp"),
        @Forward(name = "viewPersonalIngressionData",
                path = "/gep/student/candidacy/personal/ingression/data/viewPersonalIngressionData.jsp"),
        @Forward(name = "viewIndividualCandidacy",
                path = "/gep/student/candidacy/personal/ingression/data/viewIndividualCandidacy.jsp"),
        @Forward(name = "viewRegistration", path = "/gep/student/candidacy/personal/ingression/data/viewRegistration.jsp") })
public class PersonalIngressionDataViewer extends FenixDispatchAction {

    public ActionForward chooseStudent(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute("chooseStudentBean", new ChooseStudentBean());

        return mapping.findForward("chooseStudent");
    }

    public ActionForward findStudents(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        ChooseStudentBean chooseStudentBean = getRenderedObject("chooseStudentBean");
        Set<Student> students = chooseStudentBean.findStudents();

        if (students.size() == 1) {
            request.setAttribute("student", students.iterator().next());
            return mapping.findForward("viewStudent");
        }

        request.setAttribute("students", students);
        request.setAttribute("chooseStudentBean", chooseStudentBean);

        return mapping.findForward("chooseStudent");
    }

    public ActionForward findStudentsInvalid(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        ChooseStudentBean chooseStudentBean = getRenderedObject("chooseStudentBean");

        request.setAttribute("chooseStudentBean", chooseStudentBean);

        return mapping.findForward("chooseStudent");
    }

    public ActionForward viewStudent(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        Student student = getDomainObject(request, "studentId");

        request.setAttribute("student", student);

        return mapping.findForward("viewStudent");
    }

    public ActionForward viewStudentCandidacy(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        StudentCandidacy studentCandidacy = getDomainObject(request, "studentCandidacyId");

        request.setAttribute("studentCandidacy", studentCandidacy);

        return mapping.findForward("viewStudentCandidacy");
    }

    public ActionForward viewPersonalIngressionData(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        PersonalIngressionData personalIngressionData = getDomainObject(request, "personalIngressionDataId");

        request.setAttribute("personalIngressionData", personalIngressionData);

        return mapping.findForward("viewPersonalIngressionData");
    }

    public ActionForward viewIndividualCandidacy(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        IndividualCandidacy individualCandidacy = getDomainObject(request, "individualCandidacyId");

        request.setAttribute("individualCandidacy", individualCandidacy);

        return mapping.findForward("viewIndividualCandidacy");
    }

    public ActionForward viewRegistration(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        Registration registration = getDomainObject(request, "registrationId");

        request.setAttribute("registration", registration);

        return mapping.findForward("viewRegistration");
    }
}
