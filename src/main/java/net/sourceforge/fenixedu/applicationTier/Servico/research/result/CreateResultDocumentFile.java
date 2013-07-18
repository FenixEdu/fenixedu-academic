package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateResultDocumentFile {
    @Service
    public static void run(ResultDocumentFileSubmissionBean bean) {
        final ResearchResult result = bean.getResult();
        final String displayName = bean.getDisplayName();
        final Group permittedGroup = ResearchResultDocumentFile.getPermittedGroup(bean.getPermission());
        final VirtualPath virtualPath = getVirtualPath(result);
        final String filename = bean.getFileName();
        final Collection<FileSetMetaData> metaData = Collections.emptySet();
        final byte[] content = bean.readStream();
        result.addDocumentFile(virtualPath, metaData, content, filename, displayName, bean.getPermission(), permittedGroup);
    }

    /**
     * @param result
     * @return filePath
     * 
     *         Path examples: /Research/Results/Publications/pub{idInternal}/
     *         /Research/Results/Patents/pat{idInternal}/
     */
    private static VirtualPath getVirtualPath(ResearchResult result) {
        final VirtualPath filePath = new VirtualPath();

        filePath.addNode(new VirtualPathNode("Research", "Research"));
        filePath.addNode(new VirtualPathNode("Results", "Results"));

        if (result instanceof ResearchResultPublication) {
            filePath.addNode(new VirtualPathNode("Publications", "Publications"));
            filePath.addNode(new VirtualPathNode("pub" + result.getIdInternal(), "pub" + result.getIdInternal().toString()));
        }
        if (result instanceof ResearchResultPatent) {
            filePath.addNode(new VirtualPathNode("Patents", "Patents"));
            filePath.addNode(new VirtualPathNode("pat" + result.getIdInternal(), "pat" + result.getIdInternal().toString()));
        }

        return filePath;
    }
}