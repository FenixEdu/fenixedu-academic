package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurrentExecutionYear implements IService {

    public InfoExecutionYear run() {

        InfoExecutionYear infoExecutionYear = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

            infoExecutionYear = (InfoExecutionYear) Cloner.get(executionYear);

        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        return infoExecutionYear;
    }

}