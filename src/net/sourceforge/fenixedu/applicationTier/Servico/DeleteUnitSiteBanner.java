package net.sourceforge.fenixedu.applicationTier.Servico;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class DeleteUnitSiteBanner extends Service {

	public void run(UnitSite site, UnitSiteBanner banner) {
		UnitSiteBannerFile mainImage = banner.getMainImage();
		UnitSiteBannerFile backgroundImage = banner.getBackgroundImage();

		String mainImageId = null;
		String backgroundImageId = null;

		if (mainImage != null) {
			mainImageId = mainImage.getExternalStorageIdentification();
		}
		if (backgroundImage != null) {
			backgroundImageId = backgroundImage.getExternalStorageIdentification();
		}

		IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();

		banner.delete();

		if (mainImage != null) {
			new DeleteFileRequest(AccessControl.getPerson(),mainImageId);
		}

		if (backgroundImage != null) {
			new DeleteFileRequest(AccessControl.getPerson(), backgroundImageId);
		}

	}

}
