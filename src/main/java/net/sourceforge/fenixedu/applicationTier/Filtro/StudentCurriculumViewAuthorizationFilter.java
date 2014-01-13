package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

import org.fenixedu.bennu.core.security.Authenticate;

public class StudentCurriculumViewAuthorizationFilter {

    public final static StudentCurriculumViewAuthorizationFilter instance = new StudentCurriculumViewAuthorizationFilter();

    final public void execute() throws NotAuthorizedException {
        if (!AcademicPredicates.VIEW_FULL_STUDENT_CURRICULUM.evaluate(Authenticate.getUser().getPerson())) {
            throw new NotAuthorizedException();
        }
    }

}
