package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionDegreesByDegree extends Service {

    public List run(Integer idDegree) throws FenixServiceException, ExcepcaoPersistencia {

        Degree degree = rootDomainObject.readDegreeByOID(idDegree);
        
        List<ExecutionDegree> allExecutionDegrees = ExecutionDegree.getAllByDegreeAndCurricularStage(degree, CurricularStage.OLD);
        if (allExecutionDegrees == null || allExecutionDegrees.isEmpty()) {
            throw new FenixServiceException();
        }

        List<InfoExecutionDegree> allInfoExecutionDegrees = new ArrayList<InfoExecutionDegree>(allExecutionDegrees.size());
        for (ExecutionDegree executionDegree : allExecutionDegrees) {
            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            allInfoExecutionDegrees.add(infoExecutionDegree);
        }

        return allInfoExecutionDegrees;
    }

}
