package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurmas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IExecutionDegree;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurmas implements IServico {

    private static LerTurmas _servico = new LerTurmas();

    /**
     * The singleton access method of this class.
     */
    public static LerTurmas getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerTurmas() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerTurmas";
    }

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) throws ExcepcaoPersistencia {

        List classesList = null;
        List infoClassesList = null;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITurmaPersistente classDAO = sp.getITurmaPersistente();

        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

        IExecutionDegree executionDegree = Cloner
                .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

        if (curricularYear != null) {
            classesList = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
                    executionPeriod, curricularYear, executionDegree);
        } else {
            classesList = classDAO.readByExecutionDegreeAndExecutionPeriod(executionDegree,
                    executionPeriod);
        }

        Iterator iterator = classesList.iterator();
        infoClassesList = new ArrayList();
        while (iterator.hasNext()) {
            ISchoolClass elem = (ISchoolClass) iterator.next();
            infoClassesList.add(Cloner.copyClass2InfoClass(elem));
        }

        return infoClassesList;
    }

}