package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;

public class DeleteResultDocumentFile extends Service {

    public void run(Integer oid) {
	final ResultDocumentFile documentFile = ResultDocumentFile.readByOID(oid);
	documentFile.delete();
    }
}
