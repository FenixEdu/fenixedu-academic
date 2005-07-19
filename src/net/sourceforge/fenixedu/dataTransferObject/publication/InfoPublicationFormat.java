package net.sourceforge.fenixedu.dataTransferObject.publication;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.publication.IPublicationFormat;

public class InfoPublicationFormat extends InfoObject {

    private String format;

    public InfoPublicationFormat() {
        super();
    }
    
    public static InfoPublicationFormat newInfoFromDomain(IPublicationFormat publicationFormat) {
        InfoPublicationFormat infoPublicationFormat = new InfoPublicationFormat();
        if(publicationFormat != null) {
            infoPublicationFormat.copyFromDomain(publicationFormat);
        }
        return infoPublicationFormat;
    }
    
    public void copyFromDomain(IPublicationFormat publicationFormat) {
        super.copyFromDomain(publicationFormat);
        if (publicationFormat != null) {
            format = publicationFormat.getFormat();
        }
    }

    /**
     * @return Returns the subtype.
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param subtype
     *            The subtype to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }



}