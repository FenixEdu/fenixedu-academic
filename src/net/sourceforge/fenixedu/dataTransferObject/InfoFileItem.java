package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;

/**
 * 
 * @author naat
 * 
 */
public class InfoFileItem extends InfoObject {

    private String filename;

    private String displayName;

    private String mimeType;

    private String checksum;

    private String checksumAlgorithm;

    private Integer size;

    private String dspaceBitstreamIdentification;
    
    private FileItemPermittedGroupType permittedGroupType;

    public InfoFileItem() {

    }

    public void copyFromDomain(FileItem fileItem) {
        super.copyFromDomain(fileItem);
        if (fileItem != null) {
            setFilename(fileItem.getFilename());
            setDisplayName(fileItem.getDisplayName());
            setMimeType(fileItem.getMimeType());
            setChecksum(fileItem.getChecksum());
            setChecksumAlgorithm(fileItem.getChecksumAlgorithm());
            setSize(fileItem.getSize());
            setDspaceBitstreamIdentification(fileItem.getDspaceBitstreamIdentification());
            setPermittedGroupType(fileItem.getPermittedGroupType());
            
        }
    }

    /**
     * @param item
     * @return
     */
    public static InfoFileItem newInfoFromDomain(FileItem item) {
        InfoFileItem infoItem = null;
        if (item != null) {
            infoItem = new InfoFileItem();
            infoItem.copyFromDomain(item);
        }
        return infoItem;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDspaceBitstreamIdentification() {
        return dspaceBitstreamIdentification;
    }

    public void setDspaceBitstreamIdentification(String dspaceIdentification) {
        this.dspaceBitstreamIdentification = dspaceIdentification;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public FileItemPermittedGroupType getPermittedGroupType() {
        return permittedGroupType;
    }

    public void setPermittedGroupType(FileItemPermittedGroupType fileItemPermittedGroupType) {
        this.permittedGroupType = fileItemPermittedGroupType;
    }
    
    

}