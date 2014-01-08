package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteFile;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixframework.Atomic;

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

        UnitSiteFile file = new UnitSiteFile(name, name, FileUtils.readFileToByteArray(fileToUpload), null);

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
