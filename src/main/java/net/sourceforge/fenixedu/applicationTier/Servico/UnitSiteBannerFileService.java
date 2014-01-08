package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;

import com.google.common.io.Files;

public class UnitSiteBannerFileService {

    protected UnitSiteBannerFile createBannerFile(UnitSite site, File fileToUpload, String name) throws FenixServiceException,
            IOException {
        if (fileToUpload == null) {
            return null;
        }
        return new UnitSiteBannerFile(name, name, Files.toByteArray(fileToUpload), null);
    }

    protected void updateBanner(UnitSite site, UnitSiteBanner banner, File mainFile, String mainName, File backFile,
            String backName, UnitSiteBannerRepeatType repeat, String color, String link, Integer weight)
            throws FenixServiceException, IOException {
        UnitSiteBannerFile main = createBannerFile(site, mainFile, mainName);
        UnitSiteBannerFile background = createBannerFile(site, backFile, backName);

        if (main != null) {
            banner.setMainImage(main);
        }

        if (background != null) {
            banner.setBackgroundImage(background);
        }

        banner.setRepeatType(repeat);
        banner.setColor(color);
        banner.setLink(link);
        banner.setWeight(weight);
    }

    protected void deleteFile(UnitSiteBannerFile bannerFile) {
        if (bannerFile != null) {
            bannerFile.delete();
        }
    }

}
