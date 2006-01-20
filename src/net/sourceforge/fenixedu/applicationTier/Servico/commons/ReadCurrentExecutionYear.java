package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurrentExecutionYear extends Service {

    public InfoExecutionYear run() throws ExcepcaoPersistencia {
        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();

        ExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

        InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

        return infoExecutionYear;
    }

}