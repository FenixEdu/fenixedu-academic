package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestionBank;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateQuestionBank extends FenixService {
    public NewQuestionBank run(Person owner) throws FenixServiceException {
	return new NewQuestionBank(owner);
    }
}
