package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurrentExecutionYear extends Service {

    public InfoExecutionYear run() throws ExcepcaoPersistencia {

        InfoExecutionYear infoExecutionYear = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

        ExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

        infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

        return infoExecutionYear;
    }

}