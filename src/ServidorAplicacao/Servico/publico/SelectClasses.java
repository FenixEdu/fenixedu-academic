package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class SelectClasses implements IServico {

    private static SelectClasses _servico = new SelectClasses();

    /**
     * The actor of this class.
     */

    private SelectClasses() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "SelectClasses";
    }

    /**
     * Returns the _servico.
     * 
     * @return SelectClasses
     */
    public static SelectClasses getService() {
        return _servico;
    }

    public Object run(InfoClass infoClass) {

        List classes = new ArrayList();
        List infoClasses = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurmaPersistente classDAO = sp.getITurmaPersistente();

            IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass
                    .getInfoExecutionPeriod());
            ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass
                    .getInfoExecutionDegree());

            classes = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(executionPeriod,
                    infoClass.getAnoCurricular(), executionDegree);

            for (int i = 0; i < classes.size(); i++) {
                ITurma taux = (ITurma) classes.get(i);
                infoClasses.add(Cloner.copyClass2InfoClass(taux));
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
        }

        return infoClasses;

    }

}