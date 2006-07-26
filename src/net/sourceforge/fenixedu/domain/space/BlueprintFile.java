package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class BlueprintFile extends BlueprintFile_Base {

    static {
        BlueprintBlueprintFile.addListener(new BlueprintBlueprintFileListener());
    }

    private BlueprintFile() {
        super();
    }
    
    public BlueprintFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }

    public void delete() {
        removeBlueprint();        
        removeRootDomainObject();
        deleteDomainObject();
        deleteFile();
    }

    private void deleteFile() {
        final IFileManager fileManager = FileManagerFactory.getFileManager();
        fileManager.deleteFile(getExternalStorageIdentification());
    }

    public String getDirectDownloadUrlFormat() {
        return FileManagerFactory.getFileManager().getDirectDownloadUrlFormat(
                getExternalStorageIdentification(), getFilename());
    }

    private static class BlueprintBlueprintFileListener extends
            dml.runtime.RelationAdapter<BlueprintFile, Blueprint> {
        @Override
        public void afterRemove(BlueprintFile blueprintFile, Blueprint blueprint) {
            if (blueprintFile != null && blueprint != null) {
                blueprintFile.delete();
            }
        }
    }
}
