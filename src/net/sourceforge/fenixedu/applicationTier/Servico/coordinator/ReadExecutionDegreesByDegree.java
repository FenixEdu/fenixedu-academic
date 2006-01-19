package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionDegreesByDegree implements IService {

    public List run(Integer idDegree) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionDegree cursoExecucaoPersistente = persistentSupport.getIPersistentExecutionDegree();

        List<ExecutionDegree> allExecutionDegrees = cursoExecucaoPersistente.readExecutionsDegreesByDegree(idDegree,CurricularStage.OLD);
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
