/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class TeachingCareer extends TeachingCareer_Base {

    public TeachingCareer() {
        super();
        setOjbConcreteClass(TeachingCareer.class.getName());
    }

    public TeachingCareer(Teacher teacher, Category category, InfoTeachingCareer infoTeachingCareer) {
        if (teacher == null || category == null)
            throw new DomainException("Neither teacher nor category should be null!");

        setOjbConcreteClass(TeachingCareer.class.getName());
        setTeacher(teacher);
        setCategory(category);
        setBasicProperties(infoTeachingCareer);
    }

    public void delete() {
        removeCategory();
        super.delete();
    }

    public void edit(InfoTeachingCareer infoTeachingCareer, Category category) {
        if (category == null)
            throw new DomainException("The category should not be null!");

        setBasicProperties(infoTeachingCareer);
        this.setCategory(category);
    }

    private void setBasicProperties(InfoTeachingCareer infoTeachingCareer) {
        this.setBeginYear(infoTeachingCareer.getBeginYear());
        this.setEndYear(infoTeachingCareer.getEndYear());
        this.setCourseOrPosition(infoTeachingCareer.getCourseOrPosition());

    }

    @Override
    public void setCourseOrPosition(String courseOrPosition) {
        if (courseOrPosition != null && courseOrPosition.length() > 100) {
            throw new DomainException("error.courseOrPosition.max.length.exceeded");
        }
        super.setCourseOrPosition(courseOrPosition);
    }

}
