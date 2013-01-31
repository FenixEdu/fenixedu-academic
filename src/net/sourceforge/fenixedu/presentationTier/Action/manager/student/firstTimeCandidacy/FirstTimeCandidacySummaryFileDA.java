package net.sourceforge.fenixedu.presentationTier.Action.manager.student.firstTimeCandidacy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidacySummary", module = "manager", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "prepare", path = "/manager/student/candidacies/manageFirstCandidacySummaryFile.jsp") })
public class FirstTimeCandidacySummaryFileDA extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("studentNumberBean", new StudentNumberBean());
		return mapping.findForward("prepare");
	}

	public ActionForward searchCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final StudentNumberBean numberBean = (StudentNumberBean) getRenderedObject("student-number-bean");

		StudentCandidacy studentCandidacy = findCandidacy(numberBean.getNumber());
		request.setAttribute("candidacy", studentCandidacy);

		if (studentCandidacy != null && studentCandidacy.hasSummaryFile()) {
			request.setAttribute("hasPDF", "true");
		}

		return mapping.findForward("prepare");
	}

	private StudentCandidacy findCandidacy(Integer studentNumber) {
		final Student student = Student.readStudentByNumber(studentNumber);
		final List<Registration> registrations = student.getRegistrations();
		if (registrations != null && registrations.size() > 0) {
			return registrations.iterator().next().getStudentCandidacy();
		}
		return null;
	}
}
