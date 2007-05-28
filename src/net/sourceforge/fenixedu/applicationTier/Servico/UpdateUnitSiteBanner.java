package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;

public class UpdateUnitSiteBanner extends UnitSiteBannerFileService {

    public void run(UnitSite site, UnitSiteBanner banner, File mainFile, String mainName, File backFile, String backName, UnitSiteBannerRepeatType repeat, String color, String link, Integer weight) throws FenixServiceException, IOException {
        UnitSiteBannerFile main = banner.getMainImage();
        if (main != null && mainFile != null) {
        	deleteFile(main);
        }
        
        UnitSiteBannerFile background = banner.getBackgroundImage();
        if (background != null && mainFile != null) {
        	deleteFile(background);
        }

        updateBanner(site, banner, mainFile, mainName, backFile, backName, repeat, color, link, weight);
    }
    
}
