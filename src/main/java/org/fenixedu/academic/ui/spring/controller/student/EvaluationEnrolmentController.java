package org.fenixedu.academic.ui.spring.controller.student;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.student.EnrolStudentInWrittenEvaluation;
import org.fenixedu.academic.service.services.student.UnEnrollStudentInWrittenEvaluation;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Controller
@SpringApplication(description = "link.evaluations.enrolment", path = "showEvaluations", group = "anyone",
        hint = "Student", title = "link.evaluations.enrolment")
@SpringFunctionality(app = EvaluationEnrolmentController.class, title = "link.evaluations.enrolment")
@RequestMapping("/enrollment/evaluations/showEvaluations")
public class EvaluationEnrolmentController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        final User user = Authenticate.getUser();
        final Student student = user.getPerson().getStudent();

        final SortedSet<ExecutionSemester> executionSemesters = student.getRegistrationsSet().stream()
                .flatMap(registration -> registration.getAssociatedAttendsSet().stream())
                .map(attends -> attends.getExecutionPeriod())
                .collect(Collectors.toCollection(() -> new TreeSet<>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR.reversed())));
        model.addAttribute("executionSemesters", executionSemesters);

        final SortedSet<WrittenEvaluation> writtenEvaluations = student.getRegistrationsSet().stream()
                .flatMap(registration -> registration.getAssociatedAttendsSet().stream())
                .flatMap(attends -> attends.getExecutionCourse().getAssociatedEvaluationsSet().stream())
                .filter(WrittenEvaluation.class::isInstance)
                .map(WrittenEvaluation.class::cast)
                .collect(Collectors.toCollection(() -> new TreeSet<>(WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE.reversed())));
        model.addAttribute("writtenEvaluations", writtenEvaluations);

        return "student/evaluations/showEvaluations";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{writtenEvaluation}/enrol")
    public String enrol(final Model model, final @PathVariable WrittenEvaluation writtenEvaluation) {
        final User user = Authenticate.getUser();
        try {
            EnrolStudentInWrittenEvaluation.runEnrolStudentInWrittenEvaluation(user.getUsername(), writtenEvaluation.getExternalId());
        } catch (final FenixServiceException e) {
            throw new Error(e);
        }
        return "redirect:" + CoreConfiguration.getConfiguration().applicationUrl() + "/enrollment/evaluations/showEvaluations";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{writtenEvaluation}/unenrol")
    public String unenrol(final Model model, final @PathVariable WrittenEvaluation writtenEvaluation) {
        final User user = Authenticate.getUser();
        try {
            UnEnrollStudentInWrittenEvaluation.runUnEnrollStudentInWrittenEvaluation(user.getUsername(), writtenEvaluation.getExternalId());
        } catch (final FenixServiceException e) {
            throw new Error(e);
        }
        return "redirect:" + CoreConfiguration.getConfiguration().applicationUrl() + "/enrollment/evaluations/showEvaluations";
    }

}
