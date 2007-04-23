package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class UploadUnitSiteLogo extends Service {

    public File run(UnitSite site, InputStream stream, String name) {
        
        if (site.hasLogo()) {
            site.getLogo().delete();
        }
        
        if (stream == null || name == null) {
            return null;
        }
        
        VirtualPath filePath = getVirtualPath(site);
        Collection<FileSetMetaData> metaData = createMetaData(site, name);
        
        FileDescriptor descriptor = saveFile(filePath, name, false, metaData, stream);
        
        UnitSiteFile file = new UnitSiteFile(descriptor.getUniqueId(), name);
        file.setSize(descriptor.getSize());
        file.setMimeType(descriptor.getMimeType());
        file.setChecksum(descriptor.getChecksum());
        file.setChecksumAlgorithm(descriptor.getChecksumAlgorithm());

        file.setPermittedGroup(null);
        
        site.setLogo(file);
        
        return file;
    }

    private VirtualPath getVirtualPath(UnitSite site) {
        
        VirtualPathNode[] nodes = { 
                new VirtualPathNode("Site", "Site"),
                new VirtualPathNode("Unit", "Unit"),
                new VirtualPathNode(site.getUnit().getNameWithAcronym(), site.getUnit().getNameWithAcronym()),
                new VirtualPathNode("Logo" + site.getIdInternal(), "Logo")
        };
        
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

    private FileDescriptor saveFile(VirtualPath filePath, String fileName, boolean isPrivate, Collection<FileSetMetaData> metaData, InputStream stream) {
        IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
        return fileManager.saveFile(filePath, fileName, isPrivate, metaData, stream);
    }
}
