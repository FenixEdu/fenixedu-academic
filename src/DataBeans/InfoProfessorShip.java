/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package DataBeans;

/**
 * @author jpvl
 */
public class InfoProfessorShip extends InfoObject {
	protected InfoTeacher infoTeacher;
	protected InfoExecutionCourse infoExecutionCourse;

	/**
	 * @return
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * @return
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @param course
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

	/**
	 * @param teacher
	 */
	public void setInfoTeacher(InfoTeacher teacher) {
		infoTeacher = teacher;
	}

}
