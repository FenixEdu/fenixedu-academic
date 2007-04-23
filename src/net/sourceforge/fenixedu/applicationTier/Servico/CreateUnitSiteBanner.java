package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;

public class CreateUnitSiteBanner extends UnitSiteBannerFileService {

    public UnitSiteBanner run(UnitSite site, InputStream mainStream, String mainName, InputStream backStream, String backName, String color) {
        if (mainStream == null || mainName == null) {
            return null;
        }

        UnitSiteBanner banner = new UnitSiteBanner(site);
        updateBanner(site, banner, mainStream, mainName, backStream, backName, color);
        
        return banner;
    }
    
}
