package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurmas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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