package net.sourceforge.fenixedu.domain.space;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class BlueprintFile extends BlueprintFile_Base {

	static {
		BlueprintBlueprintFile.addListener(new BlueprintBlueprintFileListener());
	}

	public BlueprintFile(VirtualPath path, Blueprint blueprint, String filename, String displayName, Group permittedGroup,
			byte[] content, Collection<FileSetMetaData> metadata) {

		super();
		setBlueprint(blueprint);
		init(path, filename, displayName, metadata, content, permittedGroup);
	}

	@Override
	public void setBlueprint(Blueprint blueprint) {
		if (blueprint == null) {
			throw new DomainException("error.blueprintFile.no.blueprint");
		}
		super.setBlueprint(blueprint);
	}

	@Override
	public void delete() {
		super.setBlueprint(null);
		super.delete();
	}

	public String getDirectDownloadUrlFormat() {
		return FileManagerFactory.getFactoryInstance().getFileManager()
				.formatDownloadUrl(getExternalStorageIdentification(), getDisplayName());
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
