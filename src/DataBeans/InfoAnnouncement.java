package DataBeans;

import java.sql.Timestamp;

import Dominio.IAnnouncement;

/**
 * @author EP 15
 * @author João Mota
 * @author Ivo Brandão
 */

public class InfoAnnouncement extends InfoObject implements Comparable,
        ISiteComponent {

    private String title;

    private Timestamp creationDate;

    private Timestamp lastModifiedDate;

    private String information;

    private InfoSite infoSite;

    public InfoAnnouncement() {
    }

    public InfoAnnouncement(String title, Timestamp creationDate,
            Timestamp lastModifiedDate, String information, InfoSite infoSite) {
        this.title = title;
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.information = information;
        this.infoSite = infoSite;
    }

    public boolean equals(Object obj) {

        boolean resultado = false;
        if (obj != null && obj instanceof InfoAnnouncement) {
            resultado = getTitle().equals(((InfoAnnouncement) obj).getTitle())
                    && getCreationDate().equals(
                            ((InfoAnnouncement) obj).getCreationDate())
                    && getLastModifiedDate().equals(
                            ((InfoAnnouncement) obj).getLastModifiedDate())
                    && getInformation().equals(
                            ((InfoAnnouncement) obj).getInformation())
                    && getInfoSite().equals(
                            ((InfoAnnouncement) obj).getInfoSite());
        }
        return resultado;
    }

    public String toString() {
        String result = "[INFOANNOUNCEMENT";
        result += ", title=" + getTitle();
        result += ", information=" + getInformation();
        result += ", creationDate=" + getCreationDate();
        result += ", lastModifiedDate=" + getLastModifiedDate();
        result += ", infoSite=" + getInfoSite();
        result += "]";
        return result;
    }

    /**
     * @return Timestamp
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * @return String
     */
    public String getInformation() {
        return information;
    }

    /**
     * @return InfoSite
     */
    public InfoSite getInfoSite() {
        return infoSite;
    }

    /**
     * @return Timestamp
     */
    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the creationDate.
     * 
     * @param creationDate
     *            The creationDate to set
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets the information.
     * 
     * @param information
     *            The information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * Sets the infoSite.
     * 
     * @param infoSite
     *            The infoSite to set
     */
    public void setInfoSite(InfoSite infoSite) {
        this.infoSite = infoSite;
    }

    /**
     * Sets the lastModifiedDate.
     * 
     * @param lastModifiedDate
     *            The lastModifiedDate to set
     */
    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        if (getYoungerDate().after(((InfoAnnouncement) arg0).getYoungerDate())) {
            return -1;
        } else {
            if (getYoungerDate().before(
                    ((InfoAnnouncement) arg0).getYoungerDate())) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public String getCreationDateFormatted() {
        String result = this.creationDate.toString();

        return result.substring(0, result.indexOf("."));

    }

    public String getLastModifiedDateFormatted() {
        String result = this.lastModifiedDate.toString();

        return result.substring(0, result.indexOf("."));

    }

    private Timestamp getYoungerDate() {

        if (getLastModifiedDate() != null
                && getLastModifiedDate().after(getCreationDate())) {
            return getLastModifiedDate();
        } else {
            return getCreationDate();
        }

    }

    public void copyFromDomain(IAnnouncement announcement) {
        super.copyFromDomain(announcement);
        if (announcement != null) {
            setCreationDate(announcement.getCreationDate());
            setInformation(announcement.getInformation());
            setLastModifiedDate(announcement.getLastModifiedDate());
            setTitle(announcement.getTitle());
        }
    }

    public static InfoAnnouncement newInfoFromDomain(IAnnouncement announcement) {
        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null) {
            infoAnnouncement = new InfoAnnouncement();
            infoAnnouncement.copyFromDomain(announcement);
        }
        return infoAnnouncement;
    }
}