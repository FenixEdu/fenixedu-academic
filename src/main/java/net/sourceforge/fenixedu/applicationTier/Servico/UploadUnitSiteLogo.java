package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.io.FileUtils;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class UploadUnitSiteLogo extends FenixService {

    public File run(UnitSite site, java.io.File fileToUpload, String name) throws IOException, FenixServiceException {

        if (site.hasLogo()) {
            UnitSiteFile logo = site.getLogo();
            if (logo.getExternalStorageIdentification() != null) {
            	new DeleteFileRequest(AccessControl.getPerson(), logo.getExternalStorageIdentification());
            }

            logo.delete();
        }

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
                        new VirtualPathNode("Logo" + site.getIdInternal(), "Logo") };

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

    private FileDescriptor saveFile(VirtualPath filePath, String fileName, boolean isPrivate,
            Collection<FileSetMetaData> metaData, java.io.File file) throws IOException, FenixServiceException {
        IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return fileManager.saveFile(filePath, fileName, isPrivate, metaData, file);
        } catch (FileNotFoundException e) {
            throw new FenixServiceException(e.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }
}
