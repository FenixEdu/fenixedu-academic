package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;

public class CreateResultDocumentFile extends Service {
    public ResultDocumentFile run(ResultDocumentFileSubmissionBean bean) throws FenixServiceException {
        final Result result = bean.getResult();
        if (result == null) { throw new FenixServiceException("error.Result.not.found"); }
        
        final String fileName = result.getIdInternal() + String.valueOf(System.currentTimeMillis());
        final String displayName = bean.getFilename();
        final Person person = AccessControl.getUserView().getPerson();
        final FileMetadata fileMetadata = new FileMetadata(fileName, person.getName());

        final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                getFilePath(result), fileName, true, fileMetadata, bean.getInputStream());
        
        return new ResultDocumentFile(result, person, fileName, displayName, fileDescriptor.getMimeType(), fileDescriptor
                .getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
                fileDescriptor.getUniqueId(), new RoleGroup(Role.getRoleByRoleType(RoleType.RESEARCHER)));
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
