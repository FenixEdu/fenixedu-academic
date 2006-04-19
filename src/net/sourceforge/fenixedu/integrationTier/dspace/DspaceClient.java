package net.sourceforge.fenixedu.integrationTier.dspace;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.dspace.external.interfaces.remoteManager.objects.FileDelete;
import org.dspace.external.interfaces.remoteManager.objects.FileDeleteResponse;
import org.dspace.external.interfaces.remoteManager.objects.FilePermissionChange;
import org.dspace.external.interfaces.remoteManager.objects.FileUpload;
import org.dspace.external.interfaces.remoteManager.objects.FileUploadResponse;
import org.dspace.external.interfaces.remoteManager.objects.UploadedFileDescriptor;

public class DspaceClient {

    private static final String DSPACE_ENCODING = "UTF-8";

    private static final String SUCCESS_CODE = "SUCCESS";

    private static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    private static final String remoteInterfaceUrl;

    private static final String username;

    private static final String password;

    static {
        String dspaceServerUrl = PropertiesManager.getProperty("dspace.serverUrl");
        remoteInterfaceUrl = dspaceServerUrl + "/DspaceRemoteManagerServlet";
        username = PropertiesManager.getProperty("dspace.username");
        password = PropertiesManager.getProperty("dspace.password");
    }

    private static class DspaceResponse {
        public String responseCode;

        public String responseMessage;

        public DspaceResponse(String responseCode, String responseMessage) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
        }
    }

    private DspaceClient() {

    }

    public static UploadedFileDescriptor uploadFile(FileUpload fileUpload, File file)
            throws DspaceClientException {

        // TODO: change transport channel to RMI

        PostMethod filePost = new PostMethod(remoteInterfaceUrl);
        DspaceResponse response;
        try {
            HttpClient client = new HttpClient();

            Part[] parts = new Part[5];
            parts[0] = new StringPart("username", username, DSPACE_ENCODING);
            parts[1] = new StringPart("password", password, DSPACE_ENCODING);
            parts[2] = new StringPart("method", "uploadFile", DSPACE_ENCODING);
            parts[3] = new StringPart("message", fileUpload.toXml(), DSPACE_ENCODING);
            parts[4] = new FilePart("file", file);

            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

            client.executeMethod(filePost);

            response = getDspaceResponse(filePost.getResponseBodyAsString());

        } catch (HttpException e) {
            throw new DspaceClientException(e);
        } catch (IOException e) {
            throw new DspaceClientException(e);
        } finally {
            filePost.releaseConnection();
        }

        if (!response.responseCode.equals(SUCCESS_CODE)) {
            throw new DspaceClientException(response.responseCode);
        }

        FileUploadResponse fileUploadResponse = FileUploadResponse
                .createFromXml(response.responseMessage);

        if (fileUploadResponse.getError() != null) {
            throw new DspaceClientException(fileUploadResponse.getError());
        }

        return fileUploadResponse.getUploadedFileDescriptor();
    }

    public static void deleteFile(String bitstreamIdentification) throws DspaceClientException {

        // TODO: change transport channel to RMI

        FileDelete fileDelete = new FileDelete(bitstreamIdentification);

        PostMethod post = new PostMethod(remoteInterfaceUrl);
        DspaceResponse response;
        try {

            HttpClient client = new HttpClient();

            post.addParameter("username", username);
            post.addParameter("password", password);
            post.addParameter("method", "deleteFile");
            post.addParameter("message", fileDelete.toXml());

            client.executeMethod(post);

            response = getDspaceResponse(post.getResponseBodyAsString());

        } catch (HttpException e) {
            throw new DspaceClientException(e);
        } catch (IOException e) {
            throw new DspaceClientException(e);
        } finally {
            post.releaseConnection();
        }

        if (!response.responseCode.equals(SUCCESS_CODE)) {
            throw new DspaceClientException(response.responseCode);
        }

        FileDeleteResponse fileDeleteResponse = FileDeleteResponse
                .createFromXml(response.responseMessage);

        if (fileDeleteResponse.getError() != null) {
            throw new DspaceClientException(fileDeleteResponse.getError());
        }
    }

    public static void changeFilePermissions(String bitstreamIdentification, Boolean privateFile)
            throws DspaceClientException {

        // TODO: change transport channel to RMI

        PostMethod post = new PostMethod(remoteInterfaceUrl);
        DspaceResponse response;
        try {

            FilePermissionChange filePermissionChange = new FilePermissionChange(
                    bitstreamIdentification, privateFile);

            HttpClient client = new HttpClient();

            post.addParameter("username", username);
            post.addParameter("password", password);
            post.addParameter("method", "changeFilePermissions");
            post.addParameter("message", filePermissionChange.toXml());

            client.executeMethod(post);

            response = getDspaceResponse(post.getResponseBodyAsString());

        } catch (HttpException e) {
            throw new DspaceClientException(e);
        } catch (IOException e) {
            throw new DspaceClientException(e);
        } finally {
            post.releaseConnection();
        }

        if (!response.responseCode.equals(SUCCESS_CODE)) {
            throw new DspaceClientException(response.responseCode);
        }

        FileDeleteResponse fileDeleteResponse = FileDeleteResponse
                .createFromXml(response.responseMessage);

        if (fileDeleteResponse.getError() != null) {
            throw new DspaceClientException(fileDeleteResponse.getError());
        }

    }

    private static DspaceResponse getDspaceResponse(String rawResponse) {

        int firstIndexOfNewLine = rawResponse.indexOf('\n');
        String responseCode = rawResponse.substring(0, firstIndexOfNewLine);
        String responseMessage = rawResponse.substring(firstIndexOfNewLine + 1);

        return new DspaceResponse(responseCode, responseMessage);
    }

}
