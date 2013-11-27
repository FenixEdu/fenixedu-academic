package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResearchSiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

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

        VirtualPath filePath = getVirtualPath(site);
        Collection<FileSetMetaData> metaData = createMetaData(site, name);

        UnitSiteFile file = new UnitSiteFile(filePath, name, name, metaData, FileUtils.readFileToByteArray(fileToUpload), null);

        file.setPermittedGroup(null);

        site.setLogo(file);

        return file;
    }

    private VirtualPath getVirtualPath(UnitSite site) {

        VirtualPathNode[] nodes =
                { new VirtualPathNode("Site", "Site"), new VirtualPathNode("Unit", "Unit"),
                        new VirtualPathNode(site.getUnit().getNameWithAcronym(), site.getUnit().getNameWithAcronym()),
                        new VirtualPathNode("Logo" + site.getExternalId(), "Logo") };

        VirtualPath path = new VirtualPath();
        for (VirtualPathNode node : nodes) {
            path.addNode(node);
        }

        return path;
    }

    private Collection<FileSetMetaData> createMetaData(UnitSite site, String fileName) {
        List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();

        metaData.add(FileSetMetaData.createAuthorMeta(AccessControl.getPerson().getName()));
        metaData.add(FileSetMetaData.createTitleMeta(site.getUnit().getNameWithAcronym() + " Logo"));

        return metaData;
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
