package net.sourceforge.fenixedu.domain;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.io.domain.FileStorage;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

/**
 * {@link FileStorage} implementation that delegates read operations to a running DSpace instance.
 * 
 * <p>
 * Implementation notes:
 * <ul>
 * <li>Only reads are supported</li>
 * <li>Only supports subclasses of {@link File} with an External Storage Identification</li>
 * </p>
 * 
 * @deprecated DSpace support has been deprecated as of version 2.0.0. In order to keep applications compatible with existing
 *             DSpace stores, this class has been created. Existing files should be migrated from DSpace, as support will be
 *             permanently removed in version 3.0.0.
 * 
 * @since 2.0.0
 * 
 */
@Deprecated
public class DSpaceFileStorage extends DSpaceFileStorage_Base {
    private static final String DSPACE_REMOTE_DOWNLOAD_SERVLET = "/DSpaceFileSetDownloadServlet";

    private DSpaceFileStorage() {
        super();
        setName(DSpaceFileStorage.class.getSimpleName());
    }

    public static DSpaceFileStorage getInstance() {
        if (Bennu.getInstance().getDSpaceFileStorage() == null) {
            initDSpaceFileStorage();
        }
        return Bennu.getInstance().getDSpaceFileStorage();
    }

    @Atomic(mode = TxMode.WRITE)
    private static void initDSpaceFileStorage() {
        Bennu.getInstance().setDSpaceFileStorage(new DSpaceFileStorage());
    }

    @Override
    public String store(String uniqueIdentification, byte[] content) {
        if (content == null) {
            File file = FenixFramework.getDomainObject(uniqueIdentification);
            new DeleteFileRequest(AccessControl.getPerson(), file.getExternalStorageIdentification(), true);
        }
        throw new UnsupportedOperationException("dspace storage is read-only");
    }

    @Override
    public byte[] read(String uniqueIdentification) {
        try {
            return ByteStreams.toByteArray(readAsInputStream(uniqueIdentification));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Override
    public InputStream readAsInputStream(String uniqueIdentification) {
        File file = FenixFramework.getDomainObject(uniqueIdentification);
        HttpClient client = new HttpClient();

        String remoteDownloadInterfaceUrl =
                FenixConfigurationManager.getConfiguration().getDspaceServerUrl() + DSPACE_REMOTE_DOWNLOAD_SERVLET;

        String username = FenixConfigurationManager.getConfiguration().getDspaceUsername();
        String password = FenixConfigurationManager.getConfiguration().getDspacePassword();

        String downloadUrl =
                remoteDownloadInterfaceUrl + "?username=" + username + "&password=" + password + "&uniqueId="
                        + file.getExternalStorageIdentification();
        GetMethod gm = new GetMethod(downloadUrl);

        int result;
        try {
            result = client.executeMethod(gm);
            if (result == HttpStatus.SC_OK) {
                return gm.getResponseBodyAsStream();
            }
            throw new Error("Unable get stream for " + uniqueIdentification);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

}
