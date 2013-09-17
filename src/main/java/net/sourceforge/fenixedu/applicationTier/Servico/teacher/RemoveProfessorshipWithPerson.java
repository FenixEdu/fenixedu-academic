package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;

public class RemoveProfessorshipWithPerson extends AbstractModifyProfessorshipWithPerson {
    @Atomic
    public static Boolean run(Person person, ExecutionCourse executionCourse) throws NotAuthorizedException {
        AbstractModifyProfessorshipWithPerson.run(person);

        Professorship professorshipToDelete = person.getProfessorshipByExecutionCourse(executionCourse);

        Collection shiftProfessorshipList = professorshipToDelete.getAssociatedShiftProfessorship();

        boolean hasCredits = false;

        if (!shiftProfessorshipList.isEmpty()) {
            hasCredits = CollectionUtils.exists(shiftProfessorshipList, new Predicate() {

                @Override
                public boolean evaluate(Object arg0) {
                    ShiftProfessorship shiftProfessorship = (ShiftProfessorship) arg0;
                    return shiftProfessorship.getPercentage() != null && shiftProfessorship.getPercentage() != 0;
                }
            });
        }

        if (!hasCredits) {
            professorshipToDelete.delete();
        } else {
            if (hasCredits) {
                throw new DomainException("error.remove.professorship");
            }
        }
        return Boolean.TRUE;
    }

}
