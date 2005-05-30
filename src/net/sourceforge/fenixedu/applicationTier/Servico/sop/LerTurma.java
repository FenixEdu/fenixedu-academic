package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurma
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerTurma implements IService {

    private static LerTurma _servico = new LerTurma();

    public InfoClass run(String className, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {

        InfoClass infoTurma = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
        IExecutionDegree executionDegree = Cloner
                .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

        ISchoolClass turma = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(
                className, executionDegree.getIdInternal(), executionPeriod.getIdInternal());

        if (turma != null) {
            infoTurma = InfoClass.newInfoFromDomain(turma);
        }

        return infoTurma;
    }

}