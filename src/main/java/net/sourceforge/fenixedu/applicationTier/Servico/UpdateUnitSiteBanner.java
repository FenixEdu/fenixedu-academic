/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;
import pt.ist.fenixframework.Atomic;

public class UpdateUnitSiteBanner extends UnitSiteBannerFileService {

    protected void run(UnitSite site, UnitSiteBanner banner, File mainFile, String mainName, File backFile, String backName,
            UnitSiteBannerRepeatType repeat, String color, String link, Integer weight) throws FenixServiceException, IOException {
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

    // Service Invokers migrated from Berserk

    private static final UpdateUnitSiteBanner serviceInstance = new UpdateUnitSiteBanner();

    @Atomic
    public static void runUpdateUnitSiteBanner(UnitSite site, UnitSiteBanner banner, File mainFile, String mainName,
            File backFile, String backName, UnitSiteBannerRepeatType repeat, String color, String link, Integer weight)
            throws FenixServiceException, IOException, NotAuthorizedException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, banner, mainFile, mainName, backFile, backName, repeat, color, link, weight);
    }

}