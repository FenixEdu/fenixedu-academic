/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;

/**
 * @author jpvl
 */
public class MigrationExecutionCourse {
	private String abreviation;

	private List associatedCurricularCourses;

	private HashMap degreeToSopCourse;

	private IExecutionCourse executionCourse = null;
	private String initials;
	private boolean migrated = false;
	private String name;

	private double theoreticalHours,
		practicalHours,
		theoreticalPraticalHours,
		laboratoryHours;
	public MigrationExecutionCourse() {
		associatedCurricularCourses = new ArrayList();
		degreeToSopCourse = new HashMap();
	}

	/**
	 * @param curricularCourse
	 */
	public void addCurricularCourse(ICurricularCourse curricularCourse) {

//		try {
//			this.theoreticalHours =
//				Math.max(
//					this.theoreticalHours,
//					curricularCourse.getTheoreticalHours().doubleValue());
//		} catch (RuntimeException e) {
//			//ignored
//		}
//		try {
//
//			this.practicalHours =
//				Math.max(
//					this.practicalHours,
//					curricularCourse.getPraticalHours().doubleValue());
//		} catch (RuntimeException e) {
//			//ignored
//		}
//		try {
//
//			this.theoreticalPraticalHours =
//				Math.max(
//					this.theoreticalPraticalHours,
//					curricularCourse.getTheoPratHours().doubleValue());
//		} catch (RuntimeException e) {
//			//ignored
//		}
//		try {
//
//			this.laboratoryHours =
//				Math.max(
//					this.laboratoryHours,
//					curricularCourse.getLabHours().doubleValue());
//		} catch (RuntimeException e) {
//			//ignored
//		}

		this.associatedCurricularCourses.add(curricularCourse);
	}

	public void addExecutionDegreeSopCourseRelation(
		String degreeInitials,
		String courseInitials,
		Integer curricularYear) {

		CourseCurricularYearPair courseCurricularYearPair =
			new CourseCurricularYearPair(courseInitials, curricularYear);

		List courseList = (List) degreeToSopCourse.get(degreeInitials);

		if (courseList == null) {
			courseList = new ArrayList();
			degreeToSopCourse.put(degreeInitials, courseList);
		}

		if (!courseList.contains(courseCurricularYearPair)) {
			courseList.add(courseCurricularYearPair);
		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return getInitials().equals(
			((MigrationExecutionCourse) obj).getInitials());
	}

	/**
	 * @return String
	 */
	public String getAbreviation() {
		return abreviation;
	}

	/**
	 * @return List
	 */
	public List getAssociatedCurricularCourses() {
		return associatedCurricularCourses;
	}
	/**
	 * @return HashMap
	 */
	public HashMap getDegreeToSopCourse() {
		return degreeToSopCourse;
	}

	/**
	 * @return IDisciplinaExecucao
	 */
	public IExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @param lessonType
	 * @return double
	 */
	public double getHoursFromType(String lessonType) {
		double hours = 0;
		if (lessonType.equals("Teo")) {
			hours = getTheoreticalHours();
		} else if (lessonType.equals("Pra")) {
			hours = getPracticalHours();
		} else if (lessonType.equals("Lab")) {
			hours = getLaboratoryHours();
		} else if (lessonType.equals("TP")) {
			hours = getTheoreticalPraticalHours();
		}
		return hours;
	}

	/**
	 * @return String
	 */
	public String getInitials() {
		return initials;
	}

	/**
	 * @return double
	 */
	public double getLaboratoryHours() {
		return laboratoryHours;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return double
	 */
	public double getPracticalHours() {
		return practicalHours;
	}

	/**
	 * @return double
	 */
	public double getTheoreticalHours() {
		return theoreticalHours;
	}

	/**
	 * @return double
	 */
	public double getTheoreticalPraticalHours() {
		return theoreticalPraticalHours;
	}

	/**
	 * @return boolean
	 */
	public boolean isMigrated() {
		return migrated;
	}

	/**
	 * Sets the abreviation.
	 * @param abreviation The abreviation to set
	 */
	public void setAbreviation(String abreviation) {
		this.abreviation = abreviation;
	}

	/**
	 * Sets the executionCourse.
	 * @param executionCourse The executionCourse to set
	 */
	public void setExecutionCourse(IExecutionCourse executionCourse) {
		this.executionCourse = executionCourse;
	}

	/**
	 * Sets the initials.
	 * @param initials The initials to set
	 */
	public void setInitials(String initials) {
		this.initials = initials;
	}

	/**
	 * Sets the migrated.
	 * @param migrated The migrated to set
	 */
	public void setMigrated(boolean migrated) {
		this.migrated = migrated;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer("");
		stringBuffer
			.append("Disciplina Execução (Code: ")
			.append(getInitials())
			.append(")")
			.append("\r\n\t(Abreviation: ")
			.append(getAbreviation())
			.append(")")
			.append("\r\n\t (Nome: ")
			.append(getName())
			.append(")\r\n\t")
			.append("(TP:")
			.append(getTheoreticalPraticalHours())
			.append(")")
			.append("(L:")
			.append(getLaboratoryHours())
			.append(")")
			.append("(T:")
			.append(getTheoreticalHours())
			.append(")")
			.append("(P:")
			.append(getPracticalHours())
			.append(")")
			.append("\r\n");

		Iterator iteratorDegrees = degreeToSopCourse.keySet().iterator();

		while (iteratorDegrees.hasNext()) {
			String degreeInitials = (String) iteratorDegrees.next();
			stringBuffer.append("\t").append(degreeInitials);
			List pairList = (List) degreeToSopCourse.get(degreeInitials);
			Iterator courseIterator = pairList.iterator();
			while (courseIterator.hasNext()) {
				CourseCurricularYearPair pair =
					(CourseCurricularYearPair) courseIterator.next();
				stringBuffer
					.append("\t(")
					.append(pair.getCourseInitials())
					.append(" - ")
					.append(pair.getCurricularYear())
					.append("º ano")
					.append(")");
			}
			stringBuffer.append("\r\n");
		}

		if (!associatedCurricularCourses.isEmpty()) {

			Iterator curricularCoursesIterator =
				associatedCurricularCourses.iterator();
			stringBuffer.append("\tDisciplinas curriculares:\r\n");
			while (curricularCoursesIterator.hasNext()) {
				ICurricularCourse curricularCourse =
					(ICurricularCourse) curricularCoursesIterator.next();
				stringBuffer
					.append("\t\t(")
					.append("Nome:")
					.append(curricularCourse.getName())
					.append(",Curso:")
					.append(
						curricularCourse
							.getDegreeCurricularPlan()
							.getDegree()
							.getSigla())
					.append(",Ano:")
//					.append(curricularCourse.getCurricularYear())
					.append(")\r\n");
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * Sets the laboratoryHours.
	 * @param laboratoryHours The laboratoryHours to set
	 */
	public void setLaboratoryHours(double laboratoryHours) {
		this.laboratoryHours = laboratoryHours;
	}

	/**
	 * Sets the practicalHours.
	 * @param practicalHours The practicalHours to set
	 */
	public void setPracticalHours(double practicalHours) {
		this.practicalHours = practicalHours;
	}

	/**
	 * Sets the theoreticalHours.
	 * @param theoreticalHours The theoreticalHours to set
	 */
	public void setTheoreticalHours(double theoreticalHours) {
		this.theoreticalHours = theoreticalHours;
	}

	/**
	 * Sets the theoreticalPraticalHours.
	 * @param theoreticalPraticalHours The theoreticalPraticalHours to set
	 */
	public void setTheoreticalPraticalHours(double theoreticalPraticalHours) {
		this.theoreticalPraticalHours = theoreticalPraticalHours;
	}

}
