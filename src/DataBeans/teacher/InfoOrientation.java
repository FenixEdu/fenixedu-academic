/*
 * Created on 21/Nov/2003
 *  
 */
package DataBeans.teacher;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Dominio.teacher.IOrientation;
import Util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoOrientation extends InfoObject {
    private InfoTeacher infoTeacher;

    private OrientationType orientationType;

    private String description;

    private Integer numberOfStudents;

    private Date lastModificationDate;

    /**
     *  
     */
    public InfoOrientation() {
        super();
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return Returns the numberOfStudents.
     */
    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    /**
     * @param numberOfStudents
     *            The numberOfStudents to set.
     */
    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    /**
     * @return Returns the orientationType.
     */
    public OrientationType getOrientationType() {
        return orientationType;
    }

    /**
     * @param orientationType
     *            The orientationType to set.
     */
    public void setOrientationType(OrientationType orientationType) {
        this.orientationType = orientationType;
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

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IOrientation orientation) {
        super.copyFromDomain(orientation);
        if (orientation != null) {
            setDescription(orientation.getDescription());
            setLastModificationDate(orientation.getLastModificationDate());
            setNumberOfStudents(orientation.getNumberOfStudents());
            setOrientationType(orientation.getOrientationType());
        }
    }

    public static InfoOrientation newInfoFromDomain(IOrientation orientation) {
        InfoOrientation infoOrientation = null;
        if (orientation != null) {
            infoOrientation = new InfoOrientation();
            infoOrientation.copyFromDomain(orientation);
        }

        return infoOrientation;
    }
}