/*
 * InfoShift.java
 *
 * Created on December 23rd, 2002, 17:02
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import java.util.List;

public class InfoCourseExecutionAndListOfTypeLessonAndInfoShift extends InfoObject {
  protected InfoExecutionCourse _infoExecutionCourse;
  protected List _typeLessonsAndInfoShifts;

  public InfoCourseExecutionAndListOfTypeLessonAndInfoShift() { }

  public InfoCourseExecutionAndListOfTypeLessonAndInfoShift(InfoExecutionCourse infoExecutionCourse, List typeLessonsAndInfoShifts) {
    setInfoExecutionCourse(infoExecutionCourse);
	setTypeLesson(typeLessonsAndInfoShifts);
  }

  public List getTypeLessonsAndInfoShifts() {
    return _typeLessonsAndInfoShifts;
  }

  public void setTypeLesson(List typeLessonsAndInfoShifts) {
    _typeLessonsAndInfoShifts = typeLessonsAndInfoShifts;
  }
    
  public InfoExecutionCourse getInfoExecutionCourse() {
    return _infoExecutionCourse;
  }
    
  public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
    _infoExecutionCourse = infoExecutionCourse;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof InfoCourseExecutionAndListOfTypeLessonAndInfoShift) {
		InfoCourseExecutionAndListOfTypeLessonAndInfoShift tLAIS = (InfoCourseExecutionAndListOfTypeLessonAndInfoShift)obj;
    	resultado = getTypeLessonsAndInfoShifts().equals(tLAIS.getTypeLessonsAndInfoShifts()) &&
					getInfoExecutionCourse().equals(tLAIS.getInfoExecutionCourse());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[INFOCOURSEEXECUTIONANDLISTOFTYPELESSONANDINFOSHIFTS";
    result += "]";
    return result;
  }

}
