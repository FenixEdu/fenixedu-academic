package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class BlueprintFile extends BlueprintFile_Base {

    static {
	BlueprintBlueprintFile.addListener(new BlueprintBlueprintFileListener());
    }

    private BlueprintFile() {
	super();
    }

    public BlueprintFile(Blueprint blueprint, String filename, String displayName, String mimeType,
	    String checksum, String checksumAlgorithm, Integer size,
	    String externalStorageIdentification, Group permittedGroup) {

	this();
	setBlueprint(blueprint);
	init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
		externalStorageIdentification, permittedGroup);
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
	final IFileManager fileManager = FileManagerFactory.getFileManager();
	fileManager.deleteFile(getExternalStorageIdentification());
    }

    public String getDirectDownloadUrlFormat() {
	return FileManagerFactory.getFileManager().getDirectDownloadUrlFormat(
		getExternalStorageIdentification(), getDisplayName());
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
