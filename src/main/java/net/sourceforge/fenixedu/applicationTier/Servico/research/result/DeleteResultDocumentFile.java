package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResultDocumentFile {

    @Service
    public static void run(String oid) {
        final ResearchResultDocumentFile documentFile = ResearchResultDocumentFile.readByOID(oid);
        documentFile.getResult().removeDocumentFile(documentFile);
    }
}