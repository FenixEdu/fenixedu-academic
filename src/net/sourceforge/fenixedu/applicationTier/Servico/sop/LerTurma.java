package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurma
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.IServico;
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

public class LerTurma implements IServico {

    private static LerTurma _servico = new LerTurma();

    /**
     * The singleton access method of this class.
     */
    public static LerTurma getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerTurma() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerTurma";
    }

    public InfoClass run(String className, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) {

        InfoClass infoTurma = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

            ISchoolClass turma = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(
                    className, executionDegree, executionPeriod);

            if (turma != null) {
                infoTurma = Cloner.copyClass2InfoClass(turma);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoTurma;
    }

}