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

import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteFile;
import pt.ist.fenixframework.Atomic;

import com.google.common.io.Files;

public class UploadUnitSiteLogo {

    protected File run(UnitSite site, java.io.File fileToUpload, String name) throws IOException, FenixServiceException {

//        if (site.hasLogo()) {
//            UnitSiteFile logo = site.getLogo();
//            if (logo.getExternalStorageIdentification() != null) {
//                new DeleteFileRequest(AccessControl.getPerson(), logo.getExternalStorageIdentification());
//            }
//
//            logo.delete();
//        }

        if (fileToUpload == null || name == null) {
            return null;
        }

        UnitSiteFile file = new UnitSiteFile(name, name, Files.toByteArray(fileToUpload), null);

        file.setPermittedGroup(null);

        site.setLogo(file);

        return file;
    }

    // Service Invokers migrated from Berserk

    private static final UploadUnitSiteLogo serviceInstance = new UploadUnitSiteLogo();

    @Atomic
    public static File runUploadUnitSiteLogo(UnitSite site, java.io.File fileToUpload, String name) throws IOException,
            FenixServiceException {
        ResearchSiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, fileToUpload, name);
    }
}
