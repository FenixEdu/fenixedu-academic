/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndDegree extends Service {

    public Object run(Degree curso, ExecutionYear year) throws FenixServiceException,
			ExcepcaoPersistencia {
        List infoExecutionDegrees = new ArrayList();

            IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();

            List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(curso
                    .getIdInternal(), year.getYear(), CurricularStage.OLD);
            if (executionDegrees != null && !executionDegrees.isEmpty()) {
                for (int i = 0; i < executionDegrees.size(); i++) {
                    ExecutionDegree executionDegree = (ExecutionDegree) executionDegrees.get(i);
                    InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                            .newInfoFromDomain(executionDegree);
                    infoExecutionDegrees.add(infoExecutionDegree);
                }
            }

        return infoExecutionDegrees;
    }

}