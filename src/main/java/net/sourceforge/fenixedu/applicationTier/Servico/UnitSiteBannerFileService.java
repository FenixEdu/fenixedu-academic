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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;

import org.fenixedu.bennu.core.groups.AnyoneGroup;

import com.google.common.io.Files;

public class UnitSiteBannerFileService {

    protected UnitSiteBannerFile createBannerFile(UnitSite site, File fileToUpload, String name) throws FenixServiceException,
            IOException {
        if (fileToUpload == null) {
            return null;
        }
        return new UnitSiteBannerFile(name, name, Files.toByteArray(fileToUpload), AnyoneGroup.get());
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
