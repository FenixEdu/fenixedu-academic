/**
 * Dec 20, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class TeacherWithCreditsDTO {

    private Teacher teacher;
    private CreditLineDTO creditLineDTO;
    private String category;

    public TeacherWithCreditsDTO(Teacher teacher, String category, CreditLineDTO creditLineDTO) {
        setTeacher(teacher);
        setCategory(category);
        setCreditLineDTO(creditLineDTO);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CreditLineDTO getCreditLineDTO() {
        return creditLineDTO;
    }

    public void setCreditLineDTO(CreditLineDTO creditLineDTO) {
        this.creditLineDTO = creditLineDTO;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
