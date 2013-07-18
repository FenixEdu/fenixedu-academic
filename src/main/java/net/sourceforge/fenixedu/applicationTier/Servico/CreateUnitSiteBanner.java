package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;
import pt.ist.fenixWebFramework.services.Service;

public class CreateUnitSiteBanner extends UnitSiteBannerFileService {

    protected UnitSiteBanner run(UnitSite site, File mainFile, String mainName, File backFile, String backName,
            UnitSiteBannerRepeatType repeat, String color, String link, Integer weight) throws FenixServiceException, IOException {
        if (mainFile == null || mainName == null) {
            return null;
        }

        UnitSiteBanner banner = new UnitSiteBanner(site);
        updateBanner(site, banner, mainFile, mainName, backFile, backName, repeat, color, link, weight);

        return banner;
    }

    // Service Invokers migrated from Berserk

    private static final CreateUnitSiteBanner serviceInstance = new CreateUnitSiteBanner();

    @Service
    public static UnitSiteBanner runCreateUnitSiteBanner(UnitSite site, File mainFile, String mainName, File backFile,
            String backName, UnitSiteBannerRepeatType repeat, String color, String link, Integer weight)
            throws FenixServiceException, IOException, NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, mainFile, mainName, backFile, backName, repeat, color, link, weight);
    }

}