/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class TeachingCareer extends Career implements ITeachingCareer {

    private ICategory category;

    private Integer keyCategory;

    private String courseOrPosition;

    /**
     *  
     */
    public TeachingCareer() {
        super();
    }

    /**
     * @param idInternal
     */
    public TeachingCareer(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the category.
     */
    public ICategory getCategory() {
        return category;
    }

    /**
     * @param category
     *            The category to set.
     */
    public void setCategory(ICategory category) {
        this.category = category;
    }

    /**
     * @return Returns the courseOrPosition.
     */
    public String getCourseOrPosition() {
        return courseOrPosition;
    }

    /**
     * @param courseOrPosition
     *            The courseOrPosition to set.
     */
    public void setCourseOrPosition(String courseOrPosition) {
        this.courseOrPosition = courseOrPosition;
    }

    /**
     * @return Returns the keyCategory.
     */
    public Integer getKeyCategory() {
        return keyCategory;
    }

    /**
     * @param keyCategory
     *            The keyCategory to set.
     */
    public void setKeyCategory(Integer keyCategory) {
        this.keyCategory = keyCategory;
    }

    public String toString() {
        String result = "[Dominio.teacher.TeachingCareer ";
        result += ", beginYear=" + getBeginYear();
        result += ", endYear=" + getEndYear();
        result += ", category=" + getCategory();
        result += ", courseOrPosition=" + getCourseOrPosition();
        result += ", teacher=" + getTeacher();
        result += "]";
        return result;
    }
}