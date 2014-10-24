package org.fenixedu.core.ui.teacher;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.core.ui.StrutsFunctionalityController;
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