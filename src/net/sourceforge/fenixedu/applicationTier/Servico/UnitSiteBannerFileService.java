package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class UnitSiteBannerFileService extends Service {

    protected UnitSiteBannerFile createBannerFile(UnitSite site, InputStream stream, String name) {
        if (stream == null) {
            return null;
        }
        
        VirtualPath filePath = getVirtualPath(site);
        Collection<FileSetMetaData> metaData = createMetaData(site, name);
        
        FileDescriptor descriptor = saveFile(filePath, name, false, metaData, stream);
        
        UnitSiteBannerFile file = new UnitSiteBannerFile(descriptor.getUniqueId(), name);
        file.setSize(descriptor.getSize());
        file.setMimeType(descriptor.getMimeType());
        file.setChecksum(descriptor.getChecksum());
        file.setChecksumAlgorithm(descriptor.getChecksumAlgorithm());

        file.setPermittedGroup(null);
        
        return file;
    }
    
    protected VirtualPath getVirtualPath(UnitSite site) {
        
        VirtualPathNode[] nodes = { 
                new VirtualPathNode("Site", "Site"),
                new VirtualPathNode("Unit", "Unit"),
                new VirtualPathNode(site.getUnit().getNameWithAcronym(), site.getUnit().getNameWithAcronym()),
                new VirtualPathNode("Banner" + site.getIdInternal(), "Banner")
        };
        
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

    protected FileDescriptor saveFile(VirtualPath filePath, String fileName, boolean isPrivate, Collection<FileSetMetaData> metaData, InputStream stream) {
        IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
        return fileManager.saveFile(filePath, fileName, isPrivate, metaData, stream);
    }
    
    protected void updateBanner(UnitSite site, UnitSiteBanner banner, InputStream mainStream, String mainName, InputStream backStream, String backName, String color) {
        UnitSiteBannerFile main = createBannerFile(site, mainStream, mainName);
        UnitSiteBannerFile background = createBannerFile(site, backStream, backName);
        
        if (main != null) {
            banner.setMainImage(main);
        }
        
        if (background != null) {
            banner.setBackgroundImage(background);
        }
        
        banner.setColor(color);
    }

}
