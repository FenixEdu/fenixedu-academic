package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class BlueprintFile extends BlueprintFile_Base {

    static {
	BlueprintBlueprintFile.addListener(new BlueprintBlueprintFileListener());
    }  

    public BlueprintFile(Blueprint blueprint, String filename, String displayName, String mimeType,
	    String checksum, String checksumAlgorithm, Integer size,
	    String externalStorageIdentification, Group permittedGroup) {

	super();
	setBlueprint(blueprint);
	init(filename, displayName, mimeType, checksum, checksumAlgorithm, size, externalStorageIdentification, permittedGroup);
    }

    @Override
    public void setBlueprint(Blueprint blueprint) {
	if (blueprint == null) {
	    throw new DomainException("error.blueprintFile.no.blueprint");
	}
	super.setBlueprint(blueprint);
    }

    private void delete() {
	super.setBlueprint(null);
	removeRootDomainObject();
	deleteDomainObject();
	deleteFile();
    }

    private void deleteFile() {
    	new DeleteFileRequest(AccessControl.getPerson(),getExternalStorageIdentification());
    }

    public String getDirectDownloadUrlFormat() {
        return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(getExternalStorageIdentification(), getDisplayName());
    }

    private static class BlueprintBlueprintFileListener extends dml.runtime.RelationAdapter<BlueprintFile, Blueprint> {
	@Override
	public void afterRemove(BlueprintFile blueprintFile, Blueprint blueprint) {
	    if (blueprintFile != null && blueprint != null) {
		blueprintFile.delete();
	    }
	}
    }
}
