package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestionBank;
import pt.ist.fenixframework.Atomic;

public class CreateQuestionBank {
    @Atomic
    public static NewQuestionBank run(Person owner) throws FenixServiceException {
        return new NewQuestionBank(owner);
    }
}