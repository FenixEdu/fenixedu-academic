package net.sourceforge.fenixedu.applicationTier.Servico;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;

public class DeleteUnitSiteBanner extends Service {

	public void run(UnitSite site, UnitSiteBanner banner) {
		UnitSiteBannerFile mainImage = banner.getMainImage();
		UnitSiteBannerFile backgroundImage = banner.getBackgroundImage();

		String mainImageId = mainImage.getExternalStorageIdentification();
		String backgroundImageId = backgroundImage.getExternalStorageIdentification();
		IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();

		banner.delete();

		if (mainImage != null) {
			fileManager.deleteFile(mainImageId);
		}

		if (backgroundImage != null) {
			fileManager.deleteFile(backgroundImageId);
		}

	}

}
