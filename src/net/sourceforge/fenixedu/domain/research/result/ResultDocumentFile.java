package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class ResultDocumentFile extends ResultDocumentFile_Base {
    
    static { ResultResultDocumentFile.addListener(new ResultResultDocumentFileListener()); }
    
    public  ResultDocumentFile() {
        super();
    }

    public ResultDocumentFile(Result result, String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        checkParameters(result);
        setResult(result);
        setChangedBy();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }
    
    private void checkParameters(Result result) {
        if (result == null) { throw new DomainException("error.Result.not.found"); }
    }

    public void setChangedBy() {
        this.getResult().setModificationDateAndAuthor();
    }
    
    private void delete() {
        removeResult();        
        removeRootDomainObject();
        deleteDomainObject();
        deleteFile();
    }

    private void deleteFile() {
        final IFileManager fileManager = FileManagerFactory.getFileManager();
        fileManager.deleteFile(getExternalStorageIdentification());
    }
    
    private static class ResultResultDocumentFileListener extends
    dml.runtime.RelationAdapter<ResultDocumentFile, Result> {
        @Override
        public void afterRemove(ResultDocumentFile resultDocumentFile, Result result) {
            if (resultDocumentFile != null && result != null) {
                resultDocumentFile.delete();
            }
        }
    }

    /**
     * Method used for calling the service responsible for creating a ResultDocumentFile
     * 
     * @param bean
     * @throws FenixFilterException
     * @throws FenixServiceException
     */
	public static void create(ResultDocumentFileSubmissionBean bean) throws FenixFilterException, FenixServiceException {
		ServiceUtils.executeService(AccessControl.getUserView(), "CreateResultDocumentFile", bean);
	}
}

