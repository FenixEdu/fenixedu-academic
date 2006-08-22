/*
 * Created on 16/Abr/2005 - 11:39:14
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

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

	public InfoTeacherWithRemainingClassTypes(final Teacher teacher, InfoExecutionCourse infoExecutionCourse) {
		super(teacher);

		if (infoExecutionCourse.getTheoreticalHours().doubleValue() > 0) {
			this.remainingClassTypes.add(ShiftType.TEORICA);
		}
		if (infoExecutionCourse.getPraticalHours().doubleValue() > 0) {
			this.remainingClassTypes.add(ShiftType.PRATICA);
		}
		if (infoExecutionCourse.getLabHours().doubleValue() > 0) {
			this.remainingClassTypes.add(ShiftType.LABORATORIAL);
		}
		if (infoExecutionCourse.getTheoPratHours().doubleValue() > 0) {
			this.remainingClassTypes.add(ShiftType.TEORICO_PRATICA);
		}
	}

}
