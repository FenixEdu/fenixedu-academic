package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.FileItem;

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

    public InfoFileItem() {

    }

    public void copyFromDomain(FileItem item) {
        super.copyFromDomain(item);
        if (item != null) {
            setFilename(item.getFilename());
            setDisplayName(item.getDisplayName());
            setMimeType(item.getMimeType());
            setChecksum(item.getChecksum());
            setChecksumAlgorithm(item.getChecksumAlgorithm());
            setSize(item.getSize());
            setDspaceBitstreamIdentification(item.getDspaceBitstreamIdentification());
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

}