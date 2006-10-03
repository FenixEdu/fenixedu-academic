package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CorrectTestGroup extends Service {
	public void run(NewTestGroup testGroup) throws FenixServiceException, ExcepcaoPersistencia {
		testGroup.correct();
	}
}
