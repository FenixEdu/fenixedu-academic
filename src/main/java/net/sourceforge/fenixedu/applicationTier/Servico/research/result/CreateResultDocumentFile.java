package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import pt.ist.fenixframework.Atomic;

public class CreateResultDocumentFile {
    @Atomic
    public static void run(ResultDocumentFileSubmissionBean bean) {
        final ResearchResult result = bean.getResult();
        final String displayName = bean.getDisplayName();
        final Group permittedGroup = ResearchResultDocumentFile.getPermittedGroup(bean.getPermission());
        final String filename = bean.getFileName();
        final byte[] content = bean.readStream();
        result.addDocumentFile(content, filename, displayName, bean.getPermission(), permittedGroup);
    }
}