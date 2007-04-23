package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;

public class UpdateUnitSiteBanner extends UnitSiteBannerFileService {

    public void run(UnitSite site, UnitSiteBanner banner, InputStream mainStream, String mainName, InputStream backStream, String backName, String color) {
        UnitSiteBannerFile main = banner.getMainImage();
        if (main != null && mainStream != null) {
            main.delete();
        }
        
        UnitSiteBannerFile background = banner.getBackgroundImage();
        if (background != null && backStream != null) {
            background.delete();
        }

        updateBanner(site, banner, mainStream, mainName, backStream, backName, color);
    }
    
}
