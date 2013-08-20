package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

public class StudentCurriculumViewAuthorizationFilter {

    public final static StudentCurriculumViewAuthorizationFilter instance = new StudentCurriculumViewAuthorizationFilter();

    final public void execute() throws NotAuthorizedException {
        if (!AcademicPredicates.VIEW_FULL_STUDENT_CURRICULUM.evaluate(AccessControl.getUserView().getPerson())) {
            throw new NotAuthorizedException();
        }
    }

}
