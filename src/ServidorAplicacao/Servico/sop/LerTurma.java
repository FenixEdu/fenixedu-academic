package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurma
 * 
 * @author tfc130
 * @version
 */

import DataBeans.InfoClass;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IExecutionDegree;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

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