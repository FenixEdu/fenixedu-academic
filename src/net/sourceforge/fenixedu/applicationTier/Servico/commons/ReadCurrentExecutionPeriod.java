package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 14/Fev/2003
 * 
 * @author jpvl
 */
public class ReadCurrentExecutionPeriod implements IService {

    public InfoExecutionPeriod run() {

        InfoExecutionPeriod infoExecutionPeriod = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);
        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        return infoExecutionPeriod;
    }

}