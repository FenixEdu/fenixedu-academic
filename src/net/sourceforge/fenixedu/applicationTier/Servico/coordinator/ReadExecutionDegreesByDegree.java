package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionDegreesByDegree extends FenixService {

    @Service
    public static List run(Integer idDegree) throws FenixServiceException {

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