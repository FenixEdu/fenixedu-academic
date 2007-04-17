package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.InputStream;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;

public class ProtocolFileBean extends OpenFileBean {

    private FilePermissionType filePermissionType;

    public ProtocolFileBean(InputStream inputStream, String fileName,
            FilePermissionType filePermissionType) {
        setInputStream(inputStream);
        setFileName(fileName);
        setFilePermissionType(filePermissionType);
    }

    public String getFilePermissionTypeName() {
        return filePermissionType.toString();
    }
    
    public FilePermissionType getFilePermissionType() {
        return filePermissionType;
    }

    public void setFilePermissionType(FilePermissionType filePermissionType) {
        this.filePermissionType = filePermissionType;
    }

}
