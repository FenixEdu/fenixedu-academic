package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class UpdateProfessorshipWithPerson {
    @Service
    public static Boolean run(Person person, ExecutionYear executionYear, final List executionCourseResponsabilities)
            throws MaxResponsibleForExceed, InvalidCategory, NotAuthorizedException {
        AbstractModifyProfessorshipWithPerson.run(person);
        person.updateResponsabilitiesFor(executionYear.getExternalId(), executionCourseResponsabilities);
        return true;
    }
}
