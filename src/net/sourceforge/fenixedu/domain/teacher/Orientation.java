/*
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class Orientation extends DomainObject implements IOrientation {
    private Integer keyTeacher;

    private ITeacher teacher;

    private OrientationType orientationType;

    private Integer numberOfStudents;

    private String description;

    private Date lastModificationDate;

    /**
     *  
     */
    public Orientation() {
        super();

    }

    /**
     * @param orientationId
     */
    public Orientation(Integer idInternal) {
        super(idInternal);

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
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
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
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
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

}