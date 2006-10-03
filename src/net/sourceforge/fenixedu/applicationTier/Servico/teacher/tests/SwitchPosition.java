package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.Positionable;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SwitchPosition extends Service {
	public void run(Positionable positionable, Integer relativePosition) throws FenixServiceException,
			ExcepcaoPersistencia {
		positionable.switchPosition(relativePosition);
	}
}
