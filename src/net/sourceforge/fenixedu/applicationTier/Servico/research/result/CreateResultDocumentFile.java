package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;

public class CreateResultDocumentFile extends Service {
    public void run(ResultDocumentFileSubmissionBean bean) {
        final Result result = bean.getResult();
        final String displayName = bean.getDisplayName();
        
        final String fileName = result.getIdInternal() + String.valueOf(System.currentTimeMillis());
        final FileMetadata fileMetadata = new FileMetadata(fileName, AccessControl.getUserView().getPerson().getName());
        
        final Group permittedGroup = ResultDocumentFile.getPermittedGroup(bean.getPermission());
        
        final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                getFilePath(result), bean.getFileName(), (permittedGroup != null) ? true : false, fileMetadata, bean.getInputStream());
        
        result.addDocumentFile(fileDescriptor.getFilename(), displayName, bean.getPermission(), fileDescriptor.getMimeType(), fileDescriptor
				        .getChecksum(), fileDescriptor.getChecksumAlgorithm(),
                fileDescriptor.getSize(), fileDescriptor.getUniqueId(), permittedGroup);
    }

    /**
     * @param result
     * @return filePath
     *
     * Path examples:   /Research/Results/Publications/pub{idInternal}/
     *                  /Research/Results/Patents/pat{idInternal}/
     */
    private FilePath getFilePath(Result result) {
        final FilePath filePath = new FilePath();
        
        filePath.addNode(new Node("Research", "Research"));
        filePath.addNode(new Node("Results", "Results"));
        
        if (result instanceof ResultPublication) {
            filePath.addNode(new Node("Publications", "Publications"));
            filePath.addNode(new Node("pub" + result.getIdInternal(), "pub" + result.getIdInternal().toString()));
        }
        if (result instanceof ResultPatent){
            filePath.addNode(new Node("Patents", "Patents"));
            filePath.addNode(new Node("pat" + result.getIdInternal(), "pat" + result.getIdInternal().toString()));
        }
        
        return filePath;
    }
}
