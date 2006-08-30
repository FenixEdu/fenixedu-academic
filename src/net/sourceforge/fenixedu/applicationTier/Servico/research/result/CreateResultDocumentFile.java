package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;

public class CreateResultDocumentFile extends Service {
    public ResultDocumentFile run(ResultDocumentFileSubmissionBean bean) {
        final Result result = bean.getResult();
        //final String fileName = result.getIdInternal() + String.valueOf(System.currentTimeMillis());
        final String displayName = bean.getDisplayName();
        final FileMetadata fileMetadata = new FileMetadata(displayName, AccessControl.getUserView().getPerson().getName());
        final Group permittedGroup = ResultDocumentFile.getPermittedGroup(bean.getPermission());
        final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                getFilePath(result), bean.getFileName(), (permittedGroup != null) ? true : false, fileMetadata, bean.getInputStream());
        
        return new ResultDocumentFile(result, fileDescriptor.getFilename(), displayName, fileDescriptor.getMimeType(), fileDescriptor
	                .getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
	                fileDescriptor.getUniqueId(), permittedGroup);
    }

    /**
     * @param result
     * @return filePath
     *
     * Path examples:   "/Result/ResultPublication12345/"
     *                  "/Result/ResultPatent54321/"
     */
    private FilePath getFilePath(Result result) {
        final FilePath filePath = new FilePath();
        
        filePath.addNode(new Node(Result.class.getSimpleName(), Result.class.getSimpleName()));
        filePath.addNode(new Node(result.getClass().getSimpleName() + result.getIdInternal(), result.getIdInternal().toString()));
        
        return filePath;
    }
}
