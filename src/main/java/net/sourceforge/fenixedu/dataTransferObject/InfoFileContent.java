package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.FileContent;

/**
 * 
 * @author naat
 * 
 */
public class InfoFileContent extends InfoObject {

    public static final Comparator<InfoFileContent> COMPARATOR_BY_DISPLAY_NAME = new Comparator<InfoFileContent>() {

        @Override
        public int compare(InfoFileContent o1, InfoFileContent o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }

    };

    private String name;

    private String displayName;

    private String mimeType;

    private String checksum;

    private String checksumAlgorithm;

    private Integer size;

    private String externalStorageIdentification;

    public InfoFileContent() {

    }

    public void copyFromDomain(FileContent fileItem) {
        super.copyFromDomain(fileItem);
        if (fileItem != null) {
            setName(fileItem.getFilename());
            setDisplayName(fileItem.getDisplayName());
            setMimeType(fileItem.getMimeType());
            setChecksum(fileItem.getChecksum());
            setChecksumAlgorithm(fileItem.getChecksumAlgorithm());
            setSize(fileItem.getSize().intValue());
            setExternalStorageIdentification(fileItem.getExternalStorageIdentification());

        }
    }

    /**
     * @param item
     * @return
     */
    public static InfoFileContent newInfoFromDomain(FileContent item) {
        InfoFileContent infoItem = null;
        if (item != null) {
            infoItem = new InfoFileContent();
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

    public String getHtmlFriendlyFilename() {
        return getFilename().replaceAll("&", "&amp;").replaceAll(" ", "%20");
    }

}