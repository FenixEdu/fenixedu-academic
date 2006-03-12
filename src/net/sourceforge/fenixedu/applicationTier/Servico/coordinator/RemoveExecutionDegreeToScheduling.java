package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveExecutionDegreeToScheduling extends Service {

    public void run(final Scheduleing scheduleing, final ExecutionDegree executionDegree)
    		throws ExcepcaoPersistencia {
    	scheduleing.getExecutionDegrees().remove(executionDegree);
    }

}