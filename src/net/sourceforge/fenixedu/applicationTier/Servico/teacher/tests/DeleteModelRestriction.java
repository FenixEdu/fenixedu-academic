package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteModelRestriction extends Service {
	public void run(NewModelRestriction modelRestriction) throws FenixServiceException, ExcepcaoPersistencia {
		modelRestriction.delete();
	}
}
