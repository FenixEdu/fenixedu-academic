/*
 * Created on 22/Nov/2003
 *  
 */
package DataBeans.teacher;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Dominio.teacher.IOldPublication;
import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoOldPublication extends InfoObject {
    private OldPublicationType oldPublicationType;

    private String oldPublicationTypeString;

    private Date lastModificationDate;

    private String publication;

    private InfoTeacher infoTeacher;

    /**
     *  
     */
    public InfoOldPublication() {
        super();
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate
     *            The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /**
     * @return Returns the oldPublicationType.
     */
    public OldPublicationType getOldPublicationType() {
        return oldPublicationType;
    }

    /**
     * @param oldPublicationType
     *            The oldPublicationType to set.
     */
    public void setOldPublicationType(OldPublicationType oldPublicationType) {
        this.oldPublicationType = oldPublicationType;
        this.oldPublicationTypeString = oldPublicationType.getName();
    }

    /**
     * @return Returns the publication.
     */
    public String getPublication() {
        return publication;
    }

    /**
     * @param publication
     *            The publication to set.
     */
    public void setPublication(String publication) {
        this.publication = publication;
    }

    /**
     * @return Returns the oldPublicationTypeString.
     */
    public String getOldPublicationTypeString() {
        return oldPublicationTypeString;
    }

    /**
     * @param oldPublicationTypeString
     *            The oldPublicationTypeString to set.
     */
    public void setOldPublicationTypeString(String oldPublicationTypeString) {
        this.oldPublicationTypeString = oldPublicationTypeString;
        this.oldPublicationType = OldPublicationType.getEnum(oldPublicationTypeString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IOldPublication oldPublication) {
        super.copyFromDomain(oldPublication);

        if (oldPublication != null) {
            setLastModificationDate(oldPublication.getLastModificationDate());
            setOldPublicationType(oldPublication.getOldPublicationType());
            setOldPublicationTypeString(oldPublication.getOldPublicationType().toString());
            setPublication(oldPublication.getPublication());
        }
    }

    public static InfoOldPublication newInfoFromDomain(IOldPublication oldPublication) {
        InfoOldPublication infoOldPublication = null;
        if (oldPublication != null) {
            infoOldPublication = new InfoOldPublication();
            infoOldPublication.copyFromDomain(oldPublication);
        }
        return infoOldPublication;
    }
}