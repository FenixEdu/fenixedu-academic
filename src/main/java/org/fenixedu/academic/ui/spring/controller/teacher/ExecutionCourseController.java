package org.fenixedu.academic.ui.spring.controller.teacher;

import java.util.Optional;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.spring.StrutsFunctionalityController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class ExecutionCourseController extends StrutsFunctionalityController {

    ExecutionCourse executionCourse;

    public ExecutionCourseController() {
        super();
    }

    private static Professorship findProfessorship(final ExecutionCourse executionCourse) {
        final Person person = AccessControl.getPerson();
        if (person != null) {
            Optional<Professorship> professorshipOpt =
                    person.getProfessorshipsSet().stream()
                            .filter(professorship -> professorship.getExecutionCourse().equals(executionCourse)).findFirst();
            if (professorshipOpt.isPresent()) {
                Professorship prof = professorshipOpt.get();
                if (!prof.getPermissions().getGroups()) {
                    throw new RuntimeException("Professor is not authorized to manage the student groups");
                } else {
                    return prof;
                }
            }
        }
        throw new RuntimeException("User is not authorized to manage the selected course!");
    }

    @ModelAttribute("projectGroup")
    public ProjectGroupBean setProjectGroup() {
        return new ProjectGroupBean();
    }

    @ModelAttribute("professorship")
    public Professorship setProfessorship(@PathVariable ExecutionCourse executionCourse) {
        return findProfessorship(executionCourse);
    }

    @ModelAttribute("executionCourse")
    public ExecutionCourse getExecutionCourse(@PathVariable ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
        return executionCourse;
    }
}