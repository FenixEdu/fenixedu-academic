/*
 * Created on 7/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author jpvl
 */
public class CurricularCourseLoad {
    private Integer idInternal;

    private String degreeCode;

    private String courseName;

    private Double theoHours;

    private Double pratHours;

    private Double theoPratHours;

    private Double labHours;

    public CurricularCourseLoad() {
    }

    /**
     * @return Integer
     */
    public Integer getIdInternal() {
        return idInternal;
    }

    /**
     * @return Double
     */
    public Double getLabHours() {
        return labHours;
    }

    /**
     * @return Double
     */
    public Double getPratHours() {
        return pratHours;
    }

    /**
     * @return Double
     */
    public Double getTheoHours() {
        return theoHours;
    }

    /**
     * @return Double
     */
    public Double getTheoPratHours() {
        return theoPratHours;
    }

    /**
     * Sets the idInternal.
     * 
     * @param idInternal
     *            The idInternal to set
     */
    public void setIdInternal(Integer idInternal) {
        this.idInternal = idInternal;
    }

    /**
     * Sets the labHours.
     * 
     * @param labHours
     *            The labHours to set
     */
    public void setLabHours(Double labHours) {
        this.labHours = labHours;
    }

    /**
     * Sets the pratHours.
     * 
     * @param pratHours
     *            The pratHours to set
     */
    public void setPratHours(Double pratHours) {
        this.pratHours = pratHours;
    }

    /**
     * Sets the theoHours.
     * 
     * @param theoHours
     *            The theoHours to set
     */
    public void setTheoHours(Double theoHours) {
        this.theoHours = theoHours;
    }

    /**
     * Sets the theoPratHours.
     * 
     * @param theoPratHours
     *            The theoPratHours to set
     */
    public void setTheoPratHours(Double theoPratHours) {
        this.theoPratHours = theoPratHours;
    }

    /**
     * @return String
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * @return String
     */
    public String getDegreeCode() {
        return degreeCode;
    }

    /**
     * Sets the courseName.
     * 
     * @param courseName
     *            The courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Sets the degreeCode.
     * 
     * @param degreeCode
     *            The degreeCode to set
     */
    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

}