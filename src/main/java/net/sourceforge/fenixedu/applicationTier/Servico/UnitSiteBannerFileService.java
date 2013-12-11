package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.io.FileUtils;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class UnitSiteBannerFileService {

    protected UnitSiteBannerFile createBannerFile(UnitSite site, File fileToUpload, String name) throws FenixServiceException,
            IOException {
        if (fileToUpload == null) {
            return null;
        }

        VirtualPath filePath = getVirtualPath(site);
        Collection<FileSetMetaData> metaData = createMetaData(site, name);

        return new UnitSiteBannerFile(filePath, name, name, metaData, FileUtils.readFileToByteArray(fileToUpload), null);
    }

    protected VirtualPath getVirtualPath(UnitSite site) {

        VirtualPathNode[] nodes =
                { new VirtualPathNode("Site", "Site"), new VirtualPathNode("Unit", "Unit"),
                        new VirtualPathNode(site.getUnit().getNameWithAcronym(), site.getUnit().getNameWithAcronym()),
                        new VirtualPathNode("Banner" + site.getExternalId(), "Banner") };

        VirtualPath path = new VirtualPath();
        for (VirtualPathNode node : nodes) {
            path.addNode(node);
        }

        return path;
    }

    protected Collection<FileSetMetaData> createMetaData(UnitSite site, String fileName) {
        List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();

        metaData.add(FileSetMetaData.createAuthorMeta(AccessControl.getPerson().getName()));
        metaData.add(FileSetMetaData.createTitleMeta(site.getUnit().getNameWithAcronym() + " Banner"));

        return metaData;
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
