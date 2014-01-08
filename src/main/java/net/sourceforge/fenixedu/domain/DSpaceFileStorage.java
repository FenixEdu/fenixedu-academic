package net.sourceforge.fenixedu.domain;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.io.ByteStreams;

@Deprecated
public class DSpaceFileStorage extends DSpaceFileStorage_Base {
    private static final String DSPACE_REMOTE_DOWNLOAD_SERVLET = "/DSpaceFileSetDownloadServlet";

    protected DSpaceFileStorage() {
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
            new DeleteFileRequest(AccessControl.getPerson(), uniqueIdentification, true);
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
        HttpClient client = new HttpClient();

        String remoteDownloadInterfaceUrl =
                FenixConfigurationManager.getConfiguration().getDspaceServerUrl() + DSPACE_REMOTE_DOWNLOAD_SERVLET;

        String username = FenixConfigurationManager.getConfiguration().getDspaceUsername();
        String password = FenixConfigurationManager.getConfiguration().getDspacePassword();

        String downloadUrl =
                remoteDownloadInterfaceUrl + "?username=" + username + "&password=" + password + "&uniqueId="
                        + uniqueIdentification;
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
