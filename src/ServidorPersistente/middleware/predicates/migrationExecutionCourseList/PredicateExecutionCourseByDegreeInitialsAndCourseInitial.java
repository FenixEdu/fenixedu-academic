/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.predicates.migrationExecutionCourseList;

import java.util.HashMap;
import java.util.List;

import ServidorPersistente.middleware.CourseCurricularYearPair;
import ServidorPersistente.middleware.MigrationExecutionCourse;

/**
 * @author jpvl
 */
public class PredicateExecutionCourseByDegreeInitialsAndCourseInitial
	extends PredicateForMigrationExecutionCourseList {

	/**
	 * @param degreeInitials
	 * @param courseInitials
	 * @param curricularYear
	 */
	public PredicateExecutionCourseByDegreeInitialsAndCourseInitial(
		String degreeInitials,
		String courseInitials,
		int curricularYear) {
//		this.courseInitialsToFind = courseInitials;
		this.degreeInitialsToFind = degreeInitials;
		pairToFind =
			new CourseCurricularYearPair(
				courseInitials,
				new Integer(curricularYear));
	}

//	private String courseInitialsToFind;
	private String degreeInitialsToFind;
	private CourseCurricularYearPair pairToFind;

	/* (non-Javadoc)
	 * @see ServidorPersistente.middleware.predicates.migrationExecutionCourseList.PredicateForMigrationExecutionCourseList#evaluateMigrationExecutionCourse(ServidorPersistente.middleware.MigrationExecutionCourse)
	 */
	public boolean evaluateMigrationExecutionCourse(MigrationExecutionCourse executionCourse) {
		HashMap degreeToSopCourse = executionCourse.getDegreeToSopCourse();
		List courseCurricularYearList =
			(List) degreeToSopCourse.get(degreeInitialsToFind);
		return (
			(courseCurricularYearList != null)
				&& (courseCurricularYearList.contains(pairToFind)));
	}

}
