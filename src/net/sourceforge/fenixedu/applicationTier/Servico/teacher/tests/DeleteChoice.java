package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteChoice extends FenixService {
    public void run(NewChoice choice) throws FenixServiceException {
	choice.delete();
    }
}
