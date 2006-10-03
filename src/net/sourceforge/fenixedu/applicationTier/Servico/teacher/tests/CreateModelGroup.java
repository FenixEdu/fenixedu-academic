package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateModelGroup extends Service {
	public NewModelGroup run(NewModelGroup parentGroup, String name) throws FenixServiceException,
			ExcepcaoPersistencia {
		return new NewModelGroup(parentGroup, name);
	}
}
