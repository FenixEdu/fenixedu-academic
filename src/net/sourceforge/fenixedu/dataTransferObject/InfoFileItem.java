package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;

/**
 * 
 * @author naat
 * 
 */
public class InfoFileItem extends InfoObject {

    private String name;

    private String displayName;

    private String mimeType;

    private String checksum;

    private String checksumAlgorithm;

    private Integer size;

    private String externalStorageIdentification;
    
    private FileItemPermittedGroupType permittedGroupType;

    public InfoFileItem() {

    }

    public void copyFromDomain(FileItem fileItem) {
        super.copyFromDomain(fileItem);
        if (fileItem != null) {
            setName(fileItem.getFilename());
            setDisplayName(fileItem.getDisplayName());
            setMimeType(fileItem.getMimeType());
            setChecksum(fileItem.getChecksum());
            setChecksumAlgorithm(fileItem.getChecksumAlgorithm());
            setSize(fileItem.getSize());
            setExternalStorageIdentification(fileItem.getExternalStorageIdentification());
            setPermittedGroupType(fileItem.getFileItemPermittedGroupType());
            
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

    public String getExternalStorageIdentification() {
        return externalStorageIdentification;
    }

    public void setExternalStorageIdentification(String externalStorageIdentification) {
        this.externalStorageIdentification = externalStorageIdentification;
    }

    public String getFilename() {
        return name;
    }

    public void setName(String filename) {
        this.name = filename;
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
    
    public String getHtmlFriendlyFilename() {
        return getFilename().replaceAll("&", "&amp;").replaceAll(" ", "%20");
    }

}