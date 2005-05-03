package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IAnnouncement;

/**
 * @author EP 15
 * @author João Mota
 * @author Ivo Brandão
 */

public class InfoAnnouncement extends InfoObject implements Comparable, ISiteComponent {

    private String title;

    private Date creationDate;

    private Date lastModifiedDate;

    private String information;

    private InfoSite infoSite;

    public InfoAnnouncement() {
    }

    public InfoAnnouncement(String title, Date creationDate, Date lastModifiedDate,
            String information, InfoSite infoSite) {
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
                    && getCreationDate().equals(((InfoAnnouncement) obj).getCreationDate())
                    && getLastModifiedDate().equals(((InfoAnnouncement) obj).getLastModifiedDate())
                    && getInformation().equals(((InfoAnnouncement) obj).getInformation())
                    && getInfoSite().equals(((InfoAnnouncement) obj).getInfoSite());
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

    public Date getCreationDate() {
        return creationDate;
    }

    public String getInformation() {
        return information;
    }

    public InfoSite getInfoSite() {
        return infoSite;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setInfoSite(InfoSite infoSite) {
        this.infoSite = infoSite;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int compareTo(Object arg0) {
        if (getYoungerDate().after(((InfoAnnouncement) arg0).getYoungerDate())) {
            return -1;
        }
        if (getYoungerDate().before(((InfoAnnouncement) arg0).getYoungerDate())) {
            return 1;
        }
        return 0;

    }

    public String getCreationDateFormatted() {
        String result = this.creationDate.toString();

        return result.substring(0, result.indexOf("."));

    }

    public String getLastModifiedDateFormatted() {
        String result = this.lastModifiedDate.toString();

        return result.substring(0, result.indexOf("."));
    }

    private Date getYoungerDate() {

        if (getLastModifiedDate() != null && getLastModifiedDate().after(getCreationDate())) {
            return getLastModifiedDate();
        }
        return getCreationDate();

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