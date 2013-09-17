package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class UpdateProfessorshipWithPerson {
    @Atomic
    public static Boolean run(Person person, ExecutionYear executionYear, final List<String> executionCourseResponsabilities)
            throws MaxResponsibleForExceed, InvalidCategory, NotAuthorizedException {
        AbstractModifyProfessorshipWithPerson.run(person);
        person.updateResponsabilitiesFor(executionYear.getExternalId(), executionCourseResponsabilities);
        return true;
    }
}
