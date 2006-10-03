package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class PictureMaterialFile extends PictureMaterialFile_Base {

	public PictureMaterialFile() {
		super();
	}

	public PictureMaterialFile(String filename, String displayName, String mimeType, String checksum,
			String checksumAlgorithm, Integer size, String externalStorageIdentification,
			Group permittedGroup) {
		this();
		init(filename, displayName == null ? filename : filename, mimeType, checksum, checksumAlgorithm,
				size, externalStorageIdentification, permittedGroup);
	}

	public void delete(NewPictureMaterial pictureMaterial) {
		this.removePictureMaterials(pictureMaterial);

		if (this.getPictureMaterialsCount() == 0) {
			this.removeRootDomainObject();

			super.deleteDomainObject();
		}
	}

}
