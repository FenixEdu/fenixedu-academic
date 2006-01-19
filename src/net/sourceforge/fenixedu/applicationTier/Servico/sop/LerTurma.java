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
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class LerTurma implements IService {

    public InfoClass run(String className, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final SchoolClass turma = sp.getITurmaPersistente()
                .readByNameAndExecutionDegreeAndExecutionPeriod(className,
                        infoExecutionDegree.getIdInternal(), infoExecutionPeriod.getIdInternal());

        if (turma != null) {
            return InfoClass.newInfoFromDomain(turma);
        }

        return null;
    }

}
