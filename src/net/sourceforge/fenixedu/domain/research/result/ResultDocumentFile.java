package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class ResultDocumentFile extends ResultDocumentFile_Base {
    
    static { ResultResultDocumentFile.addListener(new ResultResultDocumentFileListener()); }
    
    public  ResultDocumentFile() {
        super();
    }

    public ResultDocumentFile(Result result, Person person, String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        checkParameters(result, person);
        setResult(result);
        setChangedBy(person.getName());
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }
    
    private void checkParameters(Result result, Person person) {
        if (result == null) { throw new DomainException("error.Result.not.found"); }
        if (person == null) { throw new DomainException("error.Result.person.not.found"); }
    }

    public void setChangedBy(String personName) {
        this.getResult().setModificationDateAndAuthor(personName);
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
}

