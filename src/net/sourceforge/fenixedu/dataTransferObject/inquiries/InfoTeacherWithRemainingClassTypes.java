/*
 * Created on 16/Abr/2005 - 11:39:14
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoTeacherWithRemainingClassTypes extends InfoTeacher {

    final private List<ShiftType> remainingClassTypes = new ArrayList<ShiftType>();

    public List<ShiftType> getRemainingClassTypes() {
	return remainingClassTypes;
    }

    public InfoTeacherWithRemainingClassTypes(final Teacher teacher, InfoExecutionCourse infoExecutionCourse) {

	super(teacher);
	
	Map<ShiftType, CourseLoad> courseLoadsMap = infoExecutionCourse.getExecutionCourse().getCourseLoadsMap();
	
	if(courseLoadsMap.containsKey(ShiftType.TEORICA) && !courseLoadsMap.get(ShiftType.TEORICA).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.TEORICA);
	}
	if(courseLoadsMap.containsKey(ShiftType.PRATICA) && !courseLoadsMap.get(ShiftType.PRATICA).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.PRATICA);
	}
	if(courseLoadsMap.containsKey(ShiftType.LABORATORIAL) && !courseLoadsMap.get(ShiftType.LABORATORIAL).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.LABORATORIAL);
	}
	if(courseLoadsMap.containsKey(ShiftType.TEORICO_PRATICA) && !courseLoadsMap.get(ShiftType.TEORICO_PRATICA).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.TEORICO_PRATICA);			
	}
	if(courseLoadsMap.containsKey(ShiftType.SEMINARY) && !courseLoadsMap.get(ShiftType.SEMINARY).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.SEMINARY);
	}
	if(courseLoadsMap.containsKey(ShiftType.PROBLEMS) && !courseLoadsMap.get(ShiftType.PROBLEMS).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.PROBLEMS);
	}
	if(courseLoadsMap.containsKey(ShiftType.FIELD_WORK) && !courseLoadsMap.get(ShiftType.FIELD_WORK).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.FIELD_WORK);
	}
	if(courseLoadsMap.containsKey(ShiftType.TRAINING_PERIOD) && !courseLoadsMap.get(ShiftType.TRAINING_PERIOD).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.TRAINING_PERIOD);
	}
	if(courseLoadsMap.containsKey(ShiftType.TUTORIAL_ORIENTATION) && !courseLoadsMap.get(ShiftType.TUTORIAL_ORIENTATION).isEmpty()) {
	    this.remainingClassTypes.add(ShiftType.TUTORIAL_ORIENTATION);
	}
    }
}
