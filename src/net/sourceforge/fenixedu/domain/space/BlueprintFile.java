package net.sourceforge.fenixedu.domain.space;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class BlueprintFile extends BlueprintFile_Base {
    
    private BlueprintFile() {
        super();   
    }      
    
    public BlueprintFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, filename, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }
    
    public void delete() {
        removeBlueprint();        
        deleteFile();
        removeRootDomainObject();
        deleteDomainObject();
    }

    private void deleteFile() {
        final IFileManager fileManager = FileManagerFactory.getFileManager();
        fileManager.deleteFile(getExternalStorageIdentification());
    }
}
