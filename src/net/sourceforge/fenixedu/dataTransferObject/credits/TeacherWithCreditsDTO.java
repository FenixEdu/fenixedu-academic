/**
* Dec 20, 2005
*/
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ICategory;

/**
 * @author Ricardo Rodrigues
 *
 */

public class TeacherWithCreditsDTO {

    private ITeacher teacher;
    private CreditLineDTO creditLineDTO;
    private ICategory category;
    
    public TeacherWithCreditsDTO(ITeacher teacher, ICategory category, CreditLineDTO creditLineDTO){
        setTeacher(teacher);
        setCategory(category);
        setCreditLineDTO(creditLineDTO);
    }

    public ICategory getCategory() {
        return category;
    }

    public void setCategory(ICategory category) {
        this.category = category;
    }

    public CreditLineDTO getCreditLineDTO() {
        return creditLineDTO;
    }

    public void setCreditLineDTO(CreditLineDTO creditLineDTO) {
        this.creditLineDTO = creditLineDTO;
    }

    public ITeacher getTeacher() {
        return teacher;
    }

    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }
}


