package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author tfc130
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses implements ISiteComponent {

	private InfoExecutionCourse infoExecutionCourse;	
	private List associatedCurricularCourses;
	private List infoShiftsWithAssociatedClassesList;

	/**
	 * Constructor for InfoShiftWithAssociatedInfoClassesAndInfoLessons.
	 */
	public InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses() {
		super();
	}

	/**
	 * Constructor for InfoShiftWithAssociatedInfoClassesAndInfoLessons.
	 */
	public InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses(InfoExecutionCourse infoExecutionCourse, List associatedCurricularCourses, List infoShiftsWithAssociatedClassesList) {
		super();
		setInfoExecutionCourse(infoExecutionCourse);
		setAssociatedCurricularCourses(associatedCurricularCourses);
		setInfoShiftsWithAssociatedClassesList(infoShiftsWithAssociatedClassesList);
	}

	

		public boolean equals(Object objectToCompare) {
			boolean result = false;
			if (objectToCompare instanceof InfoSiteAssociatedCurricularCourses) {
				result = true;
			}

			if (((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses() == null
				&& this.getAssociatedCurricularCourses() == null) {
				return true;
			}
			if (((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses() == null
				|| this.getAssociatedCurricularCourses() == null
				|| ((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses().size() != this.getAssociatedCurricularCourses().size()) {
				return false;
			}
			ListIterator iter1 = ((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses().listIterator();
			ListIterator iter2 = this.getAssociatedCurricularCourses().listIterator();
			while (result && iter1.hasNext()) {
				InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) iter1.next();
				InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) iter2.next();
				if (!infoCurricularCourse1.equals(infoCurricularCourse2)) {
					result = false;
				}
			}

			return result;
		}



	/**
	 * @return
	 */
	public List getAssociatedCurricularCourses() {
		return associatedCurricularCourses;
	}

	/**
	 * @param list
	 */
	public void setAssociatedCurricularCourses(List list) {
		associatedCurricularCourses = list;
	}

	/**
	 * @return
	 */
	public List getInfoShiftsWithAssociatedClassesList() {
		return infoShiftsWithAssociatedClassesList;
	}

	/**
	 * @param list
	 */
	public void setInfoShiftsWithAssociatedClassesList(List list) {
		infoShiftsWithAssociatedClassesList = list;
	}
	
	/**
		 * @return
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * @param course
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

}
