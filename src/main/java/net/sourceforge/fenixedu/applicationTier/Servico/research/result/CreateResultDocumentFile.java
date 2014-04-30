package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import pt.ist.fenixframework.Atomic;

public class CreateResultDocumentFile {
    @Atomic
    public static void run(ResultDocumentFileSubmissionBean bean) {
        final ResearchResult result = bean.getResult();
        final String displayName = bean.getDisplayName();
        final String filename = bean.getFileName();
        final byte[] content = bean.readStream();
        result.addDocumentFile(content, filename, displayName, bean.getPermission());
    }
}