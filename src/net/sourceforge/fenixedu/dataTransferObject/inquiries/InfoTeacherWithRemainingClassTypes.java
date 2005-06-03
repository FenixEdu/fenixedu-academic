/*
 * Created on 16/Abr/2005 - 11:39:14
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ShiftType;
/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoTeacherWithRemainingClassTypes extends InfoTeacher {
		
	final private List<ShiftType> remainingClassTypes = new ArrayList<ShiftType>();

	/**
	 * @return Returns the remainingClassTypes.
	 */
	public List<ShiftType> getRemainingClassTypes() {
		return remainingClassTypes;
	}
	


	public InfoTeacherWithRemainingClassTypes(InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse) {
		if(infoTeacher != null) {
			this.setIdInternal(infoTeacher.getIdInternal());
			this.setTeacherNumber(infoTeacher.getTeacherNumber());
		    this.setInfoPerson(infoTeacher.getInfoPerson());
		    this.setInfoCategory(infoTeacher.getInfoCategory());
			this.setProfessorShipsExecutionCourses(infoTeacher.getProfessorShipsExecutionCourses());
		    this.setResponsibleForExecutionCourses(infoTeacher.getResponsibleForExecutionCourses());
			
			if(infoExecutionCourse.getTheoreticalHours().doubleValue() > 0) {
				this.remainingClassTypes.add(ShiftType.TEORICA);
			}
			if(infoExecutionCourse.getPraticalHours().doubleValue() > 0) {
				this.remainingClassTypes.add(ShiftType.PRATICA);
			}
			if(infoExecutionCourse.getLabHours().doubleValue() > 0) {
				this.remainingClassTypes.add(ShiftType.LABORATORIAL);
			}
			if(infoExecutionCourse.getTheoPratHours().doubleValue() > 0) {
				this.remainingClassTypes.add(ShiftType.TEORICO_PRATICA);			
			}
		}
		
	}
	

	public InfoTeacherWithRemainingClassTypes() {
	}

	public void copyFromDomain(ITeacher teacher) {
		super.copyFromDomain(teacher);
	}
	
    public static InfoTeacherWithRemainingClassTypes newInfoFromDomain(ITeacher teacher) {
        InfoTeacherWithRemainingClassTypes infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacherWithRemainingClassTypes();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }
	
	
	

}
